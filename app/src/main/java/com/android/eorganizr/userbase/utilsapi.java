package com.android.eorganizr.userbase;

public class utilsapi {
    public static final String BASE_URL_API = "localhost:8000/api";

    public static BaseApiService getAPIService(){
        return retrofitclient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
