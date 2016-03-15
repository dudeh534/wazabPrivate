package com.ourincheon.wazap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ourincheon.wazap.Retrofit.Contests;
import com.ourincheon.wazap.Retrofit.regUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Youngdo on 2016-02-02.
 */
public class FragmentPage extends Fragment {

    private static final String ARG_POSITION = "position";
    RecyclerView content;
    LinearLayout linearLayout;
    private DataStorage storage = new DataStorage();
    private int position;
    Contests contest;
    RecyclerAdapter rec;
    List<Recycler_item> items;
    Recycler_item[] item;
    String[] id_list,writer_list;
    Intent Joininfo;
    String access_token;

    public static FragmentPage newInstance(int position) {
        FragmentPage f = new FragmentPage();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (position) {
            case 0:
                linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_page, container, false);
                content = (RecyclerView) linearLayout.findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                content.setHasFixedSize(true);
                content.setLayoutManager(layoutManager);

                SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                final String user_id = pref.getString("user_id", "");
                System.out.println(user_id);
                String access_token = pref.getString("access_token","");

                items = new ArrayList<>();
                loadPage(access_token);


                content.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), content, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        System.out.println("###########################################"+writer_list[position]);
                        System.out.println("###########################################"+user_id);
                        if(writer_list[position].equals(user_id))
                            Joininfo = new Intent(getActivity(), MasterJoinActivity.class);
                        else
                            Joininfo = new Intent(getActivity(), JoinActivity.class);
                        Joininfo.putExtra("id", id_list[position]);
                        startActivity(Joininfo);
                        //  Toast.makeText(getContext(), "position" + id_list[position], Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
                // content.setAdapter(new RecyclerAdapter(getActivity(), items, R.layout.fragment_page));
                rec = new RecyclerAdapter(getActivity(), items, R.layout.fragment_page);
                content.setAdapter(rec);
                linearLayout.removeAllViews();
                linearLayout.addView(content);
                return linearLayout;

            case 1:
                /*
                Log.e("position", String.valueOf(storage.getInstance().getPosition()));
                linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_page, container, false);
                content = (RecyclerView) linearLayout.findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                content.setHasFixedSize(true);
                content.setLayoutManager(layoutManager2);
                List<Recycler_item> items2 = new ArrayList<>();
                Recycler_item[] item2 = new Recycler_item[5];
                item2[0] = new Recycler_item("스타트업 브라더 창업지업 프로그램 5차 모집", "마케팅", "2016-01-25 ~ 2016-02-22");
                item2[1] = new Recycler_item("[해외] 제9회 iPhone 사진공모전", "사진/영상/UCC, 해외", "2016-02-16 ~ 2016-03-31");

                for (int i = 0; i < 2; i++) items2.add(item2[i]);
                content.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), content, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String link;
                        if(position==0)
                            link="http://www.brother-korea.com";
                        else
                            link="http://www.ippawards.com/the-competition/";

                        Intent intent = new Intent(getActivity(), WebSiteActivity.class);
                        intent.putExtra("url", link);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
                rec = new RecyclerAdapter(getActivity(), items2, R.layout.fragment_page);
                content.setAdapter(rec);
                //content.setAdapter(new RecyclerAdapter(getActivity(), items2, R.layout.fragment_page));
                linearLayout.removeAllViews();
                linearLayout.addView(content);
                return linearLayout;
                */
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                FrameLayout fl = new FrameLayout(getActivity());
                fl.setLayoutParams(params);

                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                        .getDisplayMetrics());

                TextView v = new TextView(getActivity());
                params.setMargins(margin, margin, margin, margin);
                v.setLayoutParams(params);
                v.setLayoutParams(params);
                v.setGravity(Gravity.CENTER);
                v.setBackgroundResource(R.drawable.background_card);
                v.setText("준비 중 입니다...");

                fl.addView(v);
                return fl;

            default:
                return null;
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void loadPage(String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);


        System.out.println("------------------------"+access_token);
        Call<Contests> call = service.getContests(access_token, 300);
        call.enqueue(new Callback<Contests>() {
            @Override
            public void onResponse(Response<Contests> response) {
                if (response.isSuccess() && response.body() != null) {


                    Log.d("SUCCESS", response.message());
                    contest = response.body();

                    //user = response.body();
                    //Log.d("SUCCESS", reguser.getMsg());

                    String result = new Gson().toJson(contest);
                    Log.d("SUCESS-----", result);

                    JSONObject jsonRes;
                    try {
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        System.out.println("--------------" + jsonArr.length());
                        int len = jsonArr.length();
                        item = new Recycler_item[len];
                        id_list = new String[len];
                        writer_list = new String[len];

                        for (int i = 0; i < len; i++) {
                            id_list[i] = jsonArr.getJSONObject(i).getString("contests_id");
                            writer_list[i] = jsonArr.getJSONObject(i).getString("cont_writer");

                            String[] parts = jsonArr.getJSONObject(i).getString("period").split("T");
                            Dday day = new Dday();

                            item[i] = new Recycler_item(jsonArr.getJSONObject(i).getString("title"),
                                    jsonArr.getJSONObject(i).getString("hosts"), jsonArr.getJSONObject(i).getString("username"),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("members")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("is_clip")),
                                    jsonArr.getJSONObject(i).getString("categories"), jsonArr.getJSONObject(i).getString("cont_locate"),
                                    "D - " + day.dday(parts[0])
                            );
                            items.add(item[i]);
                            //
                            System.out.println(items.get(i).getName());
                        }
                        rec.notifyDataSetChanged();
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
                Log.e("Errorglg''';kl", t.getMessage());
            }
        });

    }



}