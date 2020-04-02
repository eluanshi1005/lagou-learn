package server;

import mapper.Context;
import mapper.Host;
import mapper.Mapper;
import mapper.Wrapper;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestProcessorV4 extends Thread {

    private Socket socket;
    private Mapper mapper;

    public RequestProcessorV4(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 根据 url
            HttpServlet httpServlet = getServlet(request, mapper);
            if(httpServlet == null) {
                // 静态资源处理
                response.outputHtml(request.getUrl());
            }else{
               // 动态资源servlet请求
                httpServlet.service(request, response);
            }

            socket.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HttpServlet getServlet(Request request, Mapper mapper) {
        String hostname = request.getHostname();
        String reqURL = request.getUrl();
        String[] split = reqURL.split("/");
        String reqcontext = split[1];
        String reqUrl = getLocationStr(reqURL,2, "/"); // 获取从第2个“/”开始截取的字符串
        System.out.println("hostname=" + hostname + " context=" + reqcontext + " url=" + reqUrl);
        List<Host> hosts = mapper.getHosts();
        for (Host host : hosts) {
            if(host.getName().equals(hostname)){
                List<Context> contexts = host.getContexts();
                for (Context context : contexts) {
                    if(context.getName().equals(reqcontext)){
                        List<Wrapper> wrappers = context.getWrappers();
                        for (Wrapper wrapper : wrappers) {
                            if(wrapper.getName().equals(reqUrl)){
                                HttpServlet servlet = (HttpServlet) wrapper.getServlet();
                                return servlet;
                            }
                        }
                        
                    }
                }

            }
        }
        
        return null;
    }

    /**
     *@Author huangzx
     *@Description: 从倒数第n个"/"开始截取到最后
     *@Date 11:41 AM 7/31/19
     *@Param [str, n]
     *@return java.lang.String
     */
    private String getLocationStr(String str,int n, String separator){
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(separator).matcher(str);
        int mIdx = 0;

        while(slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if(mIdx == n){
                break;
            }
        }
        return str.substring(slashMatcher.start());
    }
}
