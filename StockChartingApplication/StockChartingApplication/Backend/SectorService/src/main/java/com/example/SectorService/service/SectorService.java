package com.example.SectorService.service;


import java.util.List;

import com.example.SectorService.dto.CompanyDto;
import com.example.SectorService.dto.SectorDto;

public interface SectorService
{
    public SectorDto save(SectorDto sectorDto);
    public List<SectorDto> findAll() ;
    public SectorDto findById(String id);
    public void deleteById(String id);
    public List<CompanyDto> getCompanies(String id);
    public SectorDto addCompanyToSector(String sectorName, CompanyDto companyDto);
}