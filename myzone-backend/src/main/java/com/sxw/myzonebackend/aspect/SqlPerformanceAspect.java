package com.sxw.myzonebackend.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * SQL性能监控切面
 */
@Aspect
@Component
public class SqlPerformanceAspect {

    /**
     * 监控Mapper方法的执行时间
     */
    @Around("execution(* com.sxw.myzonebackend.mapper.*.*(..))")
    public Object monitorSqlPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            // 如果执行时间超过1秒，记录警告日志
            if (stopWatch.getTotalTimeMillis() > 1000) {
                System.out.println("⚠️ 慢SQL警告: " + joinPoint.getSignature().getName() + 
                                 " 执行时间: " + stopWatch.getTotalTimeMillis() + "ms");
            } else {
                System.out.println("✅ SQL执行: " + joinPoint.getSignature().getName() + 
                                 " 执行时间: " + stopWatch.getTotalTimeMillis() + "ms");
            }
            
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            System.out.println("❌ SQL执行异常: " + joinPoint.getSignature().getName() + 
                             " 执行时间: " + stopWatch.getTotalTimeMillis() + "ms" +
                             " 异常: " + e.getMessage());
            throw e;
        }
    }
} 