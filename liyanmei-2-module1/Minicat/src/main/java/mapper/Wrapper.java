package mapper;

import server.Servlet;

/**
 * url-pattern 与 Servlet-class 的映射关系
 */
public class Wrapper {
    // url
    private String name;
    //servlet
    private Servlet servlet;


    public Wrapper() {
    }


    public Wrapper(String name, Servlet servlet) {
        this.name = name;
        this.servlet = servlet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

}
