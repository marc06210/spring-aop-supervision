package mgu.spring.supervision;

import mgu.spring.supervision.repository.History;
import mgu.spring.supervision.repository.HistoryRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class Supervisor implements EmbeddedValueResolverAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(Supervisor.class);
    private StringValueResolver resolver;
    final private Map<String, Long> delaysByKey = new HashMap<>();

    @Autowired
    private HistoryRepository repo;

    @Around("@annotation(supervised)")
    public Object supervise(ProceedingJoinPoint joinPoint, Supervised supervised) throws Throwable {
        long maxDuration = getDelay(supervised.maxDurationInMillis());
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;

            if (maxDuration != -1 && duration > maxDuration) {
                //Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
                //logger.warn("MGU >>> The '{}' process took too long: {} ms instead of {} ms", supervision.serviceName(), duration, maxDuration);
                LOGGER.error("MGU >>> The '{}' process took too long: {} ms instead of {} ms", supervised.serviceName(), duration, maxDuration);
            }
        }
    }

    private long getDelay(String key) {
        Long delay = delaysByKey.get(key);
        if (delay == null) {
            LOGGER.debug("updating cache for '{}'", key);
            delay = Long.parseLong(resolver.resolveStringValue(key));
            delaysByKey.put(key, delay);
        }
        return delay;
    }

    /**
     * We will supervise all public method from the com.afklm.tecc.supervision.service
     * package.
     */
    @Pointcut("execution(* mgu.spring.supervision.service.*.*(..))")
    private void selectPublicServiceMethods(){}

    @Pointcut("execution(* mgu.spring.supervision.service.*.*(..))")
    private void selectAllServiceMethods(){}

    /**
     * This method is triggered before each method matching the selectServicePublicMethods()
     * predicate.
     * @param joinPoint
     */
    @Before("selectPublicServiceMethods()")
    public void loggingServiceCall(JoinPoint joinPoint) {
        History history = new History();
        history.setMethodName(joinPoint.getSignature().toShortString());
        int updatedRows = repo.incrementInvocation(history.getMethodName());
        if (updatedRows==0) {
            // no row updated, so first time we are processing this method
            history.setInvocations(1L);
            repo.save(history);
        }
    }

    /**
     * This method is triggered after any method matching selectServicePublicMethods()
     * predicate that threw an exception.
     * @param joinPoint
     */
    @AfterThrowing("selectPublicServiceMethods()")
    public void loggingThrownExceptionServiceCall(JoinPoint joinPoint){
        int updatedRows = repo.incrementErrors(joinPoint.getSignature().toShortString());
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }
}
