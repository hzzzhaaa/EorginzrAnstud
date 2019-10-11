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

public class RegisterOrganizrActivity extends AppCompatActivity {
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
        mApiService = ApiUtil.getAPIService();
        initComponents();

    }
    private void initComponents(){
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegisterOrganizr();
            }
        });

    }

    private void requestRegisterOrganizr() {
        mApiService.registerOrganizrRequest(etNama.getText().toString(),
                etEmail.getText().toString(),
                etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("token", jsonResponse.getString("token"));
                                editor.commit();

                                Intent intent = new Intent(mContext, MainActivity.class);

                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
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

