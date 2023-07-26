package org.secwallet.payment.service;

import org.secwallet.core.NoRepeatSubmit;
import org.secwallet.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class RepeatSubmitAspect {

    public static final String  KEYPREX="noRpeat:user:";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * Perform interface anti-duplication operation processing
     * @param pjp
     * @param noRepeatSubmit
     * @return
     */
    @Around("execution(* org.secwallet.payment.api.*.*(..)) && @annotation(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        try {
            //get request
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //get token request address
            StringBuilder sb = new StringBuilder();
            sb.append(KEYPREX).append(request.getHeader("Authorization").toString()).append(request.getRequestURI().toString());
            //get now time
            long now = System.currentTimeMillis();
            if (redisTemplate.hasKey(sb.toString())){
                //lastTime
                long lastTime= Long.valueOf(redisTemplate.opsForValue().get(sb.toString()).toString()) ;
                // If the time from the last submission is less than the set default time, it will be judged as repeated submission, otherwise it will be submitted normally -> enter business processing
                if ((now - lastTime)>noRepeatSubmit.lockTime()){
                    //Re-recording time 10 minutes expiration time
                    redisTemplate.opsForValue().set(sb.toString(),String.valueOf(now),5, TimeUnit.MINUTES);
                    //handle business
                    Object result =  pjp.proceed();
                    return result;
                }else {
                    return Result.fail("Clicking too fast, please slow down!");
                }
            }else {
                //first operation
                redisTemplate.opsForValue().set(sb.toString(),String.valueOf(now),5, TimeUnit.MINUTES);
                Object result =  pjp.proceed();
                return result;
            }
        }catch (Throwable e){
            log.error("Exception when verifying form is submitted repeatedly: {}", e.getMessage());
            return Result.fail("Exception when verifying duplicate submissions");
        }
    }
}
