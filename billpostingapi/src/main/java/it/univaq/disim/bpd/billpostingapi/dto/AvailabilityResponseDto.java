package it.univaq.disim.bpd.billpostingapi.dto;

import java.util.List;
import java.util.Map;

public class AvailabilityResponseDto {
    
    private String requestId;
    private double totalPrice;
    private List<Map<String, Object>> selectedZones;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Map<String, Object>> getSelectedZones() {
        return selectedZones;
    }

    public void setSelectedZones(List<Map<String, Object>> selectedZones) {
        this.selectedZones = selectedZones;
    }
}
