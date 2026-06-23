package it.univaq.disim.bpd.billpostingapi.dto;

import java.util.List;

public class AvailabilityRequestDto {
    
    private String username;
    private String posterFormat;
    private List<CityBudgetDto> cityBudgets;
    private String strategy;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosterFormat() {
        return posterFormat;
    }

    public void setPosterFormat(String posterFormat) {
        this.posterFormat = posterFormat;
    }

    public List<CityBudgetDto> getCityBudgets() {
        return cityBudgets;
    }

    public void setCityBudgets(List<CityBudgetDto> cityBudgets) {
        this.cityBudgets = cityBudgets;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
