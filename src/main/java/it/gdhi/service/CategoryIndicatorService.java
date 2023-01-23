package it.gdhi.service;

import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.Category;
import it.gdhi.repository.ICategoryRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;


@Service
public class CategoryIndicatorService {

    private final ICategoryRepository iCategoryRepository;
    private final HealthIndicatorTranslator translator;

    @Autowired
    public CategoryIndicatorService(ICategoryRepository iCategoryRepository,
                                    HealthIndicatorTranslator translator) {
        this.iCategoryRepository = iCategoryRepository;
        this.translator = translator;
    }

    public List<CategoryIndicatorDto> getHealthIndicatorOptions(LanguageCode languageCode) {
        return iCategoryRepository.findAll().stream()
                                    .map(CategoryIndicatorDto::new)
                                    .map(dto -> translator.translateHealthIndicatorOptions(dto, languageCode))
                                    .collect(toList());
    }

    Integer getHealthIndicatorCount() {
        List<Category> categoryList = iCategoryRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);

        categoryList.stream().forEach(category -> count.addAndGet(category.getIndicators().size()));
        return count.get();
    }
}