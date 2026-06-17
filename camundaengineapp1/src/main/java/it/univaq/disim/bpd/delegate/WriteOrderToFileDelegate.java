package it.univaq.disim.bpd.delegate;

import it.univaq.disim.bpd.service.FileService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("writeOrderToFileDelegate")
public class WriteOrderToFileDelegate implements JavaDelegate {

    @Autowired
    private FileService fileService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String requestId = (String) execution.getVariable("requestId");
        String username = (String) execution.getVariable("username");
        String invoiceNumber = (String) execution.getVariable("invoiceNumber");
        Double amountDue = null;
        Object amountDueObj = execution.getVariable("amountDue");
        if (amountDueObj instanceof Number) {
            amountDue = ((Number) amountDueObj).doubleValue();
        } else if (amountDueObj instanceof String) {
            amountDue = Double.parseDouble((String) amountDueObj);
        }

        Object selectedZones = execution.getVariable("selectedZones");
        String selectedZonesJson = selectedZones != null ? selectedZones.toString() : "[]";

        fileService.writeConfirmedRequestToFile(requestId, username, selectedZonesJson, invoiceNumber, amountDue != null ? amountDue : 0.0);
    }
}
