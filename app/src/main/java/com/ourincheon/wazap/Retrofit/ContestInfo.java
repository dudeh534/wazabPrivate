package com.ourincheon.wazap.Retrofit;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sue on 2016-02-20.
 */
public class ContestInfo {
    @SerializedName("access_token")
    String access_token;
    @SerializedName("title")
    String title;
    @SerializedName("hosts")
    String hosts;
    @SerializedName("categories")
    ArrayList<String> categories;
    //String categories;
    @SerializedName("period")
    String period;
    @SerializedName("cover")
    String cover;
    @SerializedName("positions")
    String positions;
    @SerializedName("recruitment")
    int recruitment;

    public ContestInfo(){
        //categories = new JSONArray();
        categories = new ArrayList<String>();
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public void setCategories(String category) {
        categories.add(category);
       // categories.put(category);

        //String str = categories.toString();
        /*try {
            categories = new JSONArray(category);
            System.out.print("-----------"+categories);
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
*/System.out.print("-----------"+category);
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public void setRecruitment(int recruitment) {
        this.recruitment = recruitment;
    }
}
