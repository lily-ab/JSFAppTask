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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ManagedBean(name = "pairBean")
@ViewScoped
public class PairBean {

    private List<CurPair> pairs = new ArrayList<>();//list of the pairs added to the table
    private List<CurPair> firstPairs;//list of pairs for the first column
    private List<CurPair> secondPairs;//list of pairs for the second column
    private List<CurPair> thirdPairs;//list of pairs for the third column
    private List<CurPair> allPairs;
    private int curPairId;
    private int colNum;//number of columns


    /**
     * making a pool with a thread where uploading of prices is repeating
     */
    @PostConstruct
    public void init() {
        splitPairs();
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(() -> {

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

    public void addCurPair() {

        CurPair curPair = findCurPairById(curPairId);

        if (curPair != null) {
            pairs.add(curPair);
            init();
        }
    }

    public void deleteCurPair(CurPair curPair) {

        if (curPair != null) {

            for (int i = 0; i < pairs.size(); i++) {
                if (pairs.get(i).equals(curPair)) {
                    pairs.remove(i);
                }
            }
            for (int i = 0; i < firstPairs.size(); i++) {
                if (firstPairs.get(i).equals(curPair)) {
                    firstPairs.remove(i);
                }
            }
            for (int i = 0; i < secondPairs.size(); i++) {
                if (secondPairs.get(i).equals(curPair)) {
                    secondPairs.remove(i);
                }
            }
            for (int i = 0; i < thirdPairs.size(); i++) {
                if (thirdPairs.get(i).equals(curPair)) {
                    thirdPairs.remove(i);
                }
            }
        }
    }

    private CurPair findCurPairById(int id) {

        for (CurPair curPair : allPairs) {
            if (curPair.getId() == id) return curPair;
        }
        return null;
    }

    public List<CurPair> getPairs() {
        return pairs;
    }

    public List<CurPair> getAllPairs() throws IOException {
        PairRequest request = new PairRequest();
        allPairs = request.getPairList();
        return allPairs;
    }

    private void initPairs() throws IOException, URISyntaxException {

        PriceRequest priceRequest = new PriceRequest();

        for (CurPair pair : pairs) {
            pair.setPrice(priceRequest.makePriceRequest(pair.getId()));
        }
    }

    public List<CurPair> getFirstPairs() {
        return firstPairs;
    }

    public void splitPairs() {
        int size = pairs.size();
        if (colNum == 2 && size >= 2) {
            int dif = (int) Math.ceil(size / 2);
            if (size % 2 == 0) {
                firstPairs = new ArrayList<>(pairs.subList(0, dif));
                secondPairs = new ArrayList<>(pairs.subList(dif, size));
            } else {
                firstPairs = new ArrayList<>(pairs.subList(0, dif + 1));
                secondPairs = new ArrayList<>(pairs.subList(dif + 1, size));
            }
            thirdPairs = new ArrayList<>();
        } else if (colNum == 3 && size >= 3) {
            int dif = (int) Math.ceil(size / 3);
            thirdPairs = new ArrayList<>(pairs.subList(size - dif, size));
            int dif2 = (size - dif) / 2;
            secondPairs = new ArrayList<>(pairs.subList(size - dif - dif2, size - dif));
            firstPairs = new ArrayList<>(pairs.subList(0, size - dif - dif2));
        } else {
            firstPairs = pairs;
            secondPairs = new ArrayList<>();
            thirdPairs = new ArrayList<>();
        }

    }

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

    public void setFirstPairs(List<CurPair> firstPairs) {
        this.firstPairs = firstPairs;
    }

    public List<CurPair> getSecondPairs() {
        return secondPairs;
    }

    public void setSecondPairs(List<CurPair> secondPairs) {
        this.secondPairs = secondPairs;
    }

    public List<CurPair> getThirdPairs() {
        return thirdPairs;
    }

    public void setThirdPairs(List<CurPair> thirdPairs) {
        this.thirdPairs = thirdPairs;
    }
}
