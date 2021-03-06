package com.example.StockPrice.service.impl;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.StockPrice.dao.StockPriceRepository;
import com.example.StockPrice.dto.CompanyCompareRequest;
import com.example.StockPrice.dto.CompanyDto;
import com.example.StockPrice.dto.SectorCompareRequest;
import com.example.StockPrice.dto.StockPriceDto;
import com.example.StockPrice.feignclient.SectorClient;
import com.example.StockPrice.mapper.StockPriceMapper;
import com.example.StockPrice.model.StockPrice;
import com.example.StockPrice.service.StockPriceService;


@Service
public class StockPriceServiceImpl implements StockPriceService{
    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Autowired
    private StockPriceMapper stockPriceMapper;

    @Autowired
    private SectorClient sectorClient;

    public List<StockPriceDto> findAll() {
        List<StockPrice> stockPrices = stockPriceRepository.findAll();
        return stockPriceMapper.toStockPriceDtos(stockPrices);
    }

    public StockPriceDto findById(String id) {
        Optional<StockPrice> stockPrice = stockPriceRepository.findById(id);
        if(!stockPrice.isPresent())
            return null;
        return stockPriceMapper.toStockPriceDto(stockPrice.get());
    }

    public List<StockPriceDto> save(List<StockPriceDto> stockPriceDtos)
    {
        List<StockPrice> stockPrices = stockPriceMapper.toStockPrices(stockPriceDtos);
        stockPrices = stockPriceRepository.saveAll(stockPrices);
        return stockPriceMapper.toStockPriceDtos(stockPrices);
    }

    public StockPriceDto update(StockPriceDto stockPriceDto)
    {
        if(findById(stockPriceDto.getId()) == null)
            return null;
        StockPrice stockPrice = stockPriceRepository.save(stockPriceMapper.toStockPrice(stockPriceDto));
        return stockPriceMapper.toStockPriceDto(stockPrice);
    }

    public void deleteById(String id) {
        stockPriceRepository.deleteById(id);
    }

    @Override
    public List<StockPriceDto> getStockPricesForCompanyComparison(CompanyCompareRequest compareRequest)
            throws ParseException
    {
        Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(compareRequest.getFromPeriod());
        Date toDate = new SimpleDateFormat("dd-MM-yyyy").parse(compareRequest.getToPeriod());
        List<StockPrice> stockPrices = stockPriceRepository
                .findByCompanyCodeAndStockExchangeName(compareRequest.getCompanyCode(), compareRequest.getStockExchangeName());
        List<StockPrice> filteredList = stockPrices.stream()
                .filter(stockPrice -> {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd-MM-yyyy").parse(stockPrice.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date.after(fromDate) && date.before(toDate);
                })
                .collect(Collectors.toList());
        return stockPriceMapper.toStockPriceDtos(filteredList);
    }

    @Override
    public List<StockPriceDto> getStockPricesForSectorComparison(SectorCompareRequest compareRequest)
            throws ParseException
    {
        Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(compareRequest.getFromPeriod());
        Date toDate = new SimpleDateFormat("dd-MM-yyyy").parse(compareRequest.getToPeriod());
        List<StockPrice> stockPricesForSector = new ArrayList<>();
        for(CompanyDto companyDto: sectorClient.getSectorCompanies(compareRequest.getSectorName()))
        {
            List<StockPrice> stockPrices = stockPriceRepository
                    .findByCompanyCodeAndStockExchangeName(companyDto.getCode(), compareRequest.getStockExchangeName());
            List<StockPrice> filteredList = stockPrices.stream()
                    .filter(stockPrice -> {
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd-MM-yyyy").parse(stockPrice.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return date.after(fromDate) && date.before(toDate);
                    })
                    .collect(Collectors.toList());
            stockPricesForSector.addAll(filteredList);
        }
        return stockPriceMapper.toStockPriceDtos(stockPricesForSector);
    }
}