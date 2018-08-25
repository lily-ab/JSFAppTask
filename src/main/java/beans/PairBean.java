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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ManagedBean(name = "pairBean")
@ViewScoped
public class PairBean {

    private List<CurPair> pairs=new ArrayList<>();
    private List<CurPair> firstPairs=new ArrayList<>();
    private List<CurPair> secondPirs=new ArrayList<>();
    private List<CurPair> thirdPairs=new ArrayList<>();
    private List<CurPair> allPairs;
    private int curPairId;
    private int colNum;

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public int getCurPairId() {
        return curPairId;
    }

    public void setCurPairId(int curPair) {
        this.curPairId = curPair;
    }

    public void splitColumns(int num){

    }

    public void addCurPair() {

        CurPair curPair=findCurPairById(curPairId);

        if (curPair!=null) {
            pairs.add(curPair);
            init();
        }
    }

    public void deleteCurPair(CurPair curPair){

        if(curPair!=null) {
            pairs.remove(curPair);
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

        for(Iterator<CurPair> it=pairs.iterator();it.hasNext();){
            CurPair curPair1=it.next();
            curPair1.setPrice(priceRequest.makePriceRequest(curPair1.getId()));
        }
    }

    public List<CurPair> getFirstPairs() {
        return firstPairs;
    }

    public void setFirstPairs(List<CurPair> firstPairs) {
        this.firstPairs = firstPairs;
    }

    public List<CurPair> getSecondPirs() {
        return secondPirs;
    }

    public void setSecondPirs(List<CurPair> secondPirs) {
        this.secondPirs = secondPirs;
    }

    public List<CurPair> getThirdPairs() {
        return thirdPairs;
    }

    public void setThirdPairs(List<CurPair> thirdPairs) {
        this.thirdPairs = thirdPairs;
    }

    public void splitPairs(){
        int size=pairs.size();
        if(colNum==2 && size>=2){
            int dif= (int) Math.ceil(size/2);
            firstPairs=pairs.subList(0,dif+1);
            secondPirs=pairs.subList(dif+1,size);
            thirdPairs=new ArrayList<>();
        }
        else if(colNum==3 && size>=3){
            int dif= (int) Math.ceil(size/3);
            thirdPairs=pairs.subList(size-dif,size);
            int dif2=(size-dif)/2;
            secondPirs=pairs.subList(size-dif-dif2,size-dif);
            firstPairs=pairs.subList(0,size-dif-dif2);
        }
        else {
            firstPairs = pairs;
            secondPirs=new ArrayList<>();
            thirdPairs=new ArrayList<>();
        }

    }

    @PostConstruct
    public void init(){

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(() -> {
            splitPairs();
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
