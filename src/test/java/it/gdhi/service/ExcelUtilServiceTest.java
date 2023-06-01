package it.gdhi.service;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.IndicatorScoreDto;
import it.gdhi.internationalization.model.CategoryTranslation;
import it.gdhi.internationalization.model.CategoryTranslationId;
import it.gdhi.internationalization.model.HealthIndicatorTranslationId;
import it.gdhi.internationalization.model.IndicatorTranslation;
import it.gdhi.internationalization.repository.ICategoryTranslationRepository;
import it.gdhi.internationalization.repository.IIndicatorTranslationRepository;
import it.gdhi.model.Category;
import it.gdhi.model.Indicator;
import it.gdhi.repository.ICategoryRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.service.ExcelUtilService.*;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExcelUtilServiceTest {

    @InjectMocks
    @Spy
    private ExcelUtilService excelUtilService;
    @Mock
    private ICategoryRepository iCategoryRepository;
    @Mock
    private ICategoryTranslationRepository categoryTranslationRepository;
    @Mock
    private IIndicatorTranslationRepository indicatorTranslationRepository;

    private String testFilePath = "/tmp/Digital Health Data.xlsx";


    @Test
    public void shouldVerifyFileIsDownloaded() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletContext servletContext = mock(ServletContext.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);
        when(request.getServletContext()).thenReturn(servletContext);

        excelUtilService.downloadFile(request, response,testFilePath);

        verify(request).getServletContext();
        verify(response).setContentType(MIME_TYPE);
        verify(response).setHeader(HEADER_KEY, "attachment; filename=Digital Health Data.xlsx");
        verify(outputStream).close();
    }

    @Test
    public void shouldPopulateHeaderDefinitionAndNotHealthScoresWhenEmptyCountryScoresPresent() throws IOException {
        List<CountryHealthScoreDto> countryHealthScores = new ArrayList<>();
        List<Indicator> indicators = singletonList(new Indicator(1, "Ind 1", "Ind Def 1", 1));
        List<Category> categories = singletonList(new Category(1, "Cat 1",
                indicators));
        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);

        CategoryTranslation categoryTranslation = new CategoryTranslation(new CategoryTranslationId(1, fr), "Cat 1", categories.get(0));
        IndicatorTranslation indicatorTranslation = new IndicatorTranslation(new HealthIndicatorTranslationId(1, fr), "Ind 1", "Ind Def 1", indicators.get(0));
        when(categoryTranslationRepository.findByLanguageId(en)).thenReturn(of(categoryTranslation));
        when(indicatorTranslationRepository.findByLanguageId(en)).thenReturn(of(indicatorTranslation));

        excelUtilService.convertListToExcel(countryHealthScores, en,testFilePath);

        ArgumentCaptor<XSSFSheet> sheetArgumentCaptor = ArgumentCaptor.forClass(XSSFSheet.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);

        verify(excelUtilService).populateHeaderNames(any(), sheetArgumentCaptor.capture(), eq(0), eq(categories));
        verify(excelUtilService).populateHeaderDefinitions(any(), sheetArgumentCaptor.capture(), eq(1), eq(categories));
        verify(excelUtilService, times(0)).populateHealthIndicatorsWithDefinitionsAndScores(sheetArgumentCaptor.capture(),
                eq(countryHealthScores), mapArgumentCaptor.capture(), eq(2));
        XSSFSheet sheet = sheetArgumentCaptor.getValue();
        assertThat(sheet.getSheetName(), is("Global Health Data"));
    }

    @Test
    public void shouldPopulateHeaderDefinitionAndNotHealthScoresWhenNullCountryScoresPresent() throws IOException {
        List<CountryHealthScoreDto> countryHealthScores = null;
        List<Indicator> indicators = singletonList(new Indicator(1, "Ind 1", "Ind Def 1", 1));
        List<Category> categories = singletonList(new Category(1, "Cat 1",
                indicators));
        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);

        CategoryTranslation categoryTranslation = new CategoryTranslation(new CategoryTranslationId(1, fr), "Cat 1", categories.get(0));
        IndicatorTranslation indicatorTranslation = new IndicatorTranslation(new HealthIndicatorTranslationId(1, fr), "Ind 1", "Ind Def 1", indicators.get(0));
        when(categoryTranslationRepository.findByLanguageId(en)).thenReturn(of(categoryTranslation));
        when(indicatorTranslationRepository.findByLanguageId(en)).thenReturn(of(indicatorTranslation));

        excelUtilService.convertListToExcel(countryHealthScores, en, testFilePath);

        ArgumentCaptor<XSSFSheet> sheetArgumentCaptor = ArgumentCaptor.forClass(XSSFSheet.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(excelUtilService).populateHeaderNames(any(), sheetArgumentCaptor.capture(), eq(0), eq(categories));
        verify(excelUtilService).populateHeaderDefinitions(any(), sheetArgumentCaptor.capture(), eq(1), eq(categories));
        verify(excelUtilService, times(0)).populateHealthIndicatorsWithDefinitionsAndScores(sheetArgumentCaptor.capture(),
                eq(countryHealthScores), mapArgumentCaptor.capture(), eq(2));
        XSSFSheet sheet = sheetArgumentCaptor.getValue();
        assertThat(sheet.getSheetName(), is("Global Health Data"));
    }

    @Test
    public void shouldPopulateHeaderDefinitionAndHealthScoresWhenCountryScoresPresent() throws IOException {
        List<CountryHealthScoreDto> countryHealthScores = new ArrayList<>();
        countryHealthScores.add(new CountryHealthScoreDto("IND", "INDIA", "IN", emptyList(), 4,null));
        List<Indicator> indicators = singletonList(new Indicator(1, "Ind 1", "Ind Def 1", 1));
        List<Category> categories = singletonList(new Category(1, "Cat 1", indicators));

        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);

        CategoryTranslation categoryTranslation = new CategoryTranslation(new CategoryTranslationId(1, fr), "Cat 1", categories.get(0));
        IndicatorTranslation indicatorTranslation = new IndicatorTranslation(new HealthIndicatorTranslationId(1, fr), "Ind 1", "Ind Def 1", indicators.get(0));
        when(categoryTranslationRepository.findByLanguageId(en)).thenReturn(of(categoryTranslation));
        when(indicatorTranslationRepository.findByLanguageId(en)).thenReturn(of(indicatorTranslation));

        excelUtilService.convertListToExcel(countryHealthScores, en, testFilePath);

        ArgumentCaptor<XSSFSheet> sheetArgumentCaptor = ArgumentCaptor.forClass(XSSFSheet.class);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(excelUtilService).populateHeaderNames(any(), sheetArgumentCaptor.capture(), eq(0), eq(categories));
        verify(excelUtilService).populateHeaderDefinitions(any(), sheetArgumentCaptor.capture(), eq(1), eq(categories));
        verify(excelUtilService).populateHealthIndicatorsWithDefinitionsAndScores(sheetArgumentCaptor.capture(),
                eq(countryHealthScores), mapArgumentCaptor.capture(), eq(2));
        XSSFSheet sheet = sheetArgumentCaptor.getValue();
        Set<String> keys = mapArgumentCaptor.getValue().keySet();
        List<String> expectedKeys = asList("Country Name", "Indicator 1", "Category 1", "Overall Phase");
        List<String> expectedValues = asList("Country Name", "Ind 1", "Cat 1", "Overall Phase");
        assertTrue(keys.containsAll(expectedKeys));
        Collection<String> values =  mapArgumentCaptor.getValue().values();
        assertTrue(values.containsAll(expectedValues));
        assertThat(sheet.getSheetName(), is("Global Health Data"));
    }

    @Test
    public void shouldPopulateHealthScoresToBeExported() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(WORKSHEET_NAME);
        List<CountryHealthScoreDto> countryHealthScores = new ArrayList<>();
        IndicatorScoreDto indicatorScoreDto = new IndicatorScoreDto(1, "1", "Ind 1", "Ind Def 1", 1, 4, "S", "S1");
        CategoryHealthScoreDto categoryHealthScoreDto = new CategoryHealthScoreDto(1, "Cat 1", 4.0, 4,  asList(indicatorScoreDto));
        countryHealthScores.add(new CountryHealthScoreDto("IND", "INDIA", "IN",
                asList(categoryHealthScoreDto),
                4,null));
        Map<String, String> headerDef = new LinkedHashMap<>();
        headerDef.put("Country Name", "Country Name");
        headerDef.put("Indicator 1", "Ind 1");
        headerDef.put("Category 1", "Cat 1");
        headerDef.put("Overall Phase", "Overall Phase");

        excelUtilService.populateHealthIndicatorsWithDefinitionsAndScores(sheet, countryHealthScores, headerDef, 3);

        ArgumentCaptor<Map> contentMap = ArgumentCaptor.forClass(Map.class);
        verify(excelUtilService).addRow(contentMap.capture(), any(Row.class), any());
        Map actualMap = contentMap.getValue();
        Object[] keys = actualMap.keySet().toArray();
        Object[] values = actualMap.values().toArray();

        assertThat(keys[0], is("Country Name"));
        assertThat(keys[1], is("Indicator 1"));
        assertThat(keys[2], is("Category 1"));
        assertThat(keys[3], is("Overall Phase"));

        assertThat(values[0], is("INDIA"));
        assertThat(values[1], is("Phase 4"));
        assertThat(values[2], is("Phase 4"));
        assertThat(values[3], is("Phase 4"));
    }

    @Test
    public void shouldPopulateHeaderDefinitionAndHealthScoresInFrench() throws IOException {
        List<CountryHealthScoreDto> countryHealthScores = new ArrayList<>();
        countryHealthScores.add(new CountryHealthScoreDto("IND", "INDIA", "IN", emptyList(), 4,null));
        List<Indicator> indicators = singletonList(new Indicator(1, "Ind 1", "Ind Def 1", 1));
        List<Category> categories = singletonList(new Category(1, "Cat 1", indicators));

        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);

        CategoryTranslation categoryTranslation = new CategoryTranslation(new CategoryTranslationId(1, fr), "Cat 1 fr", categories.get(0));
        IndicatorTranslation indicatorTranslation = new IndicatorTranslation(new HealthIndicatorTranslationId(1, fr), "Ind 1 fr", "Ind Def 1 fr", indicators.get(0));
        when(categoryTranslationRepository.findByLanguageId(fr)).thenReturn(of(categoryTranslation));
        when(indicatorTranslationRepository.findByLanguageId(fr)).thenReturn(of(indicatorTranslation));

        excelUtilService.convertListToExcel(countryHealthScores, fr, testFilePath);

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(excelUtilService).populateHealthIndicatorsWithDefinitionsAndScores(any(), eq(countryHealthScores), mapArgumentCaptor.capture(), eq(2));

        Set<String> keys = mapArgumentCaptor.getValue().keySet();
        Collection<String> values =  mapArgumentCaptor.getValue().values();
        List<String> expectedKeys = asList("Country Name", "Indicator 1", "Category 1", "Overall Phase");
        List<String> expectedValues = asList("Nom du pays", "Ind 1 fr", "Cat 1 fr", "Dans l'ensemble Phase");
        assertTrue(keys.containsAll(expectedKeys));
        assertTrue(values.containsAll(expectedValues));
    }
}