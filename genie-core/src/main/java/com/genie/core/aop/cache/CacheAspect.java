package com.genie.core.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aspect for logging execution of service and repository Spring components.
 * <p>
 * By default, it only runs with the "dev" profile.
 */
@Aspect
public class CacheAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Environment env;
    private final DiscoveryClient discoveryClient;

    public CacheAspect(Environment env, DiscoveryClient discoveryClient) {
        this.env = env;
        this.discoveryClient = discoveryClient;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("@annotation(org.springframework.cache.annotation.CachePut)" +
        " || @annotation(org.springframework.cache.annotation.CacheEvict)")
    public void cacheOptPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("cacheOptPointcut()")
    public Object syncCache(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();

            joinPoint.getArgs();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            String methodName = joinPoint.getSignature().getName();

            Class<?> classTarget = joinPoint.getTarget().getClass();
            Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, par);

            CachePut cachePut = objMethod.getAnnotation(CachePut.class);
            if (cachePut != null) {
                cachePut.key();

            }

            CacheEvict cacheEvict = objMethod.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                cacheEvict.key();

            }

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    @Value("spring.application.name")
    private String serviceId;
    @Value("server.port")
    private int port;

    /**
     * 得到相同服务的uri地址
     * @return
     */
    private List<URI> getOtherUris() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        return discoveryClient.getInstances(serviceId)
            .stream()
            .map(service -> service.getUri())
            .filter(serviceUri -> !serviceUri.getHost().equals(host)
                || serviceUri.getPort() != port)
            .collect(Collectors.toList());
    }
}
