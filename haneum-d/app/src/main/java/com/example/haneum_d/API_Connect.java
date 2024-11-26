package com.example.haneum_d;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class API_Connect {

    public void connect(Context context, String api_ver, File file, String idx, Result_Interface result_interface ) {

        API_Interface api_interface = API_Client.getClient().create(API_Interface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part recordFile = MultipartBody.Part.createFormData("audio_file", file.getName(), requestFile);
        RequestBody content_idx = RequestBody.create(MediaType.parse("text/plain"), idx);

        if (api_ver == "1") { // Step1 & Step2 API connect
            api_interface.lev1_start(recordFile, content_idx).enqueue(new Callback<Result_Class>() {
                @Override
                public void onResponse(Call<Result_Class> call, Response<Result_Class> response) {
                    if (response.code()==200) { // success
                        Result_Class result = response.body();
                        String status = result.getStatus();
                        Log.d("status", status);

                        result_interface.success(result);
                    }else if (response.code()==420) { // fail (420 Error)
                        String status = "{\"status\":\"error\"}";
                        Gson gson = new Gson();
                        Result_Class result = gson.fromJson(status, Result_Class.class);
                        result_interface.success(result);
                        Log.d("error", "api 420 error"); // 오디오 (공백 또는 음성 추출 불가) 일 때, 토스트 메시지

                        //Toast.makeText(context, "다시 녹음해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Result_Class> call, Throwable t) {
                    Log.d("error", "api connect fail");
                    result_interface.failure(t);

                    t.printStackTrace();
                }
            });
        } else if (api_ver == "2") { // Step3 API connect
            api_interface.lev2_start(recordFile, content_idx).enqueue(new Callback<Result_Class>() {
                @Override
                public void onResponse(Call<Result_Class> call, Response<Result_Class> response) {
                    if (response.code()==200) { // success
                        Result_Class result = response.body();
                        String status = result.getStatus();
                        Log.d("status", status);

                        result_interface.success(result);
                    }else if (response.code()==420) { // fail (420 Error)
                        String status = "{\"status\":\"error\"}";
                        Gson gson = new Gson();
                        Result_Class result = gson.fromJson(status, Result_Class.class);
                        result_interface.success(result);
                        Log.d("error", "api 420 error"); // 오디오 (공백 또는 음성 추출 불가) 일 때, 토스트 메시지
                    }
                }

                @Override
                public void onFailure(Call<Result_Class> call, Throwable t) { // api connect fail
                    Log.d("error", "api connect fail");
                    result_interface.failure(t);

                    t.printStackTrace();
                }
            });
        }

    }
}
