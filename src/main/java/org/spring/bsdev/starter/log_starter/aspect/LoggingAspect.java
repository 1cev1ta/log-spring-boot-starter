package org.spring.bsdev.starter.log_starter.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.event.Level;
import org.spring.bsdev.starter.log_starter.props.LoggingProperties;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    private final LoggingProperties props;

    @Before("@annotation(org.spring.bsdev.starter.log_starter.aspect.annotation.BeforeLog)")
    public void beforeAdvice(JoinPoint joinPoint) {
        logAtLevel(props.getLevel(), "Был вызван метод: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "@annotation(org.spring.bsdev.starter.log_starter.aspect.annotation.AfterReturningLog)",
            returning = "result"
    )
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        logAtLevel(props.getLevel(), "Был вызван метод {}, с результатом работы: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(
            pointcut = "@annotation(org.spring.bsdev.starter.log_starter.aspect.annotation.AfterThrowingLog)",
            throwing = "exception"
    )
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        logAtLevel(props.getLevel(), "Произошло исключение {} в методе: {}", exception.getClass(), joinPoint.getSignature().getName());
    }

    @Around("@annotation(org.spring.bsdev.starter.log_starter.aspect.annotation.AroundLog)")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        logAtLevel(props.getLevel(), "Время выполнения: {}", end - start + " миллисекунд(ы)");
        return result;
    }

    private void logAtLevel(Level level, String fmt, Object... args) {
        switch (level) {
            case ERROR -> log.error(fmt, args);
            case WARN  -> log.warn(fmt, args);
            case INFO  -> log.info(fmt, args);
            case DEBUG -> log.debug(fmt, args);
            case TRACE -> log.trace(fmt, args);
            default   -> log.info(fmt, args);
        }
    }
}

