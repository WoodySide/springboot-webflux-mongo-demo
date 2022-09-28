package com.woodyside.reactive.aspect.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogRepositoryAspect {

    @Pointcut(value = "within(@org.springframework.stereotype.Repository *)")
    public void repository(){}

    @Before(value = "repository()")
    public void beforeRepository(JoinPoint joinPoint) {
        log.info("Repository {} starts working with DB",
                joinPoint.getSignature().getDeclaringTypeName());
    }

    @AfterReturning(
            pointcut = "repository()",
            returning = "result"
    )
    public void afterRepository(JoinPoint joinPoint, Object result) {
        log.info("Method {} of the repository {} returned {}",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringTypeName(),
                result);
    }

    @Pointcut(value = "execution(public * org.springframework.data.mongodb.repository.ReactiveMongoRepository*.*(..))")
    public void crudRepository(){}

    @Before(value = "crudRepository()")
    public void beforeCrudRepository(JoinPoint joinPoint) {
        if(joinPoint.getSignature().getName().equals("insert")) {
            log.info("Repository starts saving following data in DB: {}", joinPoint.getArgs());
        } else {
            log.info("Repository starts executing the method {} ", joinPoint.getSignature().getName());
        }
    }

    @AfterReturning(
            pointcut = "crudRepository()",
            returning = "result"
    )
    public void afterCrudRepository(JoinPoint joinPoint, Object result) {
        if(joinPoint.getSignature().getName().equals("insert")) {
            log.info("Repository successfully saved following data in DB.");
        } else {
            log.info("Repository ends executing the method {} ", joinPoint.getSignature().getName());
        }
    }
}
