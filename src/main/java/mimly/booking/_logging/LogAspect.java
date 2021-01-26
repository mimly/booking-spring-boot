//package mimly.booking._logging;
//
//import lombok.Value;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.messaging.simp.SimpLogging;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//
//import static mimly.booking.config.AddressBook.Topic.LOG_TOPIC;
//
//@Aspect
//@Component
//@Slf4j(topic = LOG_TOPIC)
//public class LogAspect {
//
//    @Value
//    private static class LogMessage {
//
//        private enum Protocol {HTTP, WS_UNICAST, WS_BROADCAST}
//
//        //        private enum HttpMethod {GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH}
//        private enum Descriptor {IN, OUT, ERR}
//
//        Protocol protocol;
//        //        HttpMethod httpMethod;
//        Descriptor descriptor;
//
//        private int getTopicColor() {
//            switch (this.descriptor) {
//                case IN:
//                    return 93;
//                case OUT:
//                    return 92;
//                case ERR:
//                    return 91;
//                default:
//                    return 90;
//            }
//        }
//
//        private String getDirectionArrows() {
//            switch (this.descriptor) {
//                case IN:
//                    return ">>IN>>";
//                case OUT:
//                    return "<<OUT<<";
//                case ERR:
//                    return "!!ERR !!";
//                default:
//                    return "";
//            }
//        }
//
//        public Logger getLoggerInstance() {
//
////            StringBuilder topic = new StringBuilder()
////                    .append(String.format("\033[2;%dm %s ", getTopicColor(), getDirectionArrows()))
////                    .append(String.format("\033[2;90dm %s \033[2;%dm", getProtocol().name(), getTopicColor()))
////                    .append("\033[0m");
//
//            return LoggerFactory.getLogger(getDirectionArrows() + " " + getProtocol().name());
//        }
//
//        public String prepareMessage(String message) {
//            return String.format("\033[2;%dm%s\033[0m", getTopicColor(), message);
//        }
//    }
//
////    private static class LogPointcut {
//
////        @Pointcut("@target(org.springframework.stereotype.Controller)")
////        public void controllerMethods() {
////        }
////
////        @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
////        public void httpMethods() {
////        }
//
////        @Pointcut("controllerMethods() && httpMethods()")
////        public void httpHandlers() {
////        }
//
//        @Pointcut("execution(public void javax.servlet.http.HttpServlet.service(..))")
//        public void httpHandlers() {
//        }
//
//        @Pointcut("execution(public void org.springframework.messaging.simp.SimpMessagingTemplate.convertAndSendToUser(..))")
//        public void wsUnicastHandlers() {
//        }
//
//        @Pointcut("execution(public void org.springframework.messaging.simp.SimpMessagingTemplate.convertAndSend(..))")
//        public void wsBroadcastHandlers() {
//        }
//
//        @Pointcut("wsUnicastHandlers() || wsBroadcastHandlers()")
//        public void wsHandlers() {
//        }
//
//        @Pointcut("httpHandlers() || wsHandlers()")
//        public void allHandlers() {
//        }
////    }
//
//    @Before(value = "httpHandlers()")
//    public void logBeforeHttp(JoinPoint joinPoint) {
//        LogMessage logMessage = new LogMessage(LogMessage.Protocol.HTTP, LogMessage.Descriptor.IN);
//        Logger log = logMessage.getLoggerInstance();
//        log.info(logMessage.prepareMessage(Arrays.toString(joinPoint.getArgs())));
//    }
//
//    @Before(value = "wsUnicastHandlers()")
//    public void logBefore(JoinPoint joinPoint) {
//        LogMessage logMessage = new LogMessage(LogMessage.Protocol.WS_UNICAST, LogMessage.Descriptor.IN);
//        Logger log = logMessage.getLoggerInstance();
//        log.info(logMessage.prepareMessage(Arrays.toString(joinPoint.getArgs())));
//    }
//
//    @AfterReturning(value = "wsUnicastHandlers()", returning = "object")
//    public void logAfterReturning(JoinPoint joinPoint, Object object) {
//        LogMessage logMessage = new LogMessage(LogMessage.Protocol.WS_UNICAST, LogMessage.Descriptor.OUT);
//        Logger log = logMessage.getLoggerInstance();
//        log.info(logMessage.prepareMessage(String.valueOf(object)));
//    }
//
//    @AfterThrowing(value = "wsUnicastHandlers()", throwing = "throwable")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
//        LogMessage logMessage = new LogMessage(LogMessage.Protocol.WS_UNICAST, LogMessage.Descriptor.ERR);
//        Logger log = logMessage.getLoggerInstance();
//        log.info(logMessage.prepareMessage(String.valueOf(throwable)));
//    }
//}
