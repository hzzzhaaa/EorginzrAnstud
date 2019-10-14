package com.android.eorganizr.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.eorganizr.Constant.AppConstant;
import com.android.eorganizr.MainActivity;
import com.android.eorganizr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.android.eorganizr.Util.RetrofitUtil.ApiUtil;
import com.android.eorganizr.Util.RetrofitUtil.BaseApiService;

public class RegisterActivity extends AppCompatActivity {
    EditText etNama;
    EditText etEmail;
    EditText etPassword;
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mApiService = ApiUtil.getAPIService(this);
        initComponents();

    }
    private void initComponents(){
        etNama = (EditText) findViewById(R.id.etNama);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegister();
            }
        });

    }

    private void requestRegister() {
            mApiService.registerOrganizrRequest(etNama.getText().toString(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                loading.dismiss();
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    if (jsonResponse.getString("response_status").equals(AppConstant.RESPONSE_OK)){
                                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("token", jsonResponse.getString("access_token"));
                                        editor.commit();

                                        Intent intent = new Intent(mContext, MainActivity.class);

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Jika login gagal
                                        String error_message = jsonResponse.getString("response_msg");
                                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext,"System Error", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext,"Server Error", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                            Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

}

