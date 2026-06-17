package it.univaq.disim.bpd.billpostingapi.dto;

public class DecisionResponseDto {
    private String accountHolder;
    private String invoiceNumber;
    private double amountDue;
    private String status; // For displaying "Canceled" or "Confirmed"
    
    // Empty bill constructor
    public DecisionResponseDto() {
        this.accountHolder = "";
        this.invoiceNumber = "";
        this.amountDue = 0.0;
        this.status = "Canceled";
    }

    public DecisionResponseDto(String accountHolder, String invoiceNumber, double amountDue, String status) {
        this.accountHolder = accountHolder;
        this.invoiceNumber = invoiceNumber;
        this.amountDue = amountDue;
        this.status = status;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
