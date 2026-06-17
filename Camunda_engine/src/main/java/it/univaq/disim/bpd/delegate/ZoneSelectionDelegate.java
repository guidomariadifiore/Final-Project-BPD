package it.univaq.disim.bpd.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.bpd.dto.CityBudgetDto;
import it.univaq.disim.bpd.dto.ZoneDto;
import it.univaq.disim.bpd.service.ZoneSelectionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.camunda.spin.json.SpinJsonNode;
import static org.camunda.spin.Spin.JSON;

import java.util.List;

@Component("zoneSelectionDelegate")
public class ZoneSelectionDelegate implements JavaDelegate {

    @Autowired
    private ZoneSelectionService zoneSelectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String posterFormat = (String) execution.getVariable("posterFormat");
        
        // Retrieve city budgets (passed as JSON string from REST API)
        String cityBudgetsJson = (String) execution.getVariable("cityBudgets");
        List<CityBudgetDto> cityBudgets = objectMapper.readValue(cityBudgetsJson, new TypeReference<List<CityBudgetDto>>() {});

        String strategy = (String) execution.getVariable("strategy");
        if (strategy == null) {
            strategy = "greedy";
        }

        // Use service to perform Greedy Algorithm
        List<ZoneDto> selectedZones = zoneSelectionService.selectZones(posterFormat, cityBudgets, strategy);
        
        double totalPrice = selectedZones.stream().mapToDouble(ZoneDto::getPrice).sum();

        // Convert the list of objects back to Spin JSON to be easily manipulated/returned by Camunda
        String selectedZonesJson = objectMapper.writeValueAsString(selectedZones);
        SpinJsonNode spinZones = JSON(selectedZonesJson);

        execution.setVariable("selectedZones", spinZones);
        execution.setVariable("totalPrice", totalPrice);
    }
}
