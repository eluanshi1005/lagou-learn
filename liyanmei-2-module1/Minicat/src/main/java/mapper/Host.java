package mapper;

import java.util.List;

/**
 * 一个Host对象包含多个context
 */
public class Host {
    private String name;
    // 存储的是一系列的MapperContext
    private List<Context> contexts;

    public Host() {
    }

    public Host(String name) {
        this.name = name;
    }

    public Host(String name, List<Context> contexts) {
        this.name = name;
        this.contexts = contexts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }
}
