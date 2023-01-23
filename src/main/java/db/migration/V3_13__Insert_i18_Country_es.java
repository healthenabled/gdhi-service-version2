package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_13__Insert_i18_Country_es   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(
                    "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ALB', 'es', 'Albania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DEU', 'es', 'Alemania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AND', 'es', 'Andorra'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AGO', 'es', 'Angola'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ATG', 'es', 'Antigua y Barbuda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SAU', 'es', 'Arabia Saudí'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DZA', 'es', 'Argelia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARG', 'es', 'Argentina'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARM', 'es', 'Armenia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUS', 'es', 'Australia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUT', 'es', 'Austria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AZE', 'es', 'Azerbaiyán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHS', 'es', 'Bahamas'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRB', 'es', 'Barbados'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHR', 'es', 'Baréin'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEL', 'es', 'Bélgica'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLZ', 'es', 'Belice'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLR', 'es', 'Bielorrusia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BOL', 'es', 'Bolivia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BIH', 'es', 'Bosnia y Herzegovina'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BWA', 'es', 'Botsuana'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRA', 'es', 'Brasil'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRN', 'es', 'Brunéi'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGR', 'es', 'Bulgaria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BFA', 'es', 'Burkina Faso'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BDI', 'es', 'Burundi'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BTN', 'es', 'Bután'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CPV', 'es', 'Cabo Verde'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KHM', 'es', 'Camboya'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CMR', 'es', 'Camerún'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAN', 'es', 'Canadá'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('QAT', 'es', 'Catar'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TCD', 'es', 'Chad'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CZE', 'es', 'Chequia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHN', 'es', 'China'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CYP', 'es', 'Chipre'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COL', 'es', 'Colombia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COM', 'es', 'Comoras'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COG', 'es', 'Congo'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRK', 'es', 'Corea del Norte'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KOR', 'es', 'Corea del Sur'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CRI', 'es', 'Costa Rica'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CIV', 'es', 'Côte d’Ivoire'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HRV', 'es', 'Croacia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CUB', 'es', 'Cuba'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DNK', 'es', 'Dinamarca'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DMA', 'es', 'Dominica'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ECU', 'es', 'Ecuador'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EGY', 'es', 'Egipto'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLV', 'es', 'El Salvador'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARE', 'es', 'Emiratos Árabes Unidos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ERI', 'es', 'Eritrea'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVK', 'es', 'Eslovaquia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVN', 'es', 'Eslovenia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ESP', 'es', 'España'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('USA', 'es', 'Estados Unidos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EST', 'es', 'Estonia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWZ', 'es', 'Esuatini'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FIN', 'es', 'Finlandia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FJI', 'es', 'Fiyi'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FRA', 'es', 'Francia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GAB', 'es', 'Gabón'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GMB', 'es', 'Gambia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GEO', 'es', 'Georgia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GHA', 'es', 'Ghana'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRD', 'es', 'Granada'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRC', 'es', 'Grecia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GTM', 'es', 'Guatemala'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GIN', 'es', 'Guinea'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNQ', 'es', 'Guinea Ecuatorial'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNB', 'es', 'Guinea-Bisáu'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GUY', 'es', 'Guyana'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HTI', 'es', 'Haití'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HND', 'es', 'Honduras'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HUN', 'es', 'Hungría'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IND', 'es', 'India'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRQ', 'es', 'Irak'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRN', 'es', 'Irán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRL', 'es', 'Irlanda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISL', 'es', 'Islandia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COK', 'es', 'Islas Cook'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MHL', 'es', 'Islas Marshall'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLB', 'es', 'Islas Salomón'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISR', 'es', 'Israel'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ITA', 'es', 'Italia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JAM', 'es', 'Jamaica'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JPN', 'es', 'Japón'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KAZ', 'es', 'Kazajistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KEN', 'es', 'Kenia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KGZ', 'es', 'Kirguistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KIR', 'es', 'Kiribati'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LSO', 'es', 'Lesoto'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LVA', 'es', 'Letonia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBN', 'es', 'Líbano'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBR', 'es', 'Liberia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBY', 'es', 'Libia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LTU', 'es', 'Lituania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LUX', 'es', 'Luxemburgo'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MKD', 'es', 'Macedonia del Norte'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDG', 'es', 'Madagascar'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MWI', 'es', 'Malaui'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDV', 'es', 'Maldivas'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLT', 'es', 'Malta'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MAR', 'es', 'Marruecos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MUS', 'es', 'Mauricio'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MRT', 'es', 'Mauritania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MEX', 'es', 'México'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FSM', 'es', 'Micronesia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDA', 'es', 'Moldavia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MCO', 'es', 'Mónaco'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNE', 'es', 'Montenegro'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MOZ', 'es', 'Mozambique'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MMR', 'es', 'Myanmar (Birmania)'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NAM', 'es', 'Namibia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NRU', 'es', 'Nauru'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NPL', 'es', 'Nepal'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIC', 'es', 'Nicaragua'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NER', 'es', 'Níger'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIU', 'es', 'Niue'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NOR', 'es', 'Noruega'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('OMN', 'es', 'Omán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NLD', 'es', 'Países Bajos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PLW', 'es', 'Palaos'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAN', 'es', 'Panamá'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PNG', 'es', 'Papúa Nueva Guinea'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRY', 'es', 'Paraguay'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('POL', 'es', 'Polonia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GBR', 'es', 'Reino Unido'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAF', 'es', 'República Centroafricana'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COD', 'es', 'República Democrática del Congo'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DOM', 'es', 'República Dominicana'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RWA', 'es', 'Ruanda'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ROU', 'es', 'Rumanía'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RUS', 'es', 'Rusia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('WSM', 'es', 'Samoa'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KNA', 'es', 'San Cristóbal y Nieves'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SMR', 'es', 'San Marino'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VCT', 'es', 'San Vicente y las Granadinas'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LCA', 'es', 'Santa Lucía'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('STP', 'es', 'Santo Tomé y Príncipe'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SEN', 'es', 'Senegal'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SRB', 'es', 'Serbia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYC', 'es', 'Seychelles'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SGP', 'es', 'Singapur'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYR', 'es', 'Siria'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SOM', 'es', 'Somalia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZAF', 'es', 'Sudáfrica'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SDN', 'es', 'Sudán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SSD', 'es', 'Sudán del Sur'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWE', 'es', 'Suecia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHE', 'es', 'Suiza'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SUR', 'es', 'Surinam'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TZA', 'es', 'Tanzania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TJK', 'es', 'Tayikistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TLS', 'es', 'Timor-Leste'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TGO', 'es', 'Togo'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TON', 'es', 'Tonga'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TTO', 'es', 'Trinidad y Tobago'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUN', 'es', 'Túnez'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TKM', 'es', 'Turkmenistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUR', 'es', 'Turquía'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUV', 'es', 'Tuvalu'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UKR', 'es', 'Ucrania'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('URY', 'es', 'Uruguay'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UZB', 'es', 'Uzbekistán'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VUT', 'es', 'Vanuatu'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VEN', 'es', 'Venezuela'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VNM', 'es', 'Vietnam'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('YEM', 'es', 'Yemen'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DJI', 'es', 'Yibuti'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZMB', 'es', 'Zambia'); " +
                        "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZWE', 'es', 'Zimbabue'); "
        );
    }
}
