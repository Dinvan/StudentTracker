package RetroFit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by ravib on 05/11/2017.
 */
public interface ApiInterface {

    @Headers({"Content-Type: application/json"})
    @POST
    Call<JsonElement> postData(@Url String remainingURL, @Body JsonObject jsonObject, @Header("session_key") String session_key, @Header("is_free") String version);

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map);

    @Multipart
    @POST
    Call<JsonElement> uploadImage(@Url String remainingURL, @Part MultipartBody.Part file, @Body ProgressRequestBody body, @Header("session_key") String session_key);

    @Multipart
    @POST
    Call<JsonElement> uploadImage(@Url String remainingURL, @Part("file\"; filename=\"test.png\" ") RequestBody file, @Part("type") RequestBody body1, @Part("message") RequestBody body2, @Part("title") RequestBody body3, @Header("session_key") String session_key);
  //  Call<JsonElement> uploadImage(@Url String remainingURL, @Part MultipartBody.Part file, @Header("session_key") String session_key);


}


