package com.zjk.zjkpicturebackend.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解范围
@Retention(RetentionPolicy.RUNTIME)//注解生效时间
public @interface AuthCheck {

    /**
     * 所需角色，留空时仅要求登录。
     */
    String mustRole() default "";
}
