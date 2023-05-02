package it.gdhi.controller;

import it.gdhi.dto.RegionCountriesDto;
import it.gdhi.model.Region;
import it.gdhi.service.RegionService;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/regions")
    public List<Region> fetchRegions(HttpServletRequest request) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return regionService.fetchRegions(languageCode);
    }

    @GetMapping("/region/{id}")
    @ResponseBody
    public RegionCountriesDto fetchRegionCountriesData(HttpServletRequest request,
                                                       @PathVariable("id") String regionId,
                                                       @RequestParam(value = "list_of_years") List<String> years) {
        if (years.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return regionService.getRegionCountriesData(regionId, years, languageCode);
    }

    @GetMapping("/region/{region_id}/get_years")
    public List<String> getYearsForARegion(@PathVariable("region_id") String regionId,
                                           @RequestParam(value = "limit", required = false) Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        return regionService.fetchYearsForARegion(regionId, limit);
    }
}
