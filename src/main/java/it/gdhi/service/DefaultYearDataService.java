package it.gdhi.service;

import it.gdhi.model.DefaultYearData;
import it.gdhi.repository.IDefaultYearData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class DefaultYearDataService {

    private IDefaultYearData iDefaultYearData;

    @Autowired
    public DefaultYearDataService(IDefaultYearData iDefaultYearData) {
        this.iDefaultYearData = iDefaultYearData;
    }

    public List<String> fetchYears() {
         List<DefaultYearData> years = iDefaultYearData.findAll();
         return years.stream().map(DefaultYearData::getYear).collect(Collectors.toList());
    }

    public void saveNewYear(String Year) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DefaultYearData year = DefaultYearData.builder().year(Year).createdAt(timestamp).build();
        iDefaultYearData.save(year);
    }

    public String fetchDefaultYear() {
        DefaultYearData defaultYearData = iDefaultYearData.findFirstByOrderByCreatedAtDesc();
        return defaultYearData.getYear();
    }
}
