package com.lagou.edu.factory;

import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.annotation.Repository;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.annotation.Transactional;
import com.lagou.edu.utils.ClassUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 根据注解生成对象
 * @author YanMei.Li
 */
public class AnnotationBeanFactory {
    private static String packagePath;
    private static ConcurrentHashMap<String,Object> beans = new ConcurrentHashMap<String,Object>(); // 存储对象
    private static ConcurrentHashMap<String,Object> aopBeans = new ConcurrentHashMap<String,Object>(); // 存储委托代理对象

    static {
        // 加载xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//scan");
            packagePath = beanList.get(0).attributeValue("package");
            beans = initBeans(); // 生成 Bean , 但无属性值
            di(); // 依赖注入
            aop();  // 动态代理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void aop() throws Exception {
        ProxyFactory proxyFactory = (ProxyFactory) getBean("proxyFactory");
        Set<Map.Entry<String, Object>> entries = aopBeans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String beanId = entry.getKey();
            Object obj = entry.getValue();
            Object proxy = proxyFactory.createAopProxy(obj);
            beans.put(beanId, proxy);
        }
    }

    private static void di() throws Exception {
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            doInjection(value);
        }
    }

    private static void doInjection(Object obj) throws Exception {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
            if(autowired != null){
                String name = field.getName();
                Object bean = getBean(name);
                field.setAccessible(true);
                field.set(obj, bean);
            }
        }
    }

    public static Object getBean(String beanId){
        return beans.get(beanId);
    }

    private static ConcurrentHashMap<String, Object> initBeans() throws Exception {
        List<Class<?>> classes = ClassUtil.getClasses(packagePath);
        ConcurrentHashMap<String,Object> beans = findClassExistAnnotation(classes);
        if(beans == null || beans.isEmpty()){
            throw new Exception("当前包下没有注解的类");
        }
        return beans;
    }

    private static ConcurrentHashMap<String, Object> findClassExistAnnotation(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            Service serviceAnnotation = clazz.getDeclaredAnnotation(Service.class);
            Repository repositoryAnnotation = clazz.getDeclaredAnnotation(Repository.class);
            Transactional transactionalAnnotation = clazz.getDeclaredAnnotation(Transactional.class);
            if(serviceAnnotation == null && repositoryAnnotation == null && transactionalAnnotation == null) continue;
            String className = clazz.getSimpleName();
            String beanId = toLowerCaseFirstLetter(className);
            if(serviceAnnotation != null){
                String serviceId = serviceAnnotation.value();
                if(serviceId != null && serviceId.trim().length() > 0){
                    beanId = serviceAnnotation.value();
                }
            }
            if(repositoryAnnotation != null) {
                String repositoryId = repositoryAnnotation.value();
                if (repositoryId != null && repositoryId.trim().length() > 0) {
                    beanId = repositoryAnnotation.value();
                }
            }
            Object bean = clazz.getDeclaredConstructor().newInstance();
            beans.put(beanId, bean);
            if(transactionalAnnotation != null){
                aopBeans.put(beanId, bean);
            }
        }
        return beans;
    }

    private static String toLowerCaseFirstLetter(String s) {
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
