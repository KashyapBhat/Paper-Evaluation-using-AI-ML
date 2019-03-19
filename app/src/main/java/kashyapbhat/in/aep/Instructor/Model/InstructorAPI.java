package kashyapbhat.in.aep.Instructor.Model;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Kashyap Bhat on 16/02/19.
 */

public interface InstructorAPI {
    @FormUrlEncoded
    @POST("/uploadphone.php")
    public void insertUser(
            @Field("quesnop") int name,
            @Field("quesp") String vquestion,
            @Field("keywp") String vkeywords,
            @Field("tmarksp") int vtotalmarks,
            @Field("textp") String vtext,
            Callback<Response> callback);
}
