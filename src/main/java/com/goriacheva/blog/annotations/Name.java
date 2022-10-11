package com.goriacheva.blog.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

  String message() default "Имя указано неверно";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}