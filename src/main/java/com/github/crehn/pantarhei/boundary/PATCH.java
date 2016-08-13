package com.github.crehn.pantarhei.boundary;

import java.lang.annotation.*;

import javax.ws.rs.HttpMethod;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {
}