package com.ourincheon.wazap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.reqContest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


public class JoinActivity extends AppCompatActivity {

    reqContest contest;
    TextView jTitle,jButton,jCate,jApply,jRec,jName,jCover,jMem,jDate,jHost,jLoc,jPos,jPro;
    String access_token,num,Writer;
    Button jPick,jBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        jTitle = (TextView) findViewById(R.id.jTitle);
        jCate =  (TextView) findViewById(R.id.jCate);
        jApply = (TextView) findViewById(R.id.jApply);
        jRec = (TextView) findViewById(R.id.jRec);
        jName = (TextView) findViewById(R.id.jName);
        jCover = (TextView) findViewById(R.id.jCover);
        jMem = (TextView) findViewById(R.id.jMem);
        jDate = (TextView) findViewById(R.id.jDate);
        jHost = (TextView) findViewById(R.id.jHost);
        jLoc = (TextView) findViewById(R.id.jLoc);
        jPos = (TextView) findViewById(R.id.jPos);


        Intent intent = getIntent();
        num =  intent.getExtras().getString("id");

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        access_token = pref.getString("access_token", "");

        loadPage(num);


        jBefore = (Button) findViewById(R.id.jBefore);
        jBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        jButton = (TextView) findViewById(R.id.jButton);
        jButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyContest(num, access_token);
            }
        });

        jPick = (Button) findViewById(R.id.jPick);
        jPick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pickContest(num, access_token);
            }
        });

        jPro = (TextView) findViewById(R.id.jPro);
        jPro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, showMypageActivity.class);
                intent.putExtra("user_id", Writer);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
    }

    void pickContest(String num, final String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-------------------" + access_token);
        Call<LinkedTreeMap> call = service.clipContests(num, access_token);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {

                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();

                    if(!msg.equals("이미 찜한 게시물 입니다.")) {
                        if (result) {
                            Log.d("저장 결과: ", msg);
                            Toast.makeText(getApplicationContext(), "찜 되었습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("저장 실패: ", msg);
                            Toast.makeText(getApplicationContext(), "찜 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        removeClip();
                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });
    }

    void removeClip()
    {

    }

    void applyContest(String num, String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-------------------"+access_token);
        Call<LinkedTreeMap> call = service.applyContests(num,access_token);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {

                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();

                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "신청 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });

    }

    void loadPage(String num)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        Call<reqContest> call = service.getConInfo(num,access_token);
        call.enqueue(new Callback<reqContest>() {
            @Override
            public void onResponse(Response<reqContest> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    contest = response.body();

                    Log.d("SUCCESS", contest.getMsg());

                    System.out.println(contest.getData().getTitle());
                    jTitle.setText(contest.getData().getTitle());
                    jCate.setText(contest.getData().getCategories());
                    jApply.setText(String.valueOf(contest.getData().getAppliers()));
                    jMem.setText(String.valueOf(contest.getData().getMembers()));
                    jRec.setText(" / "+String.valueOf(contest.getData().getRecruitment()));
                    jName.setText(contest.getData().getUsername());
                    jCover.setText(contest.getData().getCover());
                    jHost.setText(contest.getData().getHosts());
                    jLoc.setText(contest.getData().getCont_locate());
                    jPos.setText(contest.getData().getPositions());
                    Writer = contest.getData().getCont_writer();

                    String[] parts = contest.getData().getPeriod().split("T");
                    Dday day = new Dday();
                    jDate.setText("D - "+day.dday(parts[0]));

                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
