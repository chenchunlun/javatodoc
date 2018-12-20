package com.ccl.convert.operate;

import com.ccl.convert.ConvertContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author
 */
@Component
public class Operator implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        ConvertContext.setContext(context);

        String srcPath = (String) context.getBean("srcPath");
        srcPath = srcPath + File.separator + "src" + File.separator + "main" + File.separator + "java";
        Utils.traverse(srcPath);
        Utils.clearComment("UTF-8");

        try {
            Utils.generateDoc(srcPath, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
