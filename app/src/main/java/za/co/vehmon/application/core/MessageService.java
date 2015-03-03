package za.co.vehmon.application.core;

import retrofit.http.GET;
import retrofit.http.Path;
import za.co.vehmon.application.services.ConversationResponse;

/**
 * Created by Renaldo on 3/3/2015.
 */
public interface MessageService {

    @GET(Constants.Http.VEHMON_URL_MESSAGE_CREATECONV_FRAG)
    ConversationResponse CreateConversation(@Path("token")String token,@Path("conversationName") String conversationName,@Path("userNames") String userNames);
}
