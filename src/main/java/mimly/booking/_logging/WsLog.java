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
//import static mimly.booking.config.AddressBook.Topic.WS_LOG_TOPIC;
//
//@Aspect
//@Component
//@Slf4j(topic = WS_LOG_TOPIC)
//@Slf4j
//public class WsLog {
//
//    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
//    public void messageMapping() {
//    }
//
//    @Pointcut("execution(public void org.springframework.messaging.simp.SimpMessagingTemplate.convertAndSendToUser(..))")
//    public void convertAndSendToUser() {
//    }
//
//    @Pointcut("execution(public void org.springframework.messaging.simp.SimpMessagingTemplate.convertAndSend(..))")
//    public void convertAndSend() {
//    }
//
//    @Before(value = "messageMapping()")
//    public void logMessageMapping(JoinPoint joinPoint) {
//        log.info(String.format("\033[2;93mIN>> %s\033[0m", Arrays.toString(joinPoint.getArgs())));
//    }
//
//    @AfterThrowing(value = "messageMapping()", throwing = "throwable")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
//        log.info(String.format("\033[2;91m!ERR! %s\033[0m", throwable));
//    }
//
//    @Before(value = "convertAndSendToUser()")
//    public void logConvertAndSendToUser(JoinPoint joinPoint) {
//        log.info(String.format("\033[2;92mUNICAST<< %s\033[0m", Arrays.toString(joinPoint.getArgs())));
//    }
//
//    @Before(value = "convertAndSend()")
//    public void logConvertAndSend(JoinPoint joinPoint) {
//        log.info(String.format("\033[2;92mBROADCAST<< %s\033[0m", Arrays.toString(joinPoint.getArgs())));
//    }
//
//    private String getDetails(JoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        return null;
//    }
//}
