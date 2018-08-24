package beans;

import models.CurPair;
import requests.PairRequest;
import requests.PriceRequest;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ManagedBean(name = "pairBean")
@ViewScoped
public class PairBean {

    private List<CurPair> pairs=new ArrayList<>();
    private List<CurPair> allPairs;
    private int curPairId;

    public int getCurPairId() {
        return curPairId;
    }

    public void setCurPairId(int curPair) {
        this.curPairId = curPair;
    }

    public void addCurPair() {

        CurPair curPair=findCurPairById(curPairId);

        if (curPair!=null) {
            pairs.add(curPair);
            init();
        }
    }

    private CurPair findCurPairById(int id){

        for(CurPair curPair:allPairs){
            if(curPair.getId()==id) return curPair;
        }
        return null;
    }

    public List<CurPair> getPairs() throws IOException, URISyntaxException {
        return pairs;
    }

    public List<CurPair> getAllPairs() throws IOException {
        PairRequest request = new PairRequest();
        allPairs = request.getPairList();
        return allPairs;
    }

    private void initPairs() throws IOException, URISyntaxException {

        PriceRequest priceRequest = new PriceRequest();
        for (CurPair curPair : pairs) {
            curPair.setPrice(priceRequest.makePriceRequest(curPair.getId()));
        }
    }

    @PostConstruct
    public void init(){

        ExecutorService es = Executors.newFixedThreadPool(1);

        es.execute(() -> {
//            try {
//                pairs = getAllPairs();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            while (true) {
                try {
                    initPairs();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
