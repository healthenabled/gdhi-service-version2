package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_16__Insert_i18_Country_pt   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZAF', 'pt', 'África do Sul'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ALB', 'pt', 'Albânia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DEU', 'pt', 'Alemanha'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AND', 'pt', 'Andorra'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AGO', 'pt', 'Angola'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ATG', 'pt', 'Antígua e Barbuda'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SAU', 'pt', 'Arábia Saudita'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DZA', 'pt', 'Argélia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARG', 'pt', 'Argentina'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARM', 'pt', 'Armênia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUS', 'pt', 'Austrália'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUT', 'pt', 'Áustria'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AZE', 'pt', 'Azerbaijão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHS', 'pt', 'Bahamas'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHR', 'pt', 'Bahrein'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRB', 'pt', 'Barbados'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEL', 'pt', 'Bélgica'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLZ', 'pt', 'Belize'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLR', 'pt', 'Bielorrússia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BOL', 'pt', 'Bolívia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BIH', 'pt', 'Bósnia e Herzegovina'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BWA', 'pt', 'Botsuana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRA', 'pt', 'Brasil'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRN', 'pt', 'Brunei'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGR', 'pt', 'Bulgária'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BFA', 'pt', 'Burquina Faso'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BDI', 'pt', 'Burundi'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BTN', 'pt', 'Butão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CPV', 'pt', 'Cabo Verde'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CMR', 'pt', 'Camarões'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KHM', 'pt', 'Camboja'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAN', 'pt', 'Canadá'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('QAT', 'pt', 'Catar'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KAZ', 'pt', 'Cazaquistão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TCD', 'pt', 'Chade'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHN', 'pt', 'China'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CYP', 'pt', 'Chipre'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COL', 'pt', 'Colômbia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COM', 'pt', 'Comores'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COD', 'pt', 'Congo - Kinshasa'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRK', 'pt', 'Coreia do Norte'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KOR', 'pt', 'Coreia do Sul'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CIV', 'pt', 'Costa do Marfim'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CRI', 'pt', 'Costa Rica'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HRV', 'pt', 'Croácia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CUB', 'pt', 'Cuba'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DNK', 'pt', 'Dinamarca'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DJI', 'pt', 'Djibuti'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DMA', 'pt', 'Dominica'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EGY', 'pt', 'Egito'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLV', 'pt', 'El Salvador'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARE', 'pt', 'Emirados Árabes Unidos'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ECU', 'pt', 'Equador'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ERI', 'pt', 'Eritreia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVK', 'pt', 'Eslováquia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVN', 'pt', 'Eslovênia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ESP', 'pt', 'Espanha'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWZ', 'pt', 'Essuatíni'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('USA', 'pt', 'Estados Unidos'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EST', 'pt', 'Estônia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FJI', 'pt', 'Fiji'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FIN', 'pt', 'Finlândia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FRA', 'pt', 'França'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GAB', 'pt', 'Gabão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GMB', 'pt', 'Gâmbia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GHA', 'pt', 'Gana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GEO', 'pt', 'Geórgia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRD', 'pt', 'Granada'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRC', 'pt', 'Grécia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GTM', 'pt', 'Guatemala'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GUY', 'pt', 'Guiana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GIN', 'pt', 'Guiné'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNQ', 'pt', 'Guiné Equatorial'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNB', 'pt', 'Guiné-Bissau'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HTI', 'pt', 'Haiti'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HND', 'pt', 'Honduras'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HUN', 'pt', 'Hungria'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('YEM', 'pt', 'Iêmen'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COK', 'pt', 'Ilhas Cook'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MHL', 'pt', 'Ilhas Marshall'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLB', 'pt', 'Ilhas Salomão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IND', 'pt', 'Índia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRN', 'pt', 'Irã'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRQ', 'pt', 'Iraque'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRL', 'pt', 'Irlanda'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISL', 'pt', 'Islândia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISR', 'pt', 'Israel'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ITA', 'pt', 'Itália'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JAM', 'pt', 'Jamaica'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JPN', 'pt', 'Japão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LSO', 'pt', 'Lesoto'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LVA', 'pt', 'Letônia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBN', 'pt', 'Líbano'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBR', 'pt', 'Libéria'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBY', 'pt', 'Líbia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LTU', 'pt', 'Lituânia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LUX', 'pt', 'Luxemburgo'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MKD', 'pt', 'Macedônia do Norte'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDG', 'pt', 'Madagascar'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MWI', 'pt', 'Malaui'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDV', 'pt', 'Maldivas'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLT', 'pt', 'Malta'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MAR', 'pt', 'Marrocos'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MUS', 'pt', 'Maurício'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MRT', 'pt', 'Mauritânia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MEX', 'pt', 'México'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MMR', 'pt', 'Mianmar (Birmânia)'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FSM', 'pt', 'Micronésia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MOZ', 'pt', 'Moçambique'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDA', 'pt', 'Moldova'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MCO', 'pt', 'Mônaco'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNE', 'pt', 'Montenegro'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NAM', 'pt', 'Namíbia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NRU', 'pt', 'Nauru'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NPL', 'pt', 'Nepal'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIC', 'pt', 'Nicarágua'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NER', 'pt', 'Níger'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIU', 'pt', 'Niue'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NOR', 'pt', 'Noruega'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('OMN', 'pt', 'Omã'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NLD', 'pt', 'Países Baixos'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PLW', 'pt', 'Palau'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAN', 'pt', 'Panamá'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PNG', 'pt', 'Papua-Nova Guiné'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRY', 'pt', 'Paraguai'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('POL', 'pt', 'Polônia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KEN', 'pt', 'Quênia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KGZ', 'pt', 'Quirguistão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KIR', 'pt', 'Quiribati'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GBR', 'pt', 'Reino Unido'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAF', 'pt', 'República Centro-Africana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COG', 'pt', 'República do Congo'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DOM', 'pt', 'República Dominicana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ROU', 'pt', 'Romênia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RWA', 'pt', 'Ruanda'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RUS', 'pt', 'Rússia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('WSM', 'pt', 'Samoa'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SMR', 'pt', 'San Marino'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LCA', 'pt', 'Santa Lúcia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KNA', 'pt', 'São Cristóvão e Névis'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('STP', 'pt', 'São Tomé e Príncipe'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VCT', 'pt', 'São Vicente e Granadinas'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYC', 'pt', 'Seicheles'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SEN', 'pt', 'Senegal'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SRB', 'pt', 'Sérvia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SGP', 'pt', 'Singapura'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYR', 'pt', 'Síria'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SOM', 'pt', 'Somália'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SDN', 'pt', 'Sudão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SSD', 'pt', 'Sudão do Sul'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWE', 'pt', 'Suécia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHE', 'pt', 'Suíça'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SUR', 'pt', 'Suriname'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TJK', 'pt', 'Tadjiquistão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TZA', 'pt', 'Tanzânia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CZE', 'pt', 'Tchéquia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TLS', 'pt', 'Timor-Leste'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TGO', 'pt', 'Togo'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TON', 'pt', 'Tonga'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TTO', 'pt', 'Trinidad e Tobago'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUN', 'pt', 'Tunísia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TKM', 'pt', 'Turcomenistão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUR', 'pt', 'Turquia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUV', 'pt', 'Tuvalu'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UKR', 'pt', 'Ucrânia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('URY', 'pt', 'Uruguai'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UZB', 'pt', 'Uzbequistão'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VUT', 'pt', 'Vanuatu'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VEN', 'pt', 'Venezuela'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VNM', 'pt', 'Vietnã'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZMB', 'pt', 'Zâmbia'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZWE', 'pt', 'Zimbábue'); "

        );
    }
}
