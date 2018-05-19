package RetroFit;

import com.ibsglobal.utility.Config;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ravib on 05/11/2017.
 */
public class ApiClient {

    private static Retrofit posting_APIClient = null;
    public static Retrofit getClient() {
        if (posting_APIClient == null) {
            posting_APIClient = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build();
        }
        return posting_APIClient;
    }

    public static OkHttpClient getHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(45, TimeUnit.SECONDS)

                .build();
        return okHttpClient;
    }
}
