package server;

import mapper.Context;
import mapper.Host;
import mapper.Mapper;
import mapper.Wrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Minicat的主类
 */
public class Bootstrap {

    /**定义socket监听的端口号*/
    private int port = 0;

    private Mapper mapper = new Mapper();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();

    /**
     * Minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {

        // 加载解析相关的配置，web.xml
        // loadServlet();

        // 加载 port、 初始化 mapper
        loadServer();


        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize =50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );





        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);

        /*while(true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello Minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }*/


        /**
         * 完成Minicat 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());
            socket.close();

        }*/


        /**
         * 完成Minicat 3.0版本
         * 需求：可以请求动态资源（Servlet）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();

        }
*/

        /*
            多线程改造（不使用线程池）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            requestProcessor.start();
        }*/



        /*
            多线程改造（使用线程池）
         */
        /*System.out.println("=========>>>>>>使用线程池进行多线程改造");
        while(true) {

            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            //requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }*/


        /**
         * 完成Minicat 4.0版本
         * 需求：模拟出 webapps 部署效果
         */
        while(true) {

            Socket socket = serverSocket.accept();
            RequestProcessorV4 requestProcessor = new RequestProcessorV4(socket, mapper);
            //requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }

    }

    /**
     * 解析 server.xml
     */
    private void loadServer() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.selectNodes("//Service");
            for (Element element : selectNodes) {
                Element connectorElement = (Element) element.selectSingleNode("Connector");
                port = Integer.valueOf(connectorElement.attribute("port").getStringValue());
                Element engineElement = (Element) element.selectSingleNode("Engine");
                List<Element> hostNodes = engineElement.selectNodes("Host");
                for (Element hostNode : hostNodes) {
                    String hostName = hostNode.attribute("name").getStringValue();
                    String webapps = hostNode.attribute("appBase").getStringValue();
                    System.out.println(hostName + " " + webapps);
                    mapper.addHost(parseHost(hostName, webapps));
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 封装 Host
     * @param hostName 如：host 中的 name 属性
     * @param webapps  如：host 中的 appBase 属性
     * @return
     */
    private Host parseHost(String hostName, String webapps) {
        File[] projects = new File(webapps).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        List<Context> contexts = parseContexts(projects);
        Host host = new Host(hostName, contexts);
        return host;
    }


    /**
     * 把 appBase 目录下的项目封装成 Context
     * @param projects webapps 目录下的项目文件
     * @return
     */
    private List<Context> parseContexts(File[] projects) {
        List<Context> contexts = new ArrayList<>();
        for (File projectFile : projects) {
            String proPath = projectFile.getPath();
            String contextName = projectFile.getName();//项目名称 demo1 或 demo2
            List<Wrapper> wrappers = parseWrappers(proPath);
            Context context = new Context(contextName, wrappers);
            contexts.add(context);
        }
        return contexts;
    }


    /**
     * 解析web.xml，封装 Wrapper
     * @return
     */
    private List<Wrapper> parseWrappers(String proPath) {
        SAXReader saxReader = new SAXReader();
        List<Wrapper> wrappers = new ArrayList<>();
        try {
            File webXML = new File(proPath + "/web.xml");
            InputStream resourceAsStream = new FileInputStream(webXML);
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                Servlet servlet = loadServlet(proPath, servletClass);
                HttpServlet httpServlet = (HttpServlet) servlet;

                Wrapper wrapper = new Wrapper(urlPattern, httpServlet);

                wrappers.add(wrapper);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrappers;
    }

    /**
     * 根据 servletName 读取指定目录下的 Servlet class文件，进行实例化
     * @param proPath
     * @param servletClassName
     * @return
     * @throws Exception
     */
    private Servlet loadServlet(String proPath, String servletClassName) throws Exception {
        //每个项目，类加载器，去加载置顶位置的class信息
//        URL classUrl = new URL("file:/" + proPath +"/"); // windows url
        URL classUrl = new URL("file:" + proPath +"/"); // mac url
        URLClassLoader servletClassLoader = new URLClassLoader(new URL[] {classUrl});
        //1、 加载到JVM
        Class<?> servletClass = servletClassLoader.loadClass(servletClassName);
        //2、 实例化
        Servlet servlet = (Servlet) servletClass.newInstance();
        servletClassLoader.close();
        return servlet;
    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }



        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * Minicat 的程序启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
