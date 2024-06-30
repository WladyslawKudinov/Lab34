package EpicLabFolder.OldCodebase;

import java.util.HashMap;
import java.util.Map;

public class StorageOfReactorsOld {
    private Map<String, ReactorOld> reactortypeMap;
    public StorageOfReactorsOld() {
        this.reactortypeMap = new HashMap<>();
    }
    public void addReactor(String key, ReactorOld reactorOld) {
        reactortypeMap.put(key, reactorOld);
    }
    public Map<String, ReactorOld> getReactorMap() {return reactortypeMap;}
    public Map<String, Double> getLoadFactorMap() {
        Map<String, Double> loadFactorMap = new HashMap<>();
        loadFactorMap.put("LWGR", 15.0);
        loadFactorMap.put("GCR", 15.0);
        loadFactorMap.put("FBR", 30.0);
        loadFactorMap.put("HTGR", 10.0);
        for (Map.Entry<String, ReactorOld> entry : reactortypeMap.entrySet()) {
            String reactorClass = entry.getKey();
            ReactorOld reactorOld = entry.getValue();
            double loadFactor = reactorOld.getFirstLoad();
            loadFactorMap.put(reactorClass, loadFactor);
        }
        return loadFactorMap;
    }

}
