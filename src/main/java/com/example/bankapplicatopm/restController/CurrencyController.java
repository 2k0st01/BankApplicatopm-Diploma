package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.currency.CurrencyChangeDTO;
import com.example.bankapplicatopm.dto.currency.CurrencyDTO;
import com.example.bankapplicatopm.model.CurrencyRate;
import com.example.bankapplicatopm.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/api/currency/rate")
    public List<CurrencyDTO> getCurrencyRate() {
        List<CurrencyRate> list = currencyService.getAllCurrency();
        List<CurrencyDTO> main = list.stream()
                .map(t -> {
                    CurrencyDTO dto = new CurrencyDTO();
                    dto.setCc(t.getCc());
                    dto.setRate(t.getRate());
                    return dto;
                })
                .collect(Collectors.toList());
        return main;
    }

    @PostMapping("/api/currency/change")
    public void changeCurrency(@RequestBody CurrencyChangeDTO currencyChangeDTO){

        currencyService.changeCurrency(currencyChangeDTO.getFrom(),currencyChangeDTO.getTo(),currencyChangeDTO.getSum());
    }
}
