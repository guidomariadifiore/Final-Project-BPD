package it.univaq.disim.bpd.service;

import it.univaq.disim.bpd.dto.CityBudgetDto;
import it.univaq.disim.bpd.dto.ZoneDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneSelectionService {

    private final RestTemplate restTemplate;

    public ZoneSelectionService() {
        this.restTemplate = new RestTemplate();
    }

    public List<ZoneDto> selectZones(String posterFormat, List<CityBudgetDto> cityBudgets) {
        String url = "http://localhost:9090/zones/" + posterFormat;
        
        // Fetch zones
        ResponseEntity<List<ZoneDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ZoneDto>>() {}
        );
        
        List<ZoneDto> allZones = response.getBody();
        if (allZones == null) {
            allZones = new ArrayList<>();
        }

        List<ZoneDto> selectedZones = new ArrayList<>();

        for (CityBudgetDto cityBudget : cityBudgets) {
            String city = cityBudget.getCity();
            double maxBudget = cityBudget.getMaxBudget();

            // Filter zones by city
            List<ZoneDto> cityZones = allZones.stream()
                    .filter(z -> z.getCity().equalsIgnoreCase(city))
                    .sorted(Comparator.comparing(ZoneDto::getPrice).reversed()) // Greedy: Most expensive first
                    .collect(Collectors.toList());

            double currentTotal = 0.0;

            for (ZoneDto zone : cityZones) {
                if (currentTotal + zone.getPrice() <= maxBudget) {
                    selectedZones.add(zone);
                    currentTotal += zone.getPrice();
                }
            }
        }

        return selectedZones;
    }
}
