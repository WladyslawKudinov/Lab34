package NoHumanJustLab;

import java.util.HashMap;
import java.util.Map;

public class ReactorTypeStorage {
    private Map<String, ReactorType> reactortypeMap;
    public ReactorTypeStorage() {
        this.reactortypeMap = new HashMap<>();
    }
    public void addReactor(String key, ReactorType reactorType) {
        reactortypeMap.put(key, reactorType);
    }
    public Map<String, ReactorType> getReactorMap() {return reactortypeMap;}

}
