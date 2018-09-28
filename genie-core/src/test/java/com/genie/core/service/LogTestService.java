package com.genie.core.service;

import org.springframework.stereotype.Service;

@Service
public class LogTestService {

    public void serviceProcess() {
        System.out.println("service Process");
    }

    public void exceptionProcess() throws Exception {
        throw new Exception("");
    }
}
