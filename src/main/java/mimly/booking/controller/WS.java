//package mimly.booking.controller;
//
//import mimly.booking.config.AddressBook;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class WS {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    public WS(SimpMessagingTemplate simpMessagingTemplate) {
//        this.simpMessagingTemplate = simpMessagingTemplate;
//    }
//
//    public void unicastMessage(String user, String destination, Object payload, AddressBook.WebSocketEndpoint.ReservationHeader reservationHeader) {
//        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
//        accessor.addNativeHeader("method", reservationHeader.toString());
//        accessor.setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, user);
//        this.simpMessagingTemplate.convertAndSendToUser(user, destination, payload, accessor.getMessageHeaders());
//    }
//
//    public void broadcastMessage(Object payload, AddressBook.WebSocketEndpoint.ReservationHeader reservationHeader) {
//        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
//        accessor.addNativeHeader("method", reservationHeader.toString());
//        this.simpMessagingTemplate.convertAndSend(destination, payload);
//    }
//}
