package mapper;

import server.Servlet;

import java.util.List;

/**
 * 一个Context包含多个Wrapper
 */
public class Context {
    // 项目名
    String name;
    //精确匹配的Wrapper
    List<Wrapper> wrappers;

    public Context() {
    }

    public Context(String name) {
        this.name = name;
    }

    public Context(String name, List<Wrapper> wrappers) {
        this.name = name;
        this.wrappers = wrappers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Wrapper> getWrappers() {
        return wrappers;
    }

    public void setWrappers(List<Wrapper> wrappers) {
        this.wrappers = wrappers;
    }
}

