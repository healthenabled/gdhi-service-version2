package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_14__Insert_i18_Country_ar   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AZE', 'ar', 'أذربيجان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARM', 'ar', 'أرمينيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ERI', 'ar', 'إريتريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ESP', 'ar', 'إسبانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUS', 'ar', 'أستراليا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EST', 'ar', 'إستونيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISR', 'ar', 'إسرائيل'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWZ', 'ar', 'إسواتيني'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARG', 'ar', 'الأرجنتين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ECU', 'ar', 'الإكوادور'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARE', 'ar', 'الإمارات العربية المتحدة'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ALB', 'ar', 'ألبانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHR', 'ar', 'البحرين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRA', 'ar', 'البرازيل'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BIH', 'ar', 'البوسنة والهرسك'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CZE', 'ar', 'التشيك'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNE', 'ar', 'الجبل الأسود'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DZA', 'ar', 'الجزائر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DNK', 'ar', 'الدانمرك'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CPV', 'ar', 'الرأس الأخضر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLV', 'ar', 'السلفادور'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SEN', 'ar', 'السنغال'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SDN', 'ar', 'السودان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWE', 'ar', 'السويد'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SOM', 'ar', 'الصومال'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHN', 'ar', 'الصين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRQ', 'ar', 'العراق'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GAB', 'ar', 'الغابون'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CMR', 'ar', 'الكاميرون'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COG', 'ar', 'الكونغو - برازافيل'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COD', 'ar', 'الكونغو - كينشاسا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DEU', 'ar', 'ألمانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MAR', 'ar', 'المغرب'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MEX', 'ar', 'المكسيك'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SAU', 'ar', 'المملكة العربية السعودية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GBR', 'ar', 'المملكة المتحدة'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NOR', 'ar', 'النرويج'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUT', 'ar', 'النمسا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NER', 'ar', 'النيجر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IND', 'ar', 'الهند'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('USA', 'ar', 'الولايات المتحدة'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JPN', 'ar', 'اليابان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('YEM', 'ar', 'اليمن'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRC', 'ar', 'اليونان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ATG', 'ar', 'أنتيغوا وبربودا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AND', 'ar', 'أندورا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AGO', 'ar', 'أنغولا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('URY', 'ar', 'أورغواي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UZB', 'ar', 'أوزبكستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UKR', 'ar', 'أوكرانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRN', 'ar', 'إيران'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRL', 'ar', 'أيرلندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ITA', 'ar', 'إيطاليا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PNG', 'ar', 'بابوا غينيا الجديدة'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRY', 'ar', 'باراغواي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PLW', 'ar', 'بالاو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRB', 'ar', 'بربادوس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRN', 'ar', 'بروناي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEL', 'ar', 'بلجيكا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGR', 'ar', 'بلغاريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLZ', 'ar', 'بليز'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAN', 'ar', 'بنما'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BTN', 'ar', 'بوتان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BWA', 'ar', 'بوتسوانا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BFA', 'ar', 'بوركينا فاسو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BDI', 'ar', 'بوروندي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('POL', 'ar', 'بولندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BOL', 'ar', 'بوليفيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLR', 'ar', 'بيلاروس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TKM', 'ar', 'تركمانستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUR', 'ar', 'تركيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TTO', 'ar', 'ترينيداد وتوباغو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TCD', 'ar', 'تشاد'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TZA', 'ar', 'تنزانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TGO', 'ar', 'توغو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUV', 'ar', 'توفالو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUN', 'ar', 'تونس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TON', 'ar', 'تونغا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TLS', 'ar', 'تيمور - ليشتي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JAM', 'ar', 'جامايكا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHS', 'ar', 'جزر البهاما'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COM', 'ar', 'جزر القمر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDV', 'ar', 'جزر المالديف'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLB', 'ar', 'جزر سليمان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COK', 'ar', 'جزر كوك'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MHL', 'ar', 'جزر مارشال'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAF', 'ar', 'جمهورية أفريقيا الوسطى'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DOM', 'ar', 'جمهورية الدومينيكان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZAF', 'ar', 'جنوب أفريقيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SSD', 'ar', 'جنوب السودان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GEO', 'ar', 'جورجيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DJI', 'ar', 'جيبوتي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DMA', 'ar', 'دومينيكا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RWA', 'ar', 'رواندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RUS', 'ar', 'روسيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ROU', 'ar', 'رومانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZMB', 'ar', 'زامبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZWE', 'ar', 'زيمبابوي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CIV', 'ar', 'ساحل العاج'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('WSM', 'ar', 'ساموا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SMR', 'ar', 'سان مارينو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VCT', 'ar', 'سانت فنسنت وجزر غرينادين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KNA', 'ar', 'سانت كيتس ونيفيس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LCA', 'ar', 'سانت لوسيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('STP', 'ar', 'ساو تومي وبرينسيبي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVK', 'ar', 'سلوفاكيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVN', 'ar', 'سلوفينيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SGP', 'ar', 'سنغافورة'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYR', 'ar', 'سوريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SUR', 'ar', 'سورينام'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHE', 'ar', 'سويسرا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYC', 'ar', 'سيشل'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SRB', 'ar', 'صربيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TJK', 'ar', 'طاجيكستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('OMN', 'ar', 'عُمان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GMB', 'ar', 'غامبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GHA', 'ar', 'غانا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRD', 'ar', 'غرينادا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GTM', 'ar', 'غواتيمالا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GUY', 'ar', 'غيانا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GIN', 'ar', 'غينيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNQ', 'ar', 'غينيا الاستوائية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNB', 'ar', 'غينيا بيساو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VUT', 'ar', 'فانواتو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FRA', 'ar', 'فرنسا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VEN', 'ar', 'فنزويلا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FIN', 'ar', 'فنلندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VNM', 'ar', 'فيتنام'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FJI', 'ar', 'فيجي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CYP', 'ar', 'قبرص'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('QAT', 'ar', 'قطر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KGZ', 'ar', 'قيرغيزستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KAZ', 'ar', 'كازاخستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HRV', 'ar', 'كرواتيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KHM', 'ar', 'كمبوديا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAN', 'ar', 'كندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CUB', 'ar', 'كوبا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KOR', 'ar', 'كوريا الجنوبية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRK', 'ar', 'كوريا الشمالية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CRI', 'ar', 'كوستاريكا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COL', 'ar', 'كولومبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KIR', 'ar', 'كيريباتي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KEN', 'ar', 'كينيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LVA', 'ar', 'لاتفيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBN', 'ar', 'لبنان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LUX', 'ar', 'لوكسمبورغ'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBY', 'ar', 'ليبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBR', 'ar', 'ليبيريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LTU', 'ar', 'ليتوانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LSO', 'ar', 'ليسوتو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLT', 'ar', 'مالطا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDG', 'ar', 'مدغشقر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EGY', 'ar', 'مصر'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MKD', 'ar', 'مقدونيا الشمالية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MWI', 'ar', 'ملاوي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MRT', 'ar', 'موريتانيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MUS', 'ar', 'موريشيوس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MOZ', 'ar', 'موزمبيق'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDA', 'ar', 'مولدوفا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MCO', 'ar', 'موناكو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MMR', 'ar', 'ميانمار (بورما)'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FSM', 'ar', 'ميكرونيزيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NAM', 'ar', 'ناميبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NRU', 'ar', 'ناورو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NPL', 'ar', 'نيبال'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIC', 'ar', 'نيكاراغوا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIU', 'ar', 'نيوي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HTI', 'ar', 'هايتي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HND', 'ar', 'هندوراس'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HUN', 'ar', 'هنغاريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NLD', 'ar', 'هولندا'); "

        );
    }
}
