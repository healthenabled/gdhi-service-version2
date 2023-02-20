package it.gdhi.service;

import it.gdhi.repository.IDefaultYearData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultYearDataService {

    private IDefaultYearData iDefaultYearData;

    @Autowired
    public DefaultYearDataService(IDefaultYearData iDefaultYearData) {
        this.iDefaultYearData = iDefaultYearData;
    }

}
