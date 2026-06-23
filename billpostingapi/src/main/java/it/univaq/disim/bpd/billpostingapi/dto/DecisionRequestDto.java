package it.univaq.disim.bpd.billpostingapi.dto;

public class DecisionRequestDto {
    
    private String requestId;
    private String decision; // "confirm" or "cancel"

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
