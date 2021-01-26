package mimly.booking.config;

public class AddressBook {

    public static class Topic {

        public static final String EVENT_LOG_TOPIC = "** EVENT **";
        public static final String HTTP_LOG_TOPIC = "** HTTP **";
        public static final String WS_LOG_TOPIC = "** WS **";
        public static final String ASSISTANT_CONTROLLER_TOPIC = "** ASSISTANT Controller **";
        public static final String ASSISTANT_SERVICE_TOPIC = "** ASSISTANT Service **";
        public static final String AUTHENTICATION_CONTROLLER_TOPIC = "** AUTHENTICATION Controller **";
        public static final String RESERVATION_CONTROLLER_TOPIC = "** RESERVATION Controller **";
        public static final String RESERVATION_SERVICE_TOPIC = "** RESERVATION Service **";
    }

    public static final String APIv1 = "/api/v1";
    public static final String APIv1_SECURED = "/api/secured/v1";

    public static class HttpEndpoint {

        public static final String ADD_TIME = "/times";
        public static final String REMOVE_TIME = "/times/{id}";

        public static final String IS_AUTHENTICATED = "/authentication";
    }

    public static final String WS_APIv1 = "/ws-api/v1";
    public static final String WS_APIv1_SECURED = "/ws-api/secured/v1";
    public static final String WS_UNICAST = "/queue" + WS_APIv1;
    public static final String WS_BROADCAST = "/topic" + WS_APIv1;

    public static class WebSocketEndpoint {

        public static final String ORIGIN = "origin";
        public static final String POST = "POST";
        public static final String DELETE = "DELETE";

        public static final String TIMES = "/times";
        public static final String MAKE_RESERVATION = "/make-reservation";
        public static final String CANCEL_RESERVATION = "/cancel-reservation";
        public static final String CONFIRM_RESERVATION = "/confirm-reservation";

        public static final String UNICAST_TIMES = WS_UNICAST + TIMES;
        public static final String BROADCAST_RESERVATIONS = WS_BROADCAST + "/reservations";
        public static final String UNICAST_RESERVATION_STARTED = WS_UNICAST + "/reservation-started";
        public static final String UNICAST_RESERVATION_ENDED = WS_UNICAST + "/reservation-ended";

        public static final String UNICAST_SESSION_EXPIRED = WS_UNICAST + "/session-expired";
    }
}
