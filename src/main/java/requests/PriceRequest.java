package requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class PriceRequest {

    private static final String API_URL = "http://priceticker.exactpro.com/RestInstruments/json/price";
    private final HttpClient client = HttpClientBuilder.create().build();

    public String makePriceRequest(int id) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(API_URL)
                .addParameter("id", Integer.toString(id));
        HttpGet get = new HttpGet(builder.build());
        HttpResponse response = client.execute(get);
        String result = new BasicResponseHandler().handleResponse(response);
        JSONObject postsObj = new JSONObject(result);
        return postsObj.get("price").toString();
    }
}
