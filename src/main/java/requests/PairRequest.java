package requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.CurPair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class PairRequest {

    private static final String API_URL = "http://priceticker.exactpro.com/RestInstruments/json/instrumentList";
    private final HttpClient client = HttpClientBuilder.create().build();

    private List<CurPair> getPairs() throws IOException {
        HttpGet get = new HttpGet(API_URL);
        HttpResponse response = client.execute(get);
        String result = new BasicResponseHandler().handleResponse(response);
        List<CurPair> pairList = new ArrayList<>();

        JSONObject postsObj = new JSONObject(result);
        JSONArray posts = postsObj.names();

        Iterator iter = postsObj.keys();
        JSONArray jsonArray = new JSONArray();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            jsonArray.put(postsObj.get(key));
        }

        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            CurPair pair = new CurPair();
            pair.setId(posts.getInt(i));
            pair.setName(jsonArray.getString(i));
            pairList.add(pair);
        }
        return pairList;
    }

    public List<CurPair> getPairList() throws IOException {

        return getPairs();
    }
}
