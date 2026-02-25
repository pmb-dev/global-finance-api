package com.github.pmbdev.global_finance_api.service.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl {
    @Value("${api.exchangerate.url}")
    private String baseUrl;

    @Value("${api.exchangerate.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getRate(String from, String to) {
        try {
            String url = baseUrl + apiKey + "/pair/" + from + "/" + to;
            ExchangeResponse response = restTemplate.getForObject(url, ExchangeResponse.class);

            if (response != null && response.getConversion_rate() != null) {
                return response.getConversion_rate();
            }
        } catch (Exception e) {
            System.err.println("Error calling ExchangeRate API: " + e.getMessage());
        }
        return BigDecimal.ONE; // Fallback
    }

    @Data
    private static class ExchangeResponse {
        private BigDecimal conversion_rate;
    }
}
