//package mimly.booking._logging.annotation;
//
//import java.lang.annotation.*;
//
//enum Protocol {HTTP, WS_UNICAST, WS_BROADCAST}
//enum Method {GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH}
//
//@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//public @interface LogMe {
//
//    Protocol protocol() default Protocol.HTTP;
//    Method method() default Method.GET;
//    String comment() default "";
//}