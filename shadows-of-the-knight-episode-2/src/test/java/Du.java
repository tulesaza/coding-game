import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Du {

    @Test

    public void round() {


        var map = new HashMap<String,Integer>();
        map.put("a",200);
        try {
            Integer c = map.get("b");
            System.out.println(c);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
