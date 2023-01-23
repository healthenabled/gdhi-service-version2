package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_4__Insert_i18_Country   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(
                    "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AFG', 'es', 'Afganistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGD', 'es', 'Bangladesh'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEN', 'es', 'Benín'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHL', 'es', 'Chile'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ETH', 'es', 'Etiopía'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IDN', 'es', 'Indonesia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JOR', 'es', 'Jordania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KWT', 'es', 'Kuwait'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LAO', 'es', 'República Democrática Popular Lao'); "  +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MYS', 'es', 'Malasia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLI', 'es', 'Malí'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNG', 'es', 'Mongolia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NZL', 'es', 'Nueva Zelanda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NGA', 'es', 'Nigeria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAK', 'es', 'Pakistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PER', 'es', 'Perú'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PHL', 'es', 'Filipinas'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRT', 'es', 'Portugal'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLE', 'es', 'Sierra Leona'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LKA', 'es', 'Sri Lanka'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('THA', 'es', 'Tailandia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UGA', 'es', 'Uganda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AFG', 'fr', 'Afghanistan'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGD', 'fr', 'Bangladesh'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEN', 'fr', 'Bénin'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHL', 'fr', 'Chili'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ETH', 'fr', 'Éthiopie'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IDN', 'fr', 'Indonésie'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JOR', 'fr', 'Jordanie'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KWT', 'fr', 'Koweït'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LAO', 'fr', 'République démocratique populaire lao'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MYS', 'fr', 'Malaisie'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLI', 'fr', 'Mali'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNG', 'fr', 'Mongolie'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NZL', 'fr', 'Nouvelle-Zélande'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NGA', 'fr', 'Nigéria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAK', 'fr', 'Pakistan'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PER', 'fr', 'Pérou'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PHL', 'fr', 'Philippines'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRT', 'fr', 'Portugal'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLE', 'fr', 'Sierra Leone'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LKA', 'fr', 'Sri Lanka'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('THA', 'fr', 'Thaïlande'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UGA', 'fr', 'Ouganda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AFG', 'pt', 'Afeganistão'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGD', 'pt', 'Bangladesh'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEN', 'pt', 'Benim'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHL', 'pt', 'Chile'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ETH', 'pt', 'Etiópia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IDN', 'pt', 'Indonésia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JOR', 'pt', 'Jordânia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KWT', 'pt', 'Kuwait'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LAO', 'pt', 'República Democrática Popular do Laos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MYS', 'pt', 'Malásia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLI', 'pt', 'Mali'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNG', 'pt', 'Mongólia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NZL', 'pt', 'Nova Zelândia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NGA', 'pt', 'Nigéria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAK', 'pt', 'Paquistão'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PER', 'pt', 'Peru'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PHL', 'pt', 'Filipinas'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRT', 'pt', 'Portugal'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLE', 'pt', 'Serra Leoa'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LKA', 'pt', 'Sri Lanka'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('THA', 'pt', 'Tailândia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UGA', 'pt', 'Uganda');" +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AFG', 'ar', 'أفغانستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGD', 'ar', 'بنغلاديش'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEN', 'ar', 'بنين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHL', 'ar', 'تشيلي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ETH', 'ar', 'أثيوبيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IDN', 'ar', 'إندونيسيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JOR', 'ar', 'الأردن'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KWT', 'ar', 'الكويت'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LAO', 'ar', 'جمهورية لاو الديمقراطية الشعبية'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MYS', 'ar', 'ماليزيا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLI', 'ar', 'مالي'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNG', 'ar', 'منغوليا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NZL', 'ar', 'نيوزيلاندا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NGA', 'ar', 'نيجيريا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAK', 'ar', 'باكستان'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PER', 'ar', 'بيرو'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PHL', 'ar', 'الفلبين'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRT', 'ar', 'البرتغال'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLE', 'ar', 'سيرا ليون'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LKA', 'ar', 'سيريلانكا'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('THA', 'ar', 'تايلاند'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UGA', 'ar', 'أوغندا');");
    }
}
