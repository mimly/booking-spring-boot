//package mimly.booking._logging;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//
//import static mimly.booking.config.AddressBook.Topic.HTTP_LOG_TOPIC;
//
//@Aspect
//@Component
//@Slf4j(topic = HTTP_LOG_TOPIC)
//public class HttpLog {
//
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
//    public void getMethods() {
//    }
//
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
//    public void postMethods() {
//    }
//
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
//    public void putMethods() {
//    }
//
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
//    public void deleteMethods() {
//    }
//
//    @Pointcut("getMethods() || postMethods() || putMethods() || deleteMethods()")
//    public void allMethods() {
//    }
//
//    @Before(value = "allMethods()")
//    public void logBeforeHttp(JoinPoint joinPoint) {
//        log.info(String.format("\033[2;93mIN>> %s\033[0m", Arrays.toString(joinPoint.getArgs())));
//    }
//
//    @AfterReturning(value = "allMethods()", returning = "object")
//    public void logAfterReturning(JoinPoint joinPoint, Object object) {
//        log.info(String.format("\033[2;92mOUT<< %s\033[0m", object));
//    }
//
//    @AfterThrowing(value = "allMethods()", throwing = "throwable")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
//        log.info(String.format("\033[2;91m!ERR! %s\033[0m", throwable));
//    }
//
//    private String getDetails(JoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
////        RequestMapping requestMapping = (RequestMapping) method.getAnnotation(GetMapping.class);
//        return null;
//    }
//}
