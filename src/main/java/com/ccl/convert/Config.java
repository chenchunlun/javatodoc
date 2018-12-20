package com.ccl.convert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Configuration

public class Config {

    @Bean
    public String srcPath(@Value("${convert.src.path}") String srcPath) {
        return srcPath;
    }

    @Bean
    public String docPath(@Value("${convert.doc.path}") String docPath) {
        return docPath;
    }

    @Bean
    public List<File> fileList() {
        return new ArrayList<>();
    }
}
