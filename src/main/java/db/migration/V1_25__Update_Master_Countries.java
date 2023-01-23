package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_25__Update_Master_Countries   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE validated_config.development_indicators DROP CONSTRAINT development_indicators_country_id_fkey");
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators DROP CONSTRAINT health_indicators_country_id_fkey");
        jdbcTemplate.execute("ALTER TABLE validated_config.countries_summary DROP CONSTRAINT countries_summary_country_id_fkey");
        jdbcTemplate.execute("ALTER TABLE validated_config.country_resource_links DROP CONSTRAINT country_resource_links_country_id_fkey");
        jdbcTemplate.execute("DELETE FROM master.countries;");
        jdbcTemplate.execute("insert into master.countries (name, id) values " +
                "('Afghanistan',	'AFG')," +
                        "('Albania',	'ALB')," +
                        "('Algeria',	'DZA')," +
                        "('Andorra',	'AND')," +
                        "('Angola',	'AGO')," +
                        "('Antigua and Barbuda',	'ATG')," +
                        "('Argentina',	'ARG')," +
                        "('Armenia',	'ARM')," +
                        "('Australia',	'AUS')," +
                        "('Austria',	'AUT')," +
                        "('Azerbaijan',	'AZE')," +
                        "('Bahamas',	'BHS')," +
                        "('Bahrain',	'BHR')," +
                        "('Bangladesh',	'BGD')," +
                        "('Barbados',	'BRB')," +
                        "('Belarus',	'BLR')," +
                        "('Belgium',	'BEL')," +
                        "('Belize',	'BLZ')," +
                        "('Benin',	'BEN')," +
                        "('Bhutan',	'BTN')," +
                        "('Bolivia',	'BOL')," +
                        "('Bosnia and Herzegovina',	'BIH')," +
                        "('Botswana',	'BWA')," +
                        "('Brazil',	'BRA')," +
                        "('Brunei Darussalam',	'BRN')," +
                        "('Bulgaria',	'BGR')," +
                        "('Burkina Faso',	'BFA')," +
                        "('Burundi',	'BDI')," +
                        "('Cambodia',	'KHM')," +
                        "('Cameroon',	'CMR')," +
                        "('Canada',	'CAN')," +
                        "('Cape Verde',	'CPV')," +
                        "('Central African Republic',	'CAF')," +
                        "('Chad',	'TCD')," +
                        "('Chile',	'CHL')," +
                        "('China',	'CHN')," +
                        "('Colombia',	'COL')," +
                        "('Comoros',	'COM')," +
                        "('Congo',	'COG')," +
                        "('Cook Islands',	'COK')," +
                        "('Costa Rica',	'CRI')," +
                        "('Côte d''Ivoire',	'CIV')," +
                        "('Croatia',	'HRV')," +
                        "('Cuba',	'CUB')," +
                        "('Cyprus',	'CYP')," +
                        "('Czech Republic',	'CZE')," +
                        "('Democratic Peoples Republic of Korea',	'PRK')," +
                        "('Democratic Republic of the Congo',	'COD')," +
                        "('Denmark',	'DNK')," +
                        "('Djibouti',	'DJI')," +
                        "('Dominica',	'DMA')," +
                        "('Dominican Republic',	'DOM')," +
                        "('Ecuador',	'ECU')," +
                        "('Egypt',	'EGY')," +
                        "('El Salvador',	'SLV')," +
                        "('Equatorial Guinea',	'GNQ')," +
                        "('Eritrea',	'ERI')," +
                        "('Estonia',	'EST')," +
                        "('Ethiopia',	'ETH')," +
                        "('Fiji',	'FJI')," +
                        "('Finland',	'FIN')," +
                        "('France',	'FRA')," +
                        "('Gabon',	'GAB')," +
                        "('Gambia',	'GMB')," +
                        "('Georgia',	'GEO')," +
                        "('Germany',	'DEU')," +
                        "('Ghana',	'GHA')," +
                        "('Greece',	'GRC')," +
                        "('Grenada',	'GRD')," +
                        "('Guatemala',	'GTM')," +
                        "('Guinea',	'GIN')," +
                        "('Guinea-Bissau',	'GNB')," +
                        "('Guyana',	'GUY')," +
                        "('Haiti',	'HTI')," +
                        "('Honduras',	'HND')," +
                        "('Hungary',	'HUN')," +
                        "('Iceland',	'ISL')," +
                        "('India',	'IND')," +
                        "('Indonesia',	'IDN')," +
                        "('Iran, Islamic Republic of',	'IRN')," +
                        "('Iraq',	'IRQ')," +
                        "('Ireland',	'IRL')," +
                        "('Israel',	'ISR')," +
                        "('Italy',	'ITA')," +
                        "('Jamaica',	'JAM')," +
                        "('Japan',	'JPN')," +
                        "('Jordan',	'JOR')," +
                        "('Kazakhstan',	'KAZ')," +
                        "('Kenya',	'KEN')," +
                        "('Kiribati',	'KIR')," +
                        "('Korea, Republic of',	'KOR')," +
                        "('Kuwait',	'KWT')," +
                        "('Kyrgyzstan',	'KGZ')," +
                        "('Lao People''s Democratic Republic',	'LAO')," +
                        "('Latvia',	'LVA')," +
                        "('Lebanon',	'LBN')," +
                        "('Lesotho',	'LSO')," +
                        "('Liberia',	'LBR')," +
                        "('Libya',	'LBY')," +
                        "('Lithuania',	'LTU')," +
                        "('Luxembourg',	'LUX')," +
                        "('Macedonia, the former Yugoslav Republic of',	'MKD')," +
                        "('Madagascar',	'MDG')," +
                        "('Malawi',	'MWI')," +
                        "('Malaysia',	'MYS')," +
                        "('Maldives',	'MDV')," +
                        "('Mali',	'MLI')," +
                        "('Malta',	'MLT')," +
                        "('Marshall Islands',	'MHL')," +
                        "('Mauritania',	'MRT')," +
                        "('Mauritius',	'MUS')," +
                        "('Mexico',	'MEX')," +
                        "('Micronesia, Federated States of',	'FSM')," +
                        "('Moldova, Republic of',	'MDA')," +
                        "('Monaco',	'MCO')," +
                        "('Mongolia',	'MNG')," +
                        "('Montenegro',	'MNE')," +
                        "('Morocco',	'MAR')," +
                        "('Mozambique',	'MOZ')," +
                        "('Myanmar',	'MMR')," +
                        "('Namibia',	'NAM')," +
                        "('Nauru',	'NRU')," +
                        "('Nepal',	'NPL')," +
                        "('Netherlands',	'NLD')," +
                        "('New Zealand',	'NZL')," +
                        "('Nicaragua',	'NIC')," +
                        "('Niger',	'NER')," +
                        "('Nigeria',	'NGA')," +
                        "('Niue',	'NIU')," +
                        "('Norway',	'NOR')," +
                        "('Oman',	'OMN')," +
                        "('Pakistan',	'PAK')," +
                        "('Palau',	'PLW')," +
                        "('Panama',	'PAN')," +
                        "('Papua New Guinea',	'PNG')," +
                        "('Paraguay',	'PRY')," +
                        "('Peru',	'PER')," +
                        "('Philippines',	'PHL')," +
                        "('Poland',	'POL')," +
                        "('Portugal',	'PRT')," +
                        "('Qatar',	'QAT')," +
                        "('Romania',	'ROU')," +
                        "('Russian Federation',	'RUS')," +
                        "('Rwanda',	'RWA')," +
                        "('Saint Kitts and Nevis',	'KNA')," +
                        "('Saint Lucia',	'LCA')," +
                        "('Saint Vincent and the Grenadines',	'VCT')," +
                        "('Samoa',	'WSM')," +
                        "('San Marino',	'SMR')," +
                        "('Sao Tome and Principe',	'STP')," +
                        "('Saudi Arabia',	'SAU')," +
                        "('Senegal',	'SEN')," +
                        "('Serbia',	'SRB')," +
                        "('Seychelles',	'SYC')," +
                        "('Sierra Leone',	'SLE')," +
                        "('Singapore',	'SGP')," +
                        "('Slovakia',	'SVK')," +
                        "('Slovenia',	'SVN')," +
                        "('Somalia',	'SOM')," +
                        "('Solomon Islands',	'SLB')," +
                        "('South Africa',	'ZAF')," +
                        "('Spain',	'ESP')," +
                        "('Sri Lanka',	'LKA')," +
                        "('Sudan',	'SDN')," +
                        "('South Sudan',	'SSD')," +
                        "('Suriname',	'SUR')," +
                        "('Swaziland',	'SWZ')," +
                        "('Sweden',	'SWE')," +
                        "('Switzerland',	'CHE')," +
                        "('Syrian Arab Republic',	'SYR')," +
                        "('Tanzania, United Republic of',	'TZA')," +
                        "('Tajikistan',	'TJK')," +
                        "('Thailand',	'THA')," +
                        "('Timor-Leste',	'TLS')," +
                        "('Togo',	'TGO')," +
                        "('Tonga',	'TON')," +
                        "('Trinidad and Tobago',	'TTO')," +
                        "('Tunisia',	'TUN')," +
                        "('Turkey',	'TUR')," +
                        "('Turkmenistan',	'TKM')," +
                        "('Tuvalu',	'TUV')," +
                        "('Uganda',	'UGA')," +
                        "('Ukraine',	'UKR')," +
                        "('United Arab Emirates',	'ARE')," +
                        "('United Kingdom',	'GBR')," +
                        "('United States of America',	'USA')," +
                        "('Uruguay',	'URY')," +
                        "('Uzbekistan',	'UZB')," +
                        "('Vanuatu',	'VUT')," +
                        "('Venezuela',	'VEN')," +
                        "('Viet Nam',	'VNM')," +
                        "('Yemen',	'YEM')," +
                        "('Zambia',	'ZMB')," +
                        "('Zimbabwe',	'ZWE');");
        jdbcTemplate.execute("DELETE FROM validated_config.development_indicators WHERE country_id NOT IN (SELECT id FROM master.countries)");
        jdbcTemplate.execute("ALTER TABLE validated_config.development_indicators ADD CONSTRAINT development_indicators_country_id_fkey FOREIGN KEY (country_id) REFERENCES master.countries (id)");
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ADD CONSTRAINT health_indicators_country_id_fkey FOREIGN KEY (country_id) REFERENCES master.countries (id)");
        jdbcTemplate.execute("ALTER TABLE validated_config.countries_summary ADD CONSTRAINT countries_summary_country_id_fkey FOREIGN KEY (country_id) REFERENCES master.countries (id)");
        jdbcTemplate.execute("ALTER TABLE validated_config.country_resource_links ADD CONSTRAINT country_resource_links_country_id_fkey FOREIGN KEY (country_id) REFERENCES master.countries (id)");
    }
}
