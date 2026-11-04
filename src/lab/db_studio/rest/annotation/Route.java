package lab.db_studio.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lab.db_studio.rest.HttpMethods;

@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String path();
    HttpMethods method() default HttpMethods.GET;
}
