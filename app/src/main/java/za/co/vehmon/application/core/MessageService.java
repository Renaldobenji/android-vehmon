package za.co.vehmon.application.core;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import za.co.vehmon.application.services.ConversationResponse;
import za.co.vehmon.application.services.MessageResponse;

/**
 * Created by Renaldo on 3/3/2015.
 */
public interface MessageService {

    @POST(Constants.Http.VEHMON_URL_MESSAGE_CREATECONV_FRAG)
    ConversationResponse CreateConversation(@Path("token")String token,@Path("conversationName") String conversationName,@Path("userNames") String userNames);

    @POST(Constants.Http.VEHMON_URL_MESSAGE_SENDMSG_FRAG)
    MessageResponse SendMessage(@Path("token") String token,@Path("conversationId") String conversationId,@Path("dateSent") String dateSent,@Path("message") String message);

    @GET(Constants.Http.VEHMON_URL_MESSAGE_CONVUNREADMSG_FRAG)
    List<MessageResponse> GetAllUnreadMessagesForConversation(@Path("token")String token,@Path("conversationId") Integer conversationId);

    @GET(Constants.Http.VEHMON_URL_MESSAGE_UNREADMSG_FRAG)
    List<MessageResponse> GetAllUnreadMessages(@Path("token")String token);

}
