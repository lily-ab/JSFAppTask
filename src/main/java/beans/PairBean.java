package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "pairBean")
@SessionScoped
public class PairBean {

    private String[] pairs={"aaa","bbb"};

    public String[] getPairs() {
        return pairs;
    }
}
