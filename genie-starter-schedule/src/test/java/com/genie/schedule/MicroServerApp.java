package com.genie.schedule;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        basePackages = {"com.genie.schedule"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)
)
public class MicroServerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MicroServerApp.class);
    }
}
