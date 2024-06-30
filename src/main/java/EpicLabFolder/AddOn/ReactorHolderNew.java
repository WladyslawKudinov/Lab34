package EpicLabFolder.AddOn;

import java.util.ArrayList;
import java.util.HashMap;

public class ReactorHolderNew {
    private ArrayList<ReactorNew> reactorNewList;
    public ReactorHolderNew(){this.reactorNewList = new ArrayList<>();
    }
    public void addReactor(ReactorNew reactorNew){
        reactorNewList.add(reactorNew);
    }

    public void calculateConsumptionPerYear(){
        for (ReactorNew reactorNew : reactorNewList){
            reactorNew.calculateConsumptionPerYear();
        }
    }

    public HashMap<String, HashMap<Integer, Double>> agregatePerCountry(){
        HashMap<String, HashMap<Integer, Double>> consumptionPerCountry = new HashMap<>();
        for (ReactorNew reactorNew : reactorNewList) {
            String country = reactorNew.getCountry();
            HashMap<Integer, Double> consumption = reactorNew.getConsumptionPerYear();
            HashMap<Integer, Double> countryConsumption = consumptionPerCountry.getOrDefault(country, new HashMap<>());
            for (HashMap.Entry<Integer, Double> entry : consumption.entrySet()) {
                Integer year = entry.getKey();
                Double cons = entry.getValue();
                double updatedConsumption = countryConsumption.getOrDefault(year, 0.0) + cons;
                updatedConsumption = Math.round(updatedConsumption * 100.0) / 100.0;
                countryConsumption.put(year, updatedConsumption);
            }
            consumptionPerCountry.put(country, countryConsumption);
        }
        return consumptionPerCountry;
    }

    public HashMap<String, HashMap<Integer, Double>> agregatePerOperator(){
        HashMap<String, HashMap<Integer, Double>> consumptionPerOperator = new HashMap<>();
        for (ReactorNew reactorNew : reactorNewList) {
            String country = reactorNew.getOperator();
            HashMap<Integer, Double> consumption = reactorNew.getConsumptionPerYear();
            HashMap<Integer, Double> countryConsumption = consumptionPerOperator.getOrDefault(country, new HashMap<>());
            for (HashMap.Entry<Integer, Double> entry : consumption.entrySet()) {
                Integer year = entry.getKey();
                Double cons = entry.getValue();
                double updatedConsumption = countryConsumption.getOrDefault(year, 0.0) + cons;
                updatedConsumption = Math.round(updatedConsumption * 100.0) / 100.0;
                countryConsumption.put(year, updatedConsumption);
            }
            consumptionPerOperator.put(country, countryConsumption);
        }
        return consumptionPerOperator;
    }

    public HashMap<String, HashMap<Integer, Double>> agregatePerRegion(){
        HashMap<String, HashMap<Integer, Double>> consumptionPerRegion = new HashMap<>();
        for (ReactorNew reactorNew : reactorNewList) {
            String country = reactorNew.getRegion();
            HashMap<Integer, Double> consumption = reactorNew.getConsumptionPerYear();
            HashMap<Integer, Double> countryConsumption = consumptionPerRegion.getOrDefault(country, new HashMap<>());
            for (HashMap.Entry<Integer, Double> entry : consumption.entrySet()) {
                Integer year = entry.getKey();
                Double cons = entry.getValue();
                double updatedConsumption = countryConsumption.getOrDefault(year, 0.0) + cons;
                updatedConsumption = Math.round(updatedConsumption * 100.0) / 100.0;
                countryConsumption.put(year, updatedConsumption);
            }
            consumptionPerRegion.put(country, countryConsumption);
        }
        return consumptionPerRegion;
    }

}
