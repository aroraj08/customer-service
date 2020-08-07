package com.rapidkart.customerservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Observable;

@Component
@Aspect
public class LoggingAroundAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.rapidkart.customerservice.controller.CustomerController.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        Throwable throwable = null;
        logger.info(String.valueOf(joinPoint.getArgs()));

        Object returnValue = null;

        Object clazz = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        LocalDateTime startDateTime = LocalDateTime.now();
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable t) {
            throwable = t;
        }
        LocalDateTime endDateTime = LocalDateTime.now();

        logger.info("{}.{} method execution started at : {}", clazz, methodName, startDateTime.toString());
        logger.info("{}.{} method execution finishing at: {} with duration of {}", clazz, methodName, endDateTime.toString(),
                endDateTime.minusNanos(startDateTime.getNano()).getNano());

        if (throwable != null) {
            throw throwable;
        }
        return returnValue;
    }
}
