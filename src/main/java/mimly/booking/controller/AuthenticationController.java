package mimly.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.session.web.socket.events.SessionConnectEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static mimly.booking.config.AddressBook.APIv1;
import static mimly.booking.config.AddressBook.HttpEndpoint.IS_AUTHENTICATED;
import static mimly.booking.config.AddressBook.Topic.AUTHENTICATION_CONTROLLER_TOPIC;
import static mimly.booking.config.AddressBook.WS_APIv1;
import static mimly.booking.config.AddressBook.WebSocketEndpoint.UNICAST_SESSION_EXPIRED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(APIv1)
@MessageMapping(WS_APIv1)
@Slf4j(topic = AUTHENTICATION_CONTROLLER_TOPIC)
public class AuthenticationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String, String> sessionSyncMap;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.sessionSyncMap = new ConcurrentHashMap<>();
    }

    @GetMapping(value = IS_AUTHENTICATED, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Principal> isAuthenticated(Principal principal) {
        return new ResponseEntity<>(principal, (principal == null)
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.OK
        );
    }

    @EventListener
    public void onSessionConnect(SessionConnectEvent event) {
        WebSocketSession webSocketSession = event.getWebSocketSession();
        String httpSessionId = (String) webSocketSession.getAttributes().get("HTTP.SESSION.ID");
        String wsSessionId = webSocketSession.getId();
        boolean authenticated = webSocketSession.getPrincipal() != null;
        this.sessionSyncMap.put(httpSessionId, wsSessionId);
        log.info(String.format("\033[2;103m Associated HTTP session: %s with WS session: %s \033[0m", httpSessionId, wsSessionId));
    }

    @EventListener
    public void onSessionExpired(SessionExpiredEvent event) {
        String httpSessionId = event.getSessionId();
        String wsSessionId = this.sessionSyncMap.get(httpSessionId);
        boolean sessionAuthenticated = event.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null;
        log.info(sessionAuthenticated
                ? String.format("\033[2;103m HTTP session: %s expired, associated with WS session: %s\033[2;102m AUTHENTICATED \033[0m", httpSessionId, wsSessionId)
                : String.format("\033[2;103m HTTP session: %s expired, associated with WS session: %s\033[2;101m UNAUTHENTICATED \033[0m", httpSessionId, wsSessionId)
        );
        if (wsSessionId != null)
            this.notifyClientOnSessionExpipred(wsSessionId, new ResponseEntity<>(sessionAuthenticated ? HttpStatus.UNAUTHORIZED : HttpStatus.OK));
        this.sessionSyncMap.remove(httpSessionId);
    }

    private void notifyClientOnSessionExpipred(String wsSessionId, ResponseEntity<String> payload) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
        simpMessageHeaderAccessor.setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, wsSessionId);
        this.simpMessagingTemplate.convertAndSendToUser(wsSessionId, UNICAST_SESSION_EXPIRED, payload, simpMessageHeaderAccessor.getMessageHeaders());
        log.info(String.format("Client with WS session: %s notified", wsSessionId));
    }
}
