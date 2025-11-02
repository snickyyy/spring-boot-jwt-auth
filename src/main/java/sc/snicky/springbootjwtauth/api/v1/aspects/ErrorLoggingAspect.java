package sc.snicky.springbootjwtauth.api.v1.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ErrorLoggingAspect {
    /**
     * Pointcut that matches all methods in the package
     * `sc.snicky.springbootjwtauth.api.v1.exceptions.handlers` and its subpackages.
     */
    @Pointcut("execution(* sc.snicky.springbootjwtauth.api.v1.exceptions.handlers..*(..))")
    public void errorHandlingLayer() { }

    /**
     * Logs error details before the execution of methods matched by the `errorHandlingLayer` pointcut.
     *
     * @param joinPoint the join point providing reflective access to the method being advised
     */
    @Before("errorHandlingLayer()")
    public void logBeforErrorHandlingMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length > 0 && args[0] instanceof Exception ex) {
            log.error("Exception handled in: {}.{}",
                    joinPoint.getTarget().getClass().getName(),
                    joinPoint.getSignature().getName(),
                    ex);
        }
    }
}
