package it.gdhi.controller;

import it.gdhi.model.Region;
import it.gdhi.service.RegionService;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/regions")
    public List<Region> fetchRegions(HttpServletRequest request){
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return regionService.fetchRegions(languageCode);
    }
}
