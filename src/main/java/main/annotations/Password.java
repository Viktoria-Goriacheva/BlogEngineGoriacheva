package main.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

  String message() default "Пароль короче 6-ти символов";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}