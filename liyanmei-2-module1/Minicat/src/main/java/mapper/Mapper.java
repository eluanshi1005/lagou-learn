package mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个Service有一个Engine，而一个Engine中有一个Mapper
 * Mapper主要功能是完成url到Wrapper的映射
 */
public class Mapper {
    //定义所有的Host组合，表示一个Engine下所有Host
    private List<Host> hosts = new ArrayList<>();

    public Mapper() {
    }

    public Mapper(List<Host> hosts) {
        this.hosts = hosts;
    }

    public void addHost(Host host){
        this.hosts.add(host);
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }


}
