package com.genie.core.aop.logging;

import com.genie.core.config.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

/**
 * Aspect for logging execution of service and repository Spring components.
 * <p>
 */
public class LoggingAfterThrowingAspect implements ThrowsAdvice {

    private Environment env;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public LoggingAfterThrowingAspect(Environment env) {
        this.env = env;
    }

    public void afterThrowing(Method method, Object[] objects, Object target, Throwable e) {
        if (env.acceptsProfiles(BaseConstants.SPRING_PROFILE_DEVELOPMENT)) {
            log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", target.getClass(),
                method.getName(), e.getCause() != null ? e.getCause() : "No Cause", e.getMessage(), e);

        } else {
            log.error("Exception in {}.{}() with cause = {}", target.getClass(),
                method.getName(), e.getCause() != null ? e.getCause() : "No Cause");
        }
    }
}
