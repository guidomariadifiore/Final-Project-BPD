package it.univaq.disim.bpd.billpostingapi.dto;

public class CityBudgetDto {
    
    private String city;
    private double maxBudget;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(double maxBudget) {
        this.maxBudget = maxBudget;
    }
}
