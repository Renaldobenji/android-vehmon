

package za.co.vehmon.application.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    public static final class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "za.co.vehmon.application";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "vehmon";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "za.co.vehmon.application.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}


        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";
        public static final String VEHMON_URL_BASE = "http://vehmonmachine.cloudapp.net";
        //public static final String VEHMON_URL_BASE = "https://santech-ws-ppe.sanlam.co.za/skymobile";


        /**
         * Authentication URL
         */
        public static final String URL_AUTH_FRAG = "/1/login";
        public static final String URL_AUTH = URL_BASE + URL_AUTH_FRAG;

        public static final String VEHMON_URL_AUTH_FRAG = "/services/Authentication.svc/GetTokenForUser/{userName}/{password}";
        public static final String VEHMON_URL_AUTH_SETDEVICEID = "/services/Authentication.svc/SetDeviceId/{token}/{deviceID}";
        public static final String VEHMON_URL_AUTH_RENEW_FRAG = "/services/Authentication.svc/RenewToken/{token}";
        public static final String VEHMON_URL_AUTH_GETALLUSERS_FRAG = "/services/Authentication.svc/GetAllUsers/{token}";
        public static final String VEHMON_URL_AUTH_LEAVE_FRAG = "/services/LeaveService.svc/RequestLeave/{token}/{startTime}/{endTime}/{leaveRequestType}";
        public static final String VEHMON_URL_AUTH_GETALLLEAVE_FRAG = "/services/LeaveService.svc/GetAllLeaveRequests/{token}";
        public static final String VEHMON_URL_AUTH_STARTSHIFT_FRAG = "/services/TimeTrackingService.svc/StartShift/{token}/{clockInLat}/{clockOutLat}/{startTime}";
        public static final String VEHMON_URL_AUTH_ENDSHIFT_FRAG = "/services/TimeTrackingService.svc/EndShift/{userToken}/{shiftId}";
        public static final String VEHMON_URL_AUTH_LOGGPS_FRAG = "/services/TimeTrackingService.svc/LogCoordinatesToShift/{token}/{shiftId}/{csvCoords}";
        public static final String VEHMON_URL_MESSAGE_CREATECONV_FRAG = "/services/MessageService.svc/CreateConversation/{token}/{conversationName}/{userNames}";
        public static final String VEHMON_URL_MESSAGE_SENDMSG_FRAG = "/services/MessageService.svc/SendMessage/{token}/{conversationId}/{dateSent}/{message}";
        public static final String VEHMON_URL_MESSAGE_CONVUNREADMSG_FRAG = "/services/MessageService.svc/GetAllUnreadMessagesForConversation/{token}/{conversationId}";
        public static final String VEHMON_URL_MESSAGE_UNREADMSG_FRAG = "/services/MessageService.svc/GetAllUnreadMessages/{token}";

        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG =  "/1/users";
        public static final String URL_USERS = URL_BASE + URL_USERS_FRAG;


        /**
         * List News URL
         */
        public static final String URL_NEWS_FRAG = "/1/classes/News";
        public static final String URL_NEWS = URL_BASE + URL_NEWS_FRAG;


        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS_FRAG = "/1/classes/Locations";
        public static final String URL_CHECKINS = URL_BASE + URL_CHECKINS_FRAG;

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";


    }


    public static final class Extra {
        private Extra() {}

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";

    }

    public static final class VehmonSharedPrefs
    {
        public static final String name = "VehmonSharedPrefs";
    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "za.co.vehmon.application.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
        public static final int GPS_NOTIFICATION_ID = 1001; // Why 1000? Why not? :)
        public static final int SYNC_NOTIFICATION_ID = 1002; // Why 1000? Why not? :)
    }

}


