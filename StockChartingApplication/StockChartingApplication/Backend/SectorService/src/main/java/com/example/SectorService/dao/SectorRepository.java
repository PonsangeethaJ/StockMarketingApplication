package com.example.SectorService.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.SectorService.model.Sector;

@Repository
public interface SectorRepository extends MongoRepository<Sector, String>
{
    public Sector findByName(String sectorName);
}
