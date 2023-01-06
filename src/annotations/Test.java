package annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String[] dependsOnMethods() default {};
    Class<? extends Throwable> expected() default None.class;

    long timeout() default 0L;

    class None extends Throwable {
        private None() {}
    }
}
