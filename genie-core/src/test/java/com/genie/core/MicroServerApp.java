package com.genie.core;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.net.UnknownHostException;


/**
 * 入口类文件
 */

@ComponentScan(
        basePackages = {"com.genie.core"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)
)
public class MicroServerApp {

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MicroServerApp.class);
    }
}
