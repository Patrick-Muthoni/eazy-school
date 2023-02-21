package com.eazybytes.eazyschool.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* com.eazybytes.eazyschool..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString() + " method execution start");
        Instant startTime = Instant.now();
        Object obj = joinPoint.proceed();
        Instant endTime = Instant.now();
        long timeElapsed = Duration.between(startTime, endTime).toMillis();
        log.info("Time taken to execute %s is %d ms".formatted(joinPoint.getSignature().toString(), timeElapsed));
        log.info("%s method execution ends" .formatted(joinPoint.getSignature().toString()));
        return obj;
    }

    @AfterThrowing(value = "execution(* com.eazybytes.eazyschool..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("method %s failed. Error is %s " .formatted(joinPoint.getSignature().toString(), ex.getMessage()));
    }
}
