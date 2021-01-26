package mimly.booking._logging.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@LogMe(protocol = Protocol.HTTP)
public @interface LogGet {
}

