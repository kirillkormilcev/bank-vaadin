/*package dev.kormilcev.bank.util;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggingAspect {

  @Pointcut("""
      within(dev.kormilcev.bank.repository.impl..*)
      """)
  public void anyMethodsInDao() {}

  @AfterReturning(value = "anyMethodsInDao()", returning = "returning")
  public void logAfter (JoinPoint joinPoint, Object returning) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();

    StringBuilder logMessage = new StringBuilder(className).append(".").append(methodName);

    if (joinPoint.getArgs().length > 0) {
      logMessage.append("(args: ").append(Arrays.toString(joinPoint.getArgs())).append(")");
    }
    if (returning != null) {
      logMessage.append("(returning: ").append(returning).append(".");
    }

    log.debug(logMessage.toString());
  }
}*/
