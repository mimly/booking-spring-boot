package mimly.booking.controller;

import lombok.extern.slf4j.Slf4j;
import mimly.booking.model.dto.PrincipalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.session.events.AbstractSessionEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.session.web.socket.events.SessionConnectEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static mimly.booking.config.AddressBook.APIv1;
import static mimly.booking.config.AddressBook.HttpEndpoint.IS_AUTHENTICATED;
import static mimly.booking.config.AddressBook.WS_APIv1;
import static mimly.booking.config.AddressBook.WebSocketEndpoint.UNICAST_SESSION_EXPIRED;
import static mimly.booking.config.HazelcastHttpSessionConfig.MAX_INACTIVE_INTERVAL_IN_SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(APIv1)
@MessageMapping(WS_APIv1)
@Slf4j
public class AuthenticationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String, Set<String>> synchronizedSessions;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.synchronizedSessions = new ConcurrentHashMap<>();
    }

    @GetMapping(value = IS_AUTHENTICATED, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PrincipalDTO> isAuthenticated(HttpSession httpSession, Principal principal) {
        PrincipalDTO principalDTO = new PrincipalDTO(principal);
        httpSession.setMaxInactiveInterval(principalDTO.isAuthenticated() ? 5 * 60 : MAX_INACTIVE_INTERVAL_IN_SECONDS);
        if (!principalDTO.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(principalDTO, HttpStatus.OK);
    }

    @EventListener
    public void onAbstractSessionEvent(AbstractSessionEvent event) {
        log.debug(String.format("\033[1;48;5;52m %s: %s \033[0m", event.getClass().getSimpleName(), event.getSessionId()));
    }

    @EventListener
    public void onSessionConnect(SessionConnectEvent event) {
        WebSocketSession webSocketSession = event.getWebSocketSession();
        String httpSessionId = (String) webSocketSession.getAttributes().get("HTTP.SESSION.ID");
        String wsSessionId = webSocketSession.getId();
        boolean authenticated = webSocketSession.getPrincipal() != null;

        this.synchronizedSessions.putIfAbsent(httpSessionId, new HashSet<>());
        this.synchronizedSessions.get(httpSessionId).add(wsSessionId);
        log.debug(String.format("\033[2;103m Associated HTTP session: %s with WS session: %s \033[0m", httpSessionId, wsSessionId));
    }

    @EventListener
    public void onSessionExpired(SessionExpiredEvent event) {
        String httpSessionId = event.getSessionId();
        Set<String> wsSessionIds = this.synchronizedSessions.get(httpSessionId);
        boolean sessionAuthenticated = event.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null;
        log.debug(sessionAuthenticated
                ? String.format("\033[2;103m HTTP session: %s expired, associated with WS session: %s\033[2;102m AUTHENTICATED \033[0m", httpSessionId, wsSessionIds)
                : String.format("\033[2;103m HTTP session: %s expired, associated with WS session: %s\033[2;101m UNAUTHENTICATED \033[0m", httpSessionId, wsSessionIds)
        );

        /**
         * Notify all connected (and authenticated) clients on session expired.
         */
        if (wsSessionIds != null && sessionAuthenticated) {
            wsSessionIds.stream().filter(Objects::nonNull).forEach(wsSessionId -> {
                SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
                simpMessageHeaderAccessor.setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, wsSessionId);
                this.simpMessagingTemplate.convertAndSendToUser(wsSessionId, UNICAST_SESSION_EXPIRED, new ResponseEntity<>(HttpStatus.UNAUTHORIZED), simpMessageHeaderAccessor.getMessageHeaders());
                log.debug(String.format("Client with WS session: %s notified", wsSessionId));
            });
        }

        this.synchronizedSessions.remove(httpSessionId);
    }
}