package com.ccl.convert;

import org.springframework.context.ApplicationContext;

/**
 * @author
 */
public class ConvertContext {

    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        ConvertContext.context = context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

}
