package com.android.eorganizr.Util.RetrofitUtil;


import android.content.Context;

import com.android.eorganizr.Constant.AppConstant;

public class ApiUtil {
    public static final String BASE_URL_API = AppConstant.BASE_URL;

    public static BaseApiService getAPIService(Context context){
        return RetrofitClient.getClient(BASE_URL_API,context).create(BaseApiService.class);
    }
}
