package com.home.reservas.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.home.reservas.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by Reynaldo on 22/03/2016.
 */
public class LinkedInLoginFragment extends DialogFragment{

    private WebView webView;
    loginListener myloginListener;

    private static final String API_KEY = "776eok8m4g9tqs";
    private static final String SECRET_KEY = "DarpL5SIYX2lYbIB";
    private static final String STATE = "E3ZYKC1T6H2yP4z";
    private static final String REDIRECT_URI = "http://www.google.es";

    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.linkedin_login, container, false);
        webView = (WebView) v.findViewById (R.id.webView);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myloginListener = (loginListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * Method that generates the url for get the access token from the Service
     * @return Url
     */
    private static String getAccessTokenUrl(String authorizationToken){
        return ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
    }
    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND+STATE_PARAM+EQUALS+STATE
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }


    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();

                try{
                    Response response = httpClient.newCall(request).execute();
                    if(response!=null){
                        //If status is OK 200
                        if(response.isSuccessful()) {
                            Object responseJson = response.body();
                            responseJson.toString();
                            myloginListener.onLogin();

                            /**
                             //Convert the string result to a JSON Object
                             JSONObject resultJson = new JSONObject(result);
                             //Extract data from JSON Response
                             int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                             String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                             Log.e("Tokenm", ""+accessToken);
                             if(expiresIn>0 && accessToken!=null){
                             Log.i("Authorize", "This is the access Token: "+accessToken+". It will expires in "+expiresIn+" secs");
                             //Calculate date of expiration
                             Calendar calendar = Calendar.getInstance();
                             calendar.add(Calendar.SECOND, expiresIn);
                             long expireDate = calendar.getTimeInMillis();
                             ////Store both expires in and access token in shared preferences
                             SharedPreferences preferences = MainActivity.this.getSharedPreferences("user_info", 0);
                             SharedPreferences.Editor editor = preferences.edit();
                             editor.putLong("expires", expireDate);
                             editor.putString("accessToken", accessToken);
                             editor.commit();
                             return true;
                             }*/
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status){
            if(status){
                //If everything went Ok, change to another activity.
                Log.d("executed","");
            }
        }

    };

    /*interfaz para comunicar con la actividad*/
    public interface loginListener {
        public void onLogin();
    }

}