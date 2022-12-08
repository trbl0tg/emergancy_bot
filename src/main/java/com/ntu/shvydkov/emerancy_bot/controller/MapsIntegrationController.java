package com.ntu.shvydkov.emerancy_bot.controller;

import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/integration")
public class MapsIntegrationController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public void sendRequestToMapsApp() {
        List<Report> all = reportService.findAll();

        List<ReportEnvelopItem> requestBody = all.stream()
                .filter(item -> item.getTextLocation() == null)
                .map(item -> new ReportEnvelopItem(
                        item.getUser().getUsername(),
                        item.getLocation().getLatitude(),
                        item.getLocation().getLongitude(),
                        item.getDangerLevel().toString(),
                        item.getCreated().toString()
                )).collect(Collectors.toList());

        final String uri = "http://localhost:8080/integration";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, new ReportEnvelop(requestBody), String.class);
        //send this shit to maps
        System.out.println(result);
    }

}
