package com.example.bankapplicatopm.componet.GetRate;

import com.example.bankapplicatopm.dto.currency.CurrencyDTO;
import com.example.bankapplicatopm.enums.Currency;
import com.example.bankapplicatopm.model.CurrencyRate;
import com.example.bankapplicatopm.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class RateGetter {
    private final CurrencyService currencyService;
    public final String LINK = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json";

    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 360_000_0) //every 60 min not confirmed tokens will be delete
    public void fetchData() {
        CurrencyDTO[] response = restTemplate.getForObject(LINK, CurrencyDTO[].class);
        JSONArray jsonArray = new JSONArray(response);
        jsonArray.forEach(item -> {
            for(Currency c: Currency.values()){
                for(CurrencyDTO cd: response){
                    if(cd.getCc().equals(c.toString())){

                        CurrencyRate currencyRate = currencyService.findCurrencyModelByCc(cd.getCc());
                        if(currencyRate == null){
                            currencyRate = new CurrencyRate();
                            currencyRate.setRate(cd.getRate());
                            currencyRate.setCc(cd.getCc());
                            currencyService.save(currencyRate);
                        } else {
                            currencyRate.setRate(cd.getRate());
                            currencyService.save(currencyRate);
                        }

                    }
                }
            }
        });
    }

}
