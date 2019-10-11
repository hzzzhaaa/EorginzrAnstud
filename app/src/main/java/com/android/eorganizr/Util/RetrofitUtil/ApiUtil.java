package com.android.eorganizr.Util.RetrofitUtil;


import com.android.eorganizr.Constant.AppConstant;

public class ApiUtil {
    public static final String BASE_URL_API = AppConstant.BASE_URL;

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
