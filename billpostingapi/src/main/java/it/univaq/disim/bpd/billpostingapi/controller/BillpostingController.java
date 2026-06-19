package it.univaq.disim.bpd.billpostingapi.controller;

import it.univaq.disim.bpd.billpostingapi.dto.AvailabilityRequestDto;
import it.univaq.disim.bpd.billpostingapi.dto.AvailabilityResponseDto;
import it.univaq.disim.bpd.billpostingapi.dto.CityBudgetDto;
import it.univaq.disim.bpd.billpostingapi.dto.DecisionRequestDto;
import it.univaq.disim.bpd.billpostingapi.dto.DecisionResponseDto;
import it.univaq.disim.bpd.billpostingapi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billposting")
public class BillpostingController {

    private final RestTemplate restTemplate;

    @Value("${camunda.rest.url:http://localhost:8080/engine-rest}")
    private String camundaRestUrl;

    @Value("${user.service.url:http://localhost:9080/user/}")
    private String userServiceUrl;

    public BillpostingController() {
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/request")
    public ResponseEntity<AvailabilityResponseDto> requestAvailability(@RequestBody AvailabilityRequestDto requestDto) {
        if (requestDto.getUsername() == null || requestDto.getUsername().isEmpty()) {
            throw new BusinessException("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (requestDto.getPosterFormat() == null || requestDto.getPosterFormat().trim().isEmpty()) {
            throw new BusinessException("Poster format cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (requestDto.getCityBudgets() == null || requestDto.getCityBudgets().isEmpty()) {
            throw new BusinessException("At least one city with a budget must be provided", HttpStatus.BAD_REQUEST);
        }
        for (CityBudgetDto cityBudget : requestDto.getCityBudgets()) {
            if (cityBudget.getCity() == null || cityBudget.getCity().trim().isEmpty()) {
                throw new BusinessException("City name cannot be empty in budget definitions", HttpStatus.BAD_REQUEST);
            }
            if (cityBudget.getMaxBudget() < 0) {
                throw new BusinessException("City max budget cannot be negative", HttpStatus.BAD_REQUEST);
            }
        }

        String strategy = requestDto.getStrategy();
        if (strategy == null || strategy.isEmpty()) {
            strategy = "greedy";
        }
        if (!strategy.equalsIgnoreCase("greedy") && !strategy.equalsIgnoreCase("cheapest")) {
            throw new BusinessException("Strategy must be either 'greedy' or 'cheapest'", HttpStatus.BAD_REQUEST);
        }

        // Validate user existence
        try {
            org.springframework.http.ResponseEntity<String> userResponse = restTemplate.getForEntity(userServiceUrl + requestDto.getUsername(), String.class);
            if (userResponse.getBody() == null || userResponse.getBody().trim().isEmpty()) {
                throw new BusinessException("User not found: " + requestDto.getUsername(), HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BusinessException("User not found: " + requestDto.getUsername(), HttpStatus.NOT_FOUND);
            }
            throw new BusinessException("Error validating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error validating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String cityBudgetsJson;
            try {
                cityBudgetsJson = mapper.writeValueAsString(requestDto.getCityBudgets());
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new BusinessException("Failed to serialize cityBudgets", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Prepare payload for Camunda Start Process
            Map<String, Object> payload = new HashMap<>();
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("username", createVariable(requestDto.getUsername(), "String"));
            variables.put("posterFormat", createVariable(requestDto.getPosterFormat(), "String"));
            variables.put("strategy", createVariable(strategy.toLowerCase(), "String"));
            // We pass the cityBudgets as JSON string to easily parse it in the delegate
            variables.put("cityBudgets", createVariable(cityBudgetsJson, "String"));
            
            payload.put("variables", variables);
            payload.put("withVariablesInReturn", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // Start Process
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    camundaRestUrl + "/process-definition/key/PublicBillpostingProcess/start",
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("variables")) {
                throw new BusinessException("Failed to start process or variables not returned", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map<String, Object> returnedVariables = (Map<String, Object>) responseBody.get("variables");

            AvailabilityResponseDto responseDto = new AvailabilityResponseDto();
            
            // Extract requestId
            if (returnedVariables.containsKey("requestId")) {
                Map<String, Object> reqIdVar = (Map<String, Object>) returnedVariables.get("requestId");
                responseDto.setRequestId((String) reqIdVar.get("value"));
            }

            // Extract selectedZones
            if (returnedVariables.containsKey("selectedZones")) {
                Map<String, Object> zonesVar = (Map<String, Object>) returnedVariables.get("selectedZones");
                Object selectedZonesObj = zonesVar.get("value");
                if (selectedZonesObj instanceof String) {
                    try {
                        responseDto.setSelectedZones(mapper.readValue((String) selectedZonesObj, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}));
                    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                        throw new BusinessException("Failed to parse selectedZones JSON", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    responseDto.setSelectedZones(mapper.convertValue(selectedZonesObj, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}));
                }
            }

            // Extract totalPrice
            if (returnedVariables.containsKey("totalPrice")) {
                Map<String, Object> priceVar = (Map<String, Object>) returnedVariables.get("totalPrice");
                Object val = priceVar.get("value");
                if (val instanceof Number) {
                    responseDto.setTotalPrice(((Number) val).doubleValue());
                } else if (val instanceof String) {
                    responseDto.setTotalPrice(Double.parseDouble((String) val));
                }
            }

            return ResponseEntity.ok(responseDto);

        } catch (HttpClientErrorException e) {
            throw new BusinessException("Error communicating with Camunda Engine: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decision")
    public ResponseEntity<DecisionResponseDto> sendDecision(@RequestBody DecisionRequestDto requestDto) {
        if (requestDto.getRequestId() == null || requestDto.getRequestId().isEmpty()) {
            throw new BusinessException("Request ID cannot be empty", HttpStatus.BAD_REQUEST);
        }
        String decision = requestDto.getDecision();
        if (decision == null || (!decision.equalsIgnoreCase("confirm") && !decision.equalsIgnoreCase("cancel"))) {
            throw new BusinessException("Decision must be 'confirm' or 'cancel'", HttpStatus.BAD_REQUEST);
        }

        try {
            // Prepare payload for Camunda Message Correlation
            Map<String, Object> payload = new HashMap<>();
            payload.put("messageName", "decisionMessage");
            // We use requestId as correlation key, not business key since it is generated inside the process
            
            
            Map<String, Object> correlationKeys = new HashMap<>();
            correlationKeys.put("requestId", createVariable(requestDto.getRequestId(), "String"));
            payload.put("correlationKeys", correlationKeys);

            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("decision", createVariable(decision.toLowerCase(), "String"));
            payload.put("processVariables", processVariables);
            
            payload.put("resultEnabled", true);
            payload.put("variablesInResultEnabled", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // Correlate Message
            ResponseEntity<List> response = restTemplate.postForEntity(
                    camundaRestUrl + "/message",
                    entity,
                    List.class
            );

            List results = response.getBody();
            if (results == null || results.isEmpty()) {
                throw new BusinessException("Failed to correlate message for Request ID: " + requestDto.getRequestId(), HttpStatus.NOT_FOUND);
            }

            Map<String, Object> result = (Map<String, Object>) results.get(0);
            if (!result.containsKey("variables")) {
                throw new BusinessException("Process finished but no variables returned", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map<String, Object> returnedVariables = (Map<String, Object>) result.get("variables");
            
            DecisionResponseDto responseDto = new DecisionResponseDto();
            
            if ("cancel".equalsIgnoreCase(decision)) {
                // Empty bill
                responseDto.setStatus("Canceled");
                return ResponseEntity.ok(responseDto);
            }

            // Confirm branch, extract billing info
            responseDto.setStatus("Confirmed");
            
            if (returnedVariables.containsKey("accountHolder")) {
                Map<String, Object> var = (Map<String, Object>) returnedVariables.get("accountHolder");
                responseDto.setAccountHolder((String) var.get("value"));
            }
            if (returnedVariables.containsKey("invoiceNumber")) {
                Map<String, Object> var = (Map<String, Object>) returnedVariables.get("invoiceNumber");
                responseDto.setInvoiceNumber((String) var.get("value"));
            }
            if (returnedVariables.containsKey("amountDue")) {
                Map<String, Object> var = (Map<String, Object>) returnedVariables.get("amountDue");
                Object val = var.get("value");
                if (val instanceof Number) {
                    responseDto.setAmountDue(((Number) val).doubleValue());
                } else if (val instanceof String) {
                    responseDto.setAmountDue(Double.parseDouble((String) val));
                }
            }

            return ResponseEntity.ok(responseDto);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                 throw new BusinessException("Process instance not found or already completed for Request ID: " + requestDto.getRequestId(), HttpStatus.NOT_FOUND);
            }
            throw new BusinessException("Error communicating with Camunda Engine: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> createVariable(Object value, String type) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", value);
        if (type != null) {
            var.put("type", type);
        }
        if ("Object".equals(type)) {
            Map<String, Object> valueInfo = new HashMap<>();
            valueInfo.put("serializationDataFormat", "application/json");
            var.put("valueInfo", valueInfo);
        }
        return var;
    }
}
