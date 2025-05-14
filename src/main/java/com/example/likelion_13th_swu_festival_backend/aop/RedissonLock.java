package com.example.likelion_13th_swu_festival_backend.aop;

import java.lang.annotation.*;

// 분산락의 사용여부 또는 부가 설정을 할 수 있는 어노테이션
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    String value(); // Lock의 이름 (고유값)
    long waitTime() default 5000L; // Lock획득을 시도하는 최대 시간 (ms)
    long leaseTime() default 2000L; // 락을 획득한 후, 점유하는 최대 시간 (ms)

}