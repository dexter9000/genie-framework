package com.genie.core.config;


import com.genie.core.aop.logging.LoggingAfterThrowingAspect;
import com.genie.core.aop.logging.LoggingAroundAspect;
import com.genie.core.properties.GenieProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

/**
 *
 *
 * genie:
 *     class-names:
 *     -   "com.genie.crm.repository..*"
 *     -   "com.genie.crm.service..*"
 *     -   "com.genie.common.web.rest..*"
 *     -   "com.genie.crm.web.rest..*"
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(GenieProperties.class)
public class LoggingAspectConfiguration {

    private final Logger log = LoggerFactory.getLogger(LoggingAspectConfiguration.class);

    @Autowired
    private GenieProperties properties;

    public LoggingAspectConfiguration() {
        log.info("Logging Aspect Configuration Init...");
    }


    @Bean
    @ConditionalOnProperty("genie.logging-aspect.patterns")
    @ConditionalOnExpression("'${genie.logging-aspect.patterns}'.length() > 0")
    public RegexpMethodPointcutAdvisor logAfterThrowing(Environment env) {
        String[] classNames = properties.getLoggingAspect().getPatterns();
        log.info("Regexp log after throwing...");
        LoggingAfterThrowingAspect loggingAfterThrowingAspect = new LoggingAfterThrowingAspect(env);
        RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor = new RegexpMethodPointcutAdvisor();
        regexpMethodPointcutAdvisor.setPatterns(classNames);
        regexpMethodPointcutAdvisor.setAdvice(loggingAfterThrowingAspect);
        return regexpMethodPointcutAdvisor;
    }

    @Bean
    @ConditionalOnProperty("genie.logging-aspect.patterns")
    @ConditionalOnExpression("'${genie.logging-aspect.patterns}'.length() > 0")
    public RegexpMethodPointcutAdvisor logAround(Environment env) {
        String[] classNames = properties.getLoggingAspect().getPatterns();
        log.info("Regexp log around method for [\n{}]", StringUtils.join(classNames, ",\n\t"));
        LoggingAroundAspect loggingAroundAspect = new LoggingAroundAspect(env);
        RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor = new RegexpMethodPointcutAdvisor();
        regexpMethodPointcutAdvisor.setPatterns(classNames);
        regexpMethodPointcutAdvisor.setAdvice(loggingAroundAspect);
        return regexpMethodPointcutAdvisor;
    }

}
