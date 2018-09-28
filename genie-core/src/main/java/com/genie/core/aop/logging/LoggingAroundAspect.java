package com.genie.core.aop.logging;

import com.genie.core.config.BaseConstants;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * 方法入口和出口日志
 */
public class LoggingAroundAspect implements MethodInterceptor {

    private Environment env;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public LoggingAroundAspect(Environment env) {
        this.env = env;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (env.acceptsProfiles(BaseConstants.SPRING_PROFILE_DEVELOPMENT)) {
            log.debug("Enter: {}.{}() with argument[s] = {}", methodInvocation.getThis().getClass().getName(),
                    methodInvocation.getMethod().getName(), Arrays.toString(methodInvocation.getArguments()));
        }
        try {
            Object result = methodInvocation.proceed();
            if (env.acceptsProfiles(BaseConstants.SPRING_PROFILE_DEVELOPMENT)) {
                log.debug("Exit: {}.{}() with result = {}", methodInvocation.getThis().getClass().getName(),
                        methodInvocation.getMethod().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(methodInvocation.getArguments()),
                    methodInvocation.getThis().getClass().getName(), methodInvocation.getMethod().getName());

            throw e;
        }
    }
}
