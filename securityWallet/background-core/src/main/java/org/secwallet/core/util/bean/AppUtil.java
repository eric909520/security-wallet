package org.secwallet.core.util.bean;


import org.secwallet.core.model.OnlineUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppUtil implements ApplicationContextAware {

    private static  ApplicationContext applicationContext;
    /**
     * online user
     */
    private static Map<Long, OnlineUser> onlineUsers=new LinkedHashMap<Long, OnlineUser>();
    private static ServletContext servletContext;
    public static void init(ServletContext _servletContext)
    {
        servletContext=_servletContext;
    }
    /**
     * Get the ServletContext object of the web application。
     * @return
     * @throws Exception
     */
    public static ServletContext getServletContext() throws Exception{
        return servletContext;
    }

    /**
     * Inject context when spring starts
     */
    @Override
    public void setApplicationContext(ApplicationContext contex) throws BeansException {
        applicationContext=contex;
    }

    /**
     * Get the context of spring。
     * @return
     */
    public static ApplicationContext getContext(){
        return applicationContext;
    }

    /**
     * Get a list of implementing classes based on the specified interface or base class。
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Class> getImplClass(Class clazz) throws ClassNotFoundException{
        List<Class> list=new ArrayList<Class>();
        Map map= applicationContext.getBeansOfType(clazz);
        for(Object obj : map.values()){
            String name=obj.getClass().getName();
            int pos=name.indexOf("$$");
            if(pos>0){
                name=name.substring(0,name.indexOf("$$")) ;
            }
            Class cls= Class.forName(name);
            list.add(cls);
        }
        return list;
    }

    /**
     * Get the instance of the implementing class of the interface。
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    public static Map<String,Object> getImplInstance(Class clazz) throws ClassNotFoundException{
        Map map= applicationContext.getBeansOfType(clazz);
        return map;
    }

    /**
     * Get bean from spring context based on class。
     * @param cls
     * @return
     */
    public  static <C> C getBean(Class<C> cls){
        return applicationContext.getBean(cls);
    }

    /**
     * Get bean from spring context based on class name。
     * @param beanId
     * @return
     */
    public static Object getBean(String  beanId){
        return applicationContext.getBean(beanId);
    }

    /**
     * Get the absolute path of the application
     * @return
     */
    public static String getAppAbsolutePath(){
        return servletContext.getRealPath("/");
    }

    /**
     * Obtain the absolute path of the corresponding page according to the path of the web page in the web environment。
     * @param path
     * @return
     */
    public static String getRealPath(String path){
        return servletContext.getRealPath(path);
    }

    public static Map<Long, OnlineUser> getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * Get the Classpath physical path
     * @return
     */
    public static String getClasspath(){
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rootPath  = "";
        //windows
        if("\\".equals(File.separator)){
            rootPath  = classPath.substring(1);
            rootPath = rootPath.replace("/", "\\");
        }
        //linux
        if("/".equals(File.separator)){
            rootPath  = classPath.substring(1);
            rootPath = rootPath.replace("\\", "/");
        }
        return rootPath;
    }

    /**
     * Spring publish event。
     * @param applicationEvent
     */
    public static void publishEvent(ApplicationEvent applicationEvent){
        applicationContext.publishEvent(applicationEvent);
    }
}
