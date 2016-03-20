package com.ourincheon.wazap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ourincheon.wazap.Retrofit.Contests;
import com.ourincheon.wazap.Retrofit.WeeklyList;

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
    WeeklyList weekly;
    RecyclerAdapter rec;
    contestRecyclerAdapter conRec;
    List<Recycler_item> items;
    List<Recycler_contestItem> contestItems;
    Recycler_item[] item;
    Recycler_contestItem[] contestItem;
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
                access_token = pref.getString("access_token", "");

                items = new ArrayList<>();


                Bundle bundle = getArguments();
                int category = bundle.getInt("position");
                Toast.makeText(getContext(), "ccccccccccccccccc" + category, Toast.LENGTH_SHORT).show();

                loadPage(access_token,category);



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
                contestItems = new ArrayList<>();
                linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_page, container, false);
                content = (RecyclerView) linearLayout.findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
                content.setHasFixedSize(true);
                content.setLayoutManager(layoutManager1);



                SharedPreferences pref2 = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                access_token = pref2.getString("access_token", "");

                items = new ArrayList<>();

                loadContest(access_token);


                content.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), content, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
                // content.setAdapter(new RecyclerAdapter(getActivity(), items, R.layout.fragment_page));
                conRec = new contestRecyclerAdapter(getActivity(), contestItems, R.layout.fragment_page);
                content.setAdapter(conRec);
                linearLayout.removeAllViews();
                linearLayout.addView(content);
                return linearLayout;
                
            default:
                return null;

        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void loadContest(String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);


        System.out.println("------------------------" + access_token);
        Call<WeeklyList> call = service.getWeeklylist(access_token);
        call.enqueue(new Callback<WeeklyList>() {
            @Override
            public void onResponse(Response<WeeklyList> response) {
                if (response.isSuccess() && response.body() != null) {

                    System.out.println("--------------------------------------------------------------------------------------");
                    Log.d("--------------------", response.message());
                    weekly = response.body();

                   String result = new Gson().toJson(weekly);
                   Log.d("SUCESS-----", result);


                    contestItem = new Recycler_contestItem[weekly.getDatasize()];
                    Dday day = new Dday();

                    for (int i = 0; i < weekly.getDatasize(); i++) {
                        contestItem[i] = new Recycler_contestItem(weekly.getData(i).getTITLE(),
                                            weekly.getData(i).getHOSTING(),
                                            "D - " + day.dday(weekly.getData(i).getDEADLINE_DATE()),
                                            weekly.getData(i).getSTART_DATE()+" ~ "+weekly.getData(i).getDEADLINE_DATE(),
                                            weekly.getData(i).getIMG());
                        contestItems.add(contestItem[i]);
                        //
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

    void loadPage(String access_token, int category)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);


        System.out.println("------------------------" + access_token);
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


                    item = new Recycler_item[contest.getDatasize()];
                    id_list = new String[contest.getDatasize()];
                    writer_list = new String[contest.getDatasize()];

                    for (int i = 0; i < contest.getDatasize(); i++) {
                        id_list[i] = String.valueOf(contest.getData(i).getContests_id());
                        writer_list[i] = contest.getData(i).getCont_writer();

                        //String[] parts = jsonArr.getJSONObject(i).getString("period").split("T");
                        String[] parts = contest.getData(i).getPeriod().split("T");
                        Dday day = new Dday();

                        item[i] = new Recycler_item(contest.getData(i).getTitle(),
                                contest.getData(i).getHosts(), contest.getData(i).getUsername(),
                                contest.getData(i).getRecruitment(),
                                contest.getData(i).getMembers(),
                                contest.getData(i).getIs_clip(),
                                contest.getData(i).getCategories(), contest.getData(i).getCont_locate(),
                                "D - " + day.dday(parts[0])
                        );
                        items.add(item[i]);
                        //
                        System.out.println(items.get(i).getName());
                    }
                    /*
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

                            String title =  jsonArr.getJSONObject(i).getString("title");
                            item[i] = new Recycler_item( title,//jsonArr.getJSONObject(i).getString("title"),
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
                    */

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