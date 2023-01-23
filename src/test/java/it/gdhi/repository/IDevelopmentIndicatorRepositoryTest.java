package it.gdhi.repository;

import it.gdhi.model.DevelopmentIndicator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class IDevelopmentIndicatorRepositoryTest {

    @Autowired
    private IDevelopmentIndicatorRepository iDevelopmentIndicatorRepository;

    @Test
    @Disabled
    public void shouldFetchPopulationGivenCountryCode() {
        DevelopmentIndicator developmentIndicator = iDevelopmentIndicatorRepository.findByCountryId("NZL").orElse(null);
        assertThat(developmentIndicator.getTotalPopulation(), is(4692700L));
    }

}