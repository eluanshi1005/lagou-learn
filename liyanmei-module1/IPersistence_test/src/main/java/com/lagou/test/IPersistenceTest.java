package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {
    SqlSession sqlSession;

    @Before
    public void init() throws Exception{
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test() throws Exception {


        //调用
        User user = new User();
        user.setId(2);
        user.setUsername("zhangsan");
      /*  User user2 = sqlSession.selectOne("user.selectOne", user);

        System.out.println(user2);*/

       /* List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }
        User byCondition = userDao.findByCondition(user);
        System.out.println(byCondition);
    }

    @Test
    public void testInsert()throws Exception{
        User user = new User();
        user.setId(3);
        user.setUsername("lym");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int i = userDao.insert(user);
        System.out.println("插入了" + i +"条记录");
    }

    @Test
    public void testUpdate()throws Exception{
        User user = new User();
        user.setId(3);
        user.setUsername("lym1");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int i = userDao.update(user);
        System.out.println("更新了" + i +"条记录");
    }
    @Test
    public void testDelete()throws Exception{
        User user = new User();
        user.setId(3);
        user.setUsername("lym1");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int i = userDao.delete(user);
        System.out.println("删除了" + i +"条记录");
    }
}
