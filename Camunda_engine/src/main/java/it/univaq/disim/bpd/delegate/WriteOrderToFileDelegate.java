package it.univaq.disim.bpd.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.bpd.service.FileService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("writeOrderToFileDelegate")
public class WriteOrderToFileDelegate implements JavaDelegate {

    @Autowired
    private FileService fileService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String requestId = (String) execution.getVariable("requestId");
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
        List<Map<String, Object>> zonesList = objectMapper.readValue(selectedZonesJson, new TypeReference<List<Map<String, Object>>>() {});

        Object userObj = execution.getVariable("user");
        String userJson = userObj != null ? userObj.toString() : "{}";
        Map<String, Object> userMap = objectMapper.readValue(userJson, new TypeReference<Map<String, Object>>() {});

        String posterFormat = (String) execution.getVariable("posterFormat");
        Object cityBudgetsObj = execution.getVariable("cityBudgets");
        String cityBudgetsJson = cityBudgetsObj != null ? cityBudgetsObj.toString() : "[]";
        List<Map<String, Object>> budgetsList = objectMapper.readValue(cityBudgetsJson, new TypeReference<List<Map<String, Object>>>() {});

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("requestId", requestId);
        dataModel.put("invoiceNumber", invoiceNumber);
        dataModel.put("amountDue", amountDue != null ? amountDue : 0.0);
        dataModel.put("selectedZones", zonesList);
        dataModel.put("user", userMap);
        dataModel.put("posterFormat", posterFormat);
        dataModel.put("cityBudgets", budgetsList);

        fileService.writeConfirmedRequestToFile(requestId, dataModel);
    }
}
