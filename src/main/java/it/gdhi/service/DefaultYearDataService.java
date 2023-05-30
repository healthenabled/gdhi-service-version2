package it.gdhi.service;

import it.gdhi.model.DefaultYearData;
import it.gdhi.repository.IDefaultYearData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultYearDataService {

    private final IDefaultYearData iDefaultYearData;

    @Autowired
    public DefaultYearDataService(IDefaultYearData iDefaultYearData) {
        this.iDefaultYearData = iDefaultYearData;
    }

    public List<String> fetchYears() {
        List<DefaultYearData> defaultYears = iDefaultYearData.findAll();
        return defaultYears.stream().map(DefaultYearData::getYear).collect(Collectors.toList());
    }

    public void saveDefaultYear(String year) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DefaultYearData defaultYearData = DefaultYearData.builder().year(year).createdAt(timestamp).build();
        if(fetchDefaultYear() == null || !fetchDefaultYear().equals(year)) {
            iDefaultYearData.save(defaultYearData);
        }
    }

    public String fetchDefaultYear() {
        DefaultYearData defaultYearData = iDefaultYearData.findFirstByOrderByCreatedAtDesc();
        return defaultYearData != null ? defaultYearData.getYear() : null;
    }
}
