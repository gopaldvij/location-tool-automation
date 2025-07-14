package e2eTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Makes this annotation available at runtime
@Target(ElementType.METHOD) // This annotation can be applied to methods
public @interface SkipSetup {

}
