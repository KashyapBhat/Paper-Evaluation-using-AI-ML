package kashyapbhat.in.aep.Student.Model;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Kashyap Bhat on 16/02/19.
 */

public interface StudentAPI {
    @FormUrlEncoded
    @POST("/uploadanswerphone.php")
    void insertUser(
            @Field("usn") String usn,
            @Field("aq1") String q1,
            @Field("aq2") String q2,
            @Field("aq3") String q3,
            @Field("aq4") String q4,
            @Field("aq5") String q5,
            @Field("aq6") String q6,
            Callback<Response> callback);
}
