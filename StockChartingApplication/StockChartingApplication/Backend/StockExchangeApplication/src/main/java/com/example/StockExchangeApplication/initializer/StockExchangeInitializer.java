package com.example.StockExchangeApplication.initializer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.StockExchangeApplication.dao.StockExchangeRepository;
import com.example.StockExchangeApplication.model.StockExchange;

@Component
public class StockExchangeInitializer implements CommandLineRunner
{
    @Autowired
    private StockExchangeRepository stockExchangeRepository;

    @Override
    public void run(String... args) throws Exception
    {
        stockExchangeRepository.deleteAll();
        StockExchange bse =new StockExchange("BSE", "Bombay Stock Exchange", "Dalal Street, Mumbai, India", "World's 10th largest stock-exchange");
        stockExchangeRepository.save(bse);
        StockExchange nse =new StockExchange("NSE", "National Stock Exchange", "Mumbai, India", "India's 4th largest stock-exchange");
        stockExchangeRepository.save(nse);
    }

}
