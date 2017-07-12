package com.hframework.listener;

import com.hframework.base.service.DataSetLoaderService;
import com.hframework.base.service.ModelLoaderService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zhangquanhong on 2016/6/13.
 */
public class ModelLoaderListener implements ServletContextListener {

    public ModelLoaderListener() {
    }

    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext webappcontext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(event.getServletContext());
        ModelLoaderService modelLoaderService =webappcontext.getBean(ModelLoaderService.class);
        modelLoaderService.load(event.getServletContext());
    }



    public void contextDestroyed(ServletContextEvent event) {
    }
}
