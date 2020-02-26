package com.lagou.sqlSession;

import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementid,Object... params) throws Exception;

    //根据条件查询单个
    public <T> T selectOne(String statementid,Object... params) throws Exception;

    // 插入
    public int insert(String statement, Object... parameter) throws Exception;

    // 修改
    public int update(String statement, Object... parameter) throws Exception;

    // 删除
    public int delete(String statement, Object... parameter) throws Exception;

    //为Dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);


}
