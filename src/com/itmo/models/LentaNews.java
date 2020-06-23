package com.itmo.models;

import org.json.JSONObject;

public class LentaNews extends News {

    public LentaNews(JSONObject json) {
        super();
        title = json.getJSONObject("info").getString("title");
        resource = "lenta";
        id = json.getJSONObject("info").getString("id");
    }

}
