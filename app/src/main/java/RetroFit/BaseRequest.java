package RetroFit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rayn.R;
import com.rayn.auth.SplashActivity;
import com.rayn.interfaces.OnItemClickInAdapter;
import com.rayn.session.SessionParam;
import com.rayn.utility.Dialogs;
import com.rayn.utility.FileCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ravib on 05/11/2017.
 */


public class BaseRequest<T> extends BaseRequestParser {
    private Context mContext;
    private ApiInterface apiInterface;
    private RequestReceiver requestReciever;
    private boolean runInBackground = false;
    private Dialog dialog;
    private View loaderView = null;
    private int RequestCode = 1;
    Fragment fragment;

    private boolean cacheEnabled = false;
    private boolean isAlreadyTaken = false;
    private boolean duringCacheLoader = false;
    String fileName = null;

    public int mResponseCode;

    public boolean isDuringCacheLoader() {
        return duringCacheLoader;
    }

    public void setDuringCacheLoader(boolean duringCacheLoader) {
        this.duringCacheLoader = duringCacheLoader;
    }

    public boolean isRunInBackground() {
        return runInBackground;
    }

    public void setRunInBackground(boolean runInBackground) {
        this.runInBackground = runInBackground;
    }

    public BaseRequest(Context context) {
        mContext = context;


        apiInterface =
                ApiClient.getClient().create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    /*public void setDefaultLoader() {
        dialog = getProgressesDialogDefault(mContext);


    }*/

    public boolean isAlreadyTaken() {
        return isAlreadyTaken;
    }

    public void setAlreadyTaken(boolean alreadyTaken) {
        isAlreadyTaken = alreadyTaken;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public BaseRequest(Context context, Fragment fragment) {
        mContext = context;
        this.fragment = fragment;
        apiInterface =
                ApiClient.getClient().create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public void setBaseRequestListner(RequestReceiver requestListner) {
        this.requestReciever = requestListner;

    }

    public void setLoaderView(View loaderView_) {
        this.loaderView = loaderView_;

    }

    public ArrayList<Object> getDataList(JSONArray mainArray, Class<T> t) {
        Gson gsm = null;
        ArrayList<Object> list = null;
        list = new ArrayList<>();
        if (null != mainArray) {

            for (int i = 0; i < mainArray.length(); i++) {
                gsm = new Gson();
                Object object = gsm.fromJson(mainArray.optJSONObject(i).toString(), t);
                list.add(object);
            }
        }
        return list;
    }

    public ArrayList<Object> getList(JSONArray mainArray, Class<T> t) {
        Gson gsm = null;
        ArrayList<Object> list = null;
        list = new ArrayList<>();
        if (null != mainArray) {

            for (int i = 0; i < mainArray.length(); i++) {
                gsm = new Gson();
                Object object = gsm.fromJson(mainArray.optJSONObject(i).toString(), t);
                list.add(object);
            }
        }
        return list;
    }

    public Callback<JsonElement> responseCallback = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            mResponseCode = response.code();
            hideLoader();
            if (null != response.body()) {
                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                }

            } else if (response.errorBody() != null) {
                responseServer = readStreamFully(response.errorBody().contentLength(),
                        response.errorBody().byteStream());
            }
            logFullResponse(responseServer, "OUTPUT + CODE "+mResponseCode);
            parseJson(responseServer);
            String logoutError = "You are not a valid user";
            try {
                JSONObject data = new JSONObject(responseServer);
                String responseMesage = data.optString("message");
                if(responseMesage.equalsIgnoreCase(logoutError)){
                    SessionParam.deletePrefrenceData(mContext);
                    Toast.makeText(mContext,"Your login session is expired, Please login",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(mContext,SplashActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mResponseCode == 200) {
                new FileCacheBGTask(responseServer).execute();
                if (null != requestReciever && !((Activity) mContext).isDestroyed()) {
                    if (fragment != null) {
                        if (fragment.isAdded()) {
                            if (null != getDataArray()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                            } else if (null != getDataObject()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                            } else {
                                requestReciever.onSuccess(RequestCode, responseServer, message);
                            }
                        }
                    } else {
                        if (null != getDataArray()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                        } else if (null != getDataObject()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                        } else {
                            requestReciever.onSuccess(RequestCode, responseServer, message);
                        }

                    }

                }

            } else {
                if (null != requestReciever) {
                    requestReciever.onFailure(mResponseCode, "" + responseServer, message);
                }

            }

        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {
            handler.removeCallbacksAndMessages(null);
            // First condition is for connection problem
            if (!TextUtils.isEmpty(t.getMessage()) && t.getMessage().startsWith("Unable to resolve") || t.getMessage().startsWith("Failed" +
                    " to connect")) {
                handler.postDelayed(r, 1000);
            } else {
                hideLoader();
                requestReciever.onFailure(1, "" + mResponseCode, message);
            }

        }
    };

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            hideLoader();
            if (null != requestReciever) {
                requestReciever.onNetworkFailure(RequestCode, mContext.getString(R.string.MSG_INTERNETERROR));

                Dialogs
                        .showOkCancelDialog(
                                mContext,
                                mContext.getString(R.string.MSG_INTERNETERROR),
                                new OnItemClickInAdapter() {
                                    @Override
                                    public void onClickItems(int clickID, int position, Object obje) {
                                        showLoader();
                                        callAPI.clone().enqueue(responseCallback);

                                    }
                                });
            }
        }
    };
    Call<JsonElement> callAPI;

    public void callAPIPost(final int requestCode, JsonObject jsonObject, String remainingURL) {
        isAlreadyTaken = false;
        RequestCode = requestCode;
        showLoader();

        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        Log.d("BaseReq", "Input URL : " + ApiClient.getClient().baseUrl() + remainingURL);
        logFullResponse(jsonObject.toString(), "INPUT");
        String sess = SessionParam.getSessionKey(mContext);
        fileName = cacheEnabled ? (remainingURL
                + sess + jsonObject.toString()).hashCode()
                + ".req" : null;


        new FileReadCacheBGTask().execute();
        callAPI = apiInterface.postData(remainingURL, jsonObject, sess, "");
        callAPI.enqueue(responseCallback);
    }

    public void callAPIPost(final int requestCode, JsonObject jsonObject, String remainingURL, String sessionKey) {

        RequestCode = requestCode;
        showLoader();

        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("BaseReq", "Input URL : " + remainingURL);
        logFullResponse(jsonObject.toString(), "INPUT");
        callAPI = apiInterface.postData(remainingURL, jsonObject, sessionKey, version);

        callAPI.enqueue(responseCallback);
    }

   // public void callAPIPostImage(final int requestCode, MultipartBody.Part image, String remainingURL,MultipartBody.Part input) {
   public void callAPIPostImage(final int requestCode, RequestBody image, String remainingURL, RequestBody input1, RequestBody input2, RequestBody input3, RequestBody input4,RequestBody input5) {
        RequestCode = requestCode;
        showLoader();
        Log.d("BaseReq", "Input URL : " + remainingURL);
        String sess = SessionParam.getSessionKey(mContext);
        callAPI = apiInterface.uploadImage(remainingURL, image,input1,input2,input3,input4,input5, sess);
        callAPI.enqueue(responseCallback);
    }


    public void callAPIGET(final int requestCode, Map<String, String> map, String remainingURL) {
        RequestCode = requestCode;

        showLoader();
        String baseURL = ApiClient.getClient().baseUrl().toString() + remainingURL;
        if (!baseURL.endsWith("?")) {
            baseURL = baseURL + "?";
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            baseURL = baseURL + entry.getKey() + "=" + entry.getValue() + "&";
        }
        System.out.println("BaseReq INPUT URL : " + baseURL);
        callAPI = apiInterface.postDataGET(remainingURL, map);
        callAPI.enqueue(responseCallback);
    }

    public void logFullResponse(String response, String inout) {
        final int chunkSize = 2000;

        if (null != response && response.length() > chunkSize) {
            int chunks = (int) Math.ceil((double) response.length()
                    / (double) chunkSize);

            for (int i = 1; i <= chunks; i++) {
                if (i != chunks) {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize, i
                                    * chunkSize));
                } else {
                    Log.i("BaseReq",
                            inout + " : "
                                    + response.substring((i - 1) * chunkSize,
                                    response.length()));
                }
            }
        } else {

            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("BaseReq", inout + " : " + jsonObject.toString(jsonObject.length()));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("BaseReq", " logFullResponse=>  " + response);
            }

        }
    }

    private String readStreamFully(long len, InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public Dialog getProgressesDialog(Context ct) {
        Dialog dialog = new Dialog(ct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_loader);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        return dialog;
    }

    public Dialog getProgressesDialogDefault(Context ct) {
        Dialog dialog = new Dialog(ct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.progress_dialog_loader);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void showLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.VISIBLE);
                } else if (null != dialog) {

                    dialog.show();
                }
            }
        }
    }

    public void hideLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.GONE);
                } else if (null != dialog) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        }
    }

    public int TYPE_NOT_CONNECTED = 0;
    public int TYPE_WIFI = 1;
    public int TYPE_MOBILE = 2;

/*    public int getConnectivityStatus(Context context) {
        if (null == context) {
            return TYPE_NOT_CONNECTED;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnected()) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;

    }*/

    String CHARSET = "UTF-8";
    private static final String BOUNDARY = "===" + System.currentTimeMillis()
            + "===";
    private static final String LINE_FEED = "\r\n";

    private static final String ACCEPT = "application/json";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USER_AGENT = "";
    private HttpURLConnection mHttpURLConnection;

    public void uploadImage(final String finalUrl, final File file, final String key) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MultipartConn multipartConn = new MultipartConn(
                            finalUrl);

                    multipartConn.addFilePart(key, file);

                    mHttpURLConnection = multipartConn.prepareConnection();

                    String response = readStreamFully(
                            mHttpURLConnection.getContentLength(),
                            mHttpURLConnection.getInputStream());

                    requestReciever.onSuccess(RequestCode, response, null);

                } catch (IOException e) {
                    e.printStackTrace();
                    requestReciever.onNetworkFailure(1, BaseRequestParser.newtWorkMessage);

                }
            }
        };
        new Thread(runnable).start();

    }

    public class MultipartConn {
        private HttpURLConnection httpURLConnection;
        private OutputStream outputStream;
        private PrintWriter printWriter;

        /**
         * This constructor initializes a new HTTP POST request with content
         * type is set to multipart/form-data
         *
         * @param requestURL
         */
        public MultipartConn(String requestURL) throws IOException {
            URL url = new URL(requestURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true); // indicates POST method
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
//
//            httpURLConnection.setRequestProperty("User-Agent",
//                    HttpConnector.USER_AGENT);

            outputStream = httpURLConnection.getOutputStream();
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream,
                    CHARSET), true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            printWriter.append("--" + BOUNDARY).append(LINE_FEED);
            printWriter.append(
                    "Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            printWriter.append("Content-Type: text/plain; charset=" + CHARSET)
                    .append(LINE_FEED);

            printWriter.append(LINE_FEED);
            printWriter.append(value).append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            printWriter.append("--" + BOUNDARY).append(LINE_FEED);
            printWriter.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"").append(
                    LINE_FEED);
            printWriter.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);

            printWriter.append("Content-Transfer-Encoding: binary").append(
                    LINE_FEED);
            printWriter.append(LINE_FEED);
            printWriter.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                System.out.println(" bytesRead "+bytesRead);
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            printWriter.append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            printWriter.append(name + ": " + value).append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Completes the request and receives response from the com.server.
         *
         * @return a list of Strings as response in case the com.server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public HttpURLConnection prepareConnection() {
            printWriter.append(LINE_FEED).flush();
            printWriter.append("--" + BOUNDARY + "--").append(LINE_FEED);
            printWriter.close();

            return httpURLConnection;
        }
    }

    private class FileCacheBGTask extends AsyncTask<Void, Void, Void> {
        String response;

        public FileCacheBGTask(String response) {
            this.response = response;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (null != fileName) {
                FileCache.writeFile(mContext, fileName, response.getBytes());
            }
            return null;
        }
    }

    private class FileReadCacheBGTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            if (!TextUtils.isEmpty(FileCache.readFile(mContext, fileName)) && cacheEnabled) {
                setAlreadyTaken(true);
                String lastResponse = FileCache.readFile(mContext, fileName);
                return lastResponse;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String lastResponse) {
            super.onPostExecute(lastResponse);
            if (fragment != null && lastResponse != null) {
                if (fragment.isAdded() && null != requestReciever && parseJson(lastResponse)) {
                    if (null != getDataArray()) {
                        requestReciever.onSuccess(RequestCode, lastResponse, getDataArray());
                    } else if (null != getDataObject()) {
                        requestReciever.onSuccess(RequestCode, lastResponse, getDataObject());
                    } else {
                        requestReciever.onSuccess(RequestCode, lastResponse, message);
                    }
                }
            } else {
                if (null != requestReciever && parseJson(lastResponse)) {
                    if (null != getDataArray()) {
                        requestReciever.onSuccess(RequestCode, lastResponse, getDataArray());
                    } else if (null != getDataObject()) {
                        requestReciever.onSuccess(RequestCode, lastResponse, getDataObject());
                    } else {
                        requestReciever.onSuccess(RequestCode, lastResponse, message);
                    }

                }
            }
        }
    }
}