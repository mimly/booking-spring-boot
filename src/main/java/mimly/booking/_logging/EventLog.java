//package mimly.booking._logging;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import static mimly.booking.config.AddressBook.Topic.EVENT_LOG_TOPIC;
//
//@Component
//@Slf4j(topic = EVENT_LOG_TOPIC)
//public class EventLog {
//
//    @EventListener
//    public void onApplicationEvent(ApplicationEvent event) {
//        log.info("\033[1;36m" + event.getClass().getName() + "\033[0m");
//    }
//}