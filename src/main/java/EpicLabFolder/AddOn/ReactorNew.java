package EpicLabFolder.AddOn;

import java.util.HashMap;
public class ReactorNew {
    private String name;
    private String type;
    private  String country;
    private  String region;
    private String operator;
    private int burnup;
    private int thermalCapacity;
    private int firstGridConnection;
    private HashMap<Integer, Double> loadFactorPerYear;

    private HashMap<Integer, Double> consumptionPerYear;
    public ReactorNew(String name, String type, String country, String region, String operator, int burnup, int thermalCapacity, int firstGridConnection, HashMap<Integer, Double> loadFactorPerYear) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.region = region;
        this.operator = operator;
        this.burnup = burnup;
        this.thermalCapacity = thermalCapacity;
        this.firstGridConnection = firstGridConnection;
        this.loadFactorPerYear = loadFactorPerYear;
        this.consumptionPerYear = new HashMap<Integer, Double>();
    }
    public String getCountry() {return country;}

    public  String getRegion(){return region;}
    public String getOperator() {return operator;}

    public HashMap<Integer, Double> getConsumptionPerYear() {return consumptionPerYear;}

    public void calculateConsumptionPerYear() {
        for (HashMap.Entry<Integer, Double> entry : loadFactorPerYear.entrySet()) {
            Integer year = entry.getKey();
            Double fLoad = entry.getValue();
            Double consumption = thermalCapacity / burnup * fLoad / 100000*365;
            if (year == firstGridConnection){consumption *= 3;}
            consumptionPerYear.put(year,consumption);
        }

    }
}
