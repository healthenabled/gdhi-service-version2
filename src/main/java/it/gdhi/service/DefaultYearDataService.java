package it.gdhi.service;

import it.gdhi.model.DefaultYearData;
import it.gdhi.repository.IDefaultYearData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

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
        DefaultYearData year = DefaultYearData.builder().year(Year).build();
        iDefaultYearData.save(year);
    }
}
