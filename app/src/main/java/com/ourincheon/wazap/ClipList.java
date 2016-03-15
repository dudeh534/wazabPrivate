package com.ourincheon.wazap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.Retrofit.Alarms;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.Contests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hsue on 16. 2. 25.
 */
public class ClipList extends AppCompatActivity {
    ScrollView scrollView;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    Contests clips;
    ArrayList<ContestData> clip_list;
    int count, posi;
    String[] id_list;
    AlertDialog dialog;
    String access_token,contest_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_list);

        mListView = (ListView) findViewById(R.id.cliplistView);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        access_token = pref.getString("access_token", "");

        clip_list = new ArrayList<ContestData>();

        loadClip(access_token);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("찜 목록 지우기").setMessage("해당 스크랩을 지우시겠습니까?")
                .setCancelable(true).setPositiveButton("지우기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                System.out.println("----------------------"+posi);
                deleteClip(id_list[posi]);
                loadClip(access_token);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ClipList.this, "취소", Toast.LENGTH_SHORT).show();
                posi = position;
                dialog.show();
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //        ContestData mData = mAdapter.mListData.get(position);
        //        contest_id = String.valueOf(mData.getContests_id());
        //        applyContest( contest_id, access_token);
            }
        });

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //loadClip(access_token);
    }

    void applyContest(String num, String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-------------------" + access_token);
        Call<LinkedTreeMap> call = service.applyContests(num, access_token);
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
                        //deleteClip(contest_id);
                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
    void deleteClip(String contest_id)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);


        Call<LinkedTreeMap> call = service.delClip(contest_id, access_token);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {

                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();

                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "스크랩 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "스크랩취소 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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


    void loadClip(String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);


        Call<Contests> call = service.getCliplist(access_token, 200, 200);
        call.enqueue(new Callback<Contests>() {
            @Override
            public void onResponse(Response<Contests> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    clips = response.body();

                   String result = new Gson().toJson(clips);
                    Log.d("SUCESS-----", result);

                    JSONObject jsonRes;
                    try {
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        count = jsonArr.length();
                        id_list = new String[count];
                        System.out.println(count);
                        for (int i = 0; i < count; i++) {
                            mAdapter.addItem(jsonArr.getJSONObject(i).getString("title"),
                                    jsonArr.getJSONObject(i).getString("period"),
                                    jsonArr.getJSONObject(i).getString("categories"),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("contests_id")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")));

                            id_list[i]= jsonArr.getJSONObject(i).getString("contests_id");
                          }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
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



    private class ViewHolder {
       // public ImageView mIcon;

        public TextView Dday;
        public TextView cTitle;
        public TextView Cate;
        public TextView Member;
        Button Join;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ContestData> mListData = new ArrayList<ContestData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String title,String period, String categories, int id, int member ){
            ContestData addInfo = null;
            addInfo = new ContestData();
            addInfo.setTitle(title);
            String[] parts = period.split("T");
            addInfo.setPeriod(parts[0]);
            addInfo.setCategories(categories);
            addInfo.setContests_id(id);
            addInfo.setRecruitment(member);

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.clip_item, null);

                holder.Dday = (TextView) convertView.findViewById(R.id.cDday);
                holder.cTitle = (TextView) convertView.findViewById(R.id.cTitle);
                holder.Cate = (TextView) convertView.findViewById(R.id.cCate);
                holder.Member = (TextView) convertView.findViewById(R.id.cMember);

                holder.Join = (Button) convertView.findViewById(R.id.cJoin);
                holder.Join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContestData mData = mListData.get(position);
                        contest_id = String.valueOf(mData.getContests_id());
                        //Toast.makeText(ClipList.this, contest_id+position, Toast.LENGTH_SHORT).show();
                        applyContest(contest_id, access_token);
                    }
                });

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ContestData mData = mListData.get(position);

            Dday day = new Dday();
            holder.Dday.setText("D - "+day.dday(mData.getPeriod()));

            holder.cTitle.setText(mData.getTitle());

            holder.Cate.setText(mData.getCategories());

            holder.Member.setText("모집인원 " + mData.getRecruitment() + "명");

            return convertView;
        }
    }
}

