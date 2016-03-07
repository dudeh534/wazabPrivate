package com.ourincheon.wazap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.KaKao.infoKaKao;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.ContestInfo;
import com.ourincheon.wazap.facebook.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Hsue.
 */

public class RecruitActivity extends AppCompatActivity {

    EditText reTitle, reHost, reNum, reIntro, reDate;
    TextView save;
    ImageView profileImg;
    String thumbnail;
    ContestData con;
    ContestInfo contest2;
    int mode,contest_id;
    String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_new);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        access_token = pref.getString("access_token", "");
        contest2 = new ContestInfo();
        contest2.setAccess_token(access_token);

        reTitle = (EditText) findViewById(R.id.reTitle);
        reHost = (EditText) findViewById(R.id.reHost);
        reNum = (EditText) findViewById(R.id.reNum);
        reDate = (EditText) findViewById(R.id.reDate);
        reIntro = (EditText) findViewById(R.id.reIntro);



        Intent intent = getIntent();
        mode = intent.getExtras().getInt("edit");
        con = (ContestData) intent.getExtras().getSerializable("contestD");
        if(mode==1) {
            System.out.println("----------------------------------------------");
            reTitle.setText(con.getTitle());
            System.out.println(con.getTitle());
            reHost.setText(con.getHosts());
            reNum.setText(String.valueOf(con.getRecruitment()));
            reDate.setText(con.getPeriod());
            reIntro.setText(con.getCover());
            contest_id = con.getContests_id();
        }

        save = (TextView) findViewById(R.id.reButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "adshf;aksdf", Toast.LENGTH_SHORT).show();
                contest2.setTitle(reTitle.getText().toString());
                contest2.setRecruitment(Integer.parseInt(reNum.getText().toString()));
                contest2.setHosts(reHost.getText().toString());
                contest2.setCover(reIntro.getText().toString());

                contest2.setCategories("논문/학술");
                contest2.setPeriod(reDate.getText().toString());
                //contest2.setPeriod("2016-04-29");
                contest2.setPositions("기획자");

                if(mode == 0)
                    sendContest(contest2);
                else
                    editCon(contest2);

                finish();
            }
        });
    }

    void editCon(ContestInfo contest)
    {
        String baseUrl = "http://come.n.get.us.to/";
        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WazapService service = client.create(WazapService.class);

        System.out.println(String.valueOf(contest_id));
        System.out.println(contest.getAccess_token());
        Call<LinkedTreeMap> call = service.editContest(String.valueOf(contest_id), contest);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {
                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();


                    if (result) {
                        Log.d("수정 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("수정 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "수정이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body is NULL", response.message());
                    Toast.makeText(getApplicationContext(), "수정이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println("------------------"+contest2.getAccess_token());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    void sendContest(ContestInfo contest)
    {
        String baseUrl = "http://come.n.get.us.to/";
        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WazapService service = client.create(WazapService.class);

        Call<LinkedTreeMap> call = service.createContests(contest);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {
                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();


                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "저장이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body is NULL", response.message());
                    Toast.makeText(getApplicationContext(), "저장이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println("------------------"+contest2.getAccess_token());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

}