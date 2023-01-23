package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_15__Insert_i18_Country_fr   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZAF', 'fr', 'Afrique du Sud'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ALB', 'fr', 'Albanie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DZA', 'fr', 'Algérie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DEU', 'fr', 'Allemagne'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AND', 'fr', 'Andorre'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AGO', 'fr', 'Angola'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ATG', 'fr', 'Antigua-et-Barbuda'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SAU', 'fr', 'Arabie saoudite'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARG', 'fr', 'Argentine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARM', 'fr', 'Arménie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUS', 'fr', 'Australie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AUT', 'fr', 'Autriche'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('AZE', 'fr', 'Azerbaïdjan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHS', 'fr', 'Bahamas'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BHR', 'fr', 'Bahreïn'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRB', 'fr', 'Barbade'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BEL', 'fr', 'Belgique'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLZ', 'fr', 'Belize'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BTN', 'fr', 'Bhoutan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BLR', 'fr', 'Biélorussie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BOL', 'fr', 'Bolivie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BIH', 'fr', 'Bosnie-Herzégovine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BWA', 'fr', 'Botswana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRA', 'fr', 'Brésil'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BRN', 'fr', 'Brunéi Darussalam'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BGR', 'fr', 'Bulgarie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BFA', 'fr', 'Burkina Faso'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('BDI', 'fr', 'Burundi'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KHM', 'fr', 'Cambodge'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CMR', 'fr', 'Cameroun'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAN', 'fr', 'Canada'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CPV', 'fr', 'Cap-Vert'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHN', 'fr', 'Chine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CYP', 'fr', 'Chypre'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COL', 'fr', 'Colombie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COM', 'fr', 'Comores'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COG', 'fr', 'Congo-Brazzaville'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COD', 'fr', 'Congo-Kinshasa'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRK', 'fr', 'Corée du Nord'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KOR', 'fr', 'Corée du Sud'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CRI', 'fr', 'Costa Rica'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CIV', 'fr', 'Côte d’Ivoire'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HRV', 'fr', 'Croatie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CUB', 'fr', 'Cuba'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DNK', 'fr', 'Danemark'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DJI', 'fr', 'Djibouti'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DMA', 'fr', 'Dominique'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EGY', 'fr', 'Égypte'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ARE', 'fr', 'Émirats arabes unis'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ECU', 'fr', 'Équateur'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ERI', 'fr', 'Érythrée'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ESP', 'fr', 'Espagne'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('EST', 'fr', 'Estonie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWZ', 'fr', 'Eswatini'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FSM', 'fr', 'États fédérés de Micronésie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('USA', 'fr', 'États-Unis'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FJI', 'fr', 'Fidji'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FIN', 'fr', 'Finlande'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('FRA', 'fr', 'France'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GAB', 'fr', 'Gabon'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GMB', 'fr', 'Gambie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GEO', 'fr', 'Géorgie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GHA', 'fr', 'Ghana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRC', 'fr', 'Grèce'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GRD', 'fr', 'Grenade'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GTM', 'fr', 'Guatemala'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GIN', 'fr', 'Guinée'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNQ', 'fr', 'Guinée équatoriale'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GNB', 'fr', 'Guinée-Bissau'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GUY', 'fr', 'Guyana'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HTI', 'fr', 'Haïti'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HND', 'fr', 'Honduras'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('HUN', 'fr', 'Hongrie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('COK', 'fr', 'Îles Cook'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MHL', 'fr', 'Îles Marshall'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLB', 'fr', 'Îles Salomon'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IND', 'fr', 'Inde'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRQ', 'fr', 'Irak'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRN', 'fr', 'Iran'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('IRL', 'fr', 'Irlande'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISL', 'fr', 'Islande'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ISR', 'fr', 'Israël'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ITA', 'fr', 'Italie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JAM', 'fr', 'Jamaïque'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('JPN', 'fr', 'Japon'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KAZ', 'fr', 'Kazakhstan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KEN', 'fr', 'Kenya'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KGZ', 'fr', 'Kirghizistan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KIR', 'fr', 'Kiribati'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LSO', 'fr', 'Lesotho'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LVA', 'fr', 'Lettonie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBN', 'fr', 'Liban'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBR', 'fr', 'Libéria'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LBY', 'fr', 'Libye'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LTU', 'fr', 'Lituanie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LUX', 'fr', 'Luxembourg'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MKD', 'fr', 'Macédoine du Nord'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDG', 'fr', 'Madagascar'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MWI', 'fr', 'Malawi'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDV', 'fr', 'Maldives'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MLT', 'fr', 'Malte'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MAR', 'fr', 'Maroc'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MUS', 'fr', 'Maurice'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MRT', 'fr', 'Mauritanie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MEX', 'fr', 'Mexique'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MDA', 'fr', 'Moldavie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MCO', 'fr', 'Monaco'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MNE', 'fr', 'Monténégro'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MOZ', 'fr', 'Mozambique'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('MMR', 'fr', 'Myanmar (Birmanie)'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NAM', 'fr', 'Namibie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NRU', 'fr', 'Nauru'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NPL', 'fr', 'Népal'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIC', 'fr', 'Nicaragua'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NER', 'fr', 'Niger'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NIU', 'fr', 'Niue'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NOR', 'fr', 'Norvège'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('OMN', 'fr', 'Oman'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UZB', 'fr', 'Ouzbékistan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PLW', 'fr', 'Palaos'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PAN', 'fr', 'Panama'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PNG', 'fr', 'Papouasie-Nouvelle-Guinée'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('PRY', 'fr', 'Paraguay'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('NLD', 'fr', 'Pays-Bas'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('POL', 'fr', 'Pologne'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('QAT', 'fr', 'Qatar'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CAF', 'fr', 'République centrafricaine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('DOM', 'fr', 'République dominicaine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ROU', 'fr', 'Roumanie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('GBR', 'fr', 'Royaume-Uni'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RUS', 'fr', 'Russie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('RWA', 'fr', 'Rwanda'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('KNA', 'fr', 'Saint-Christophe-et-Niévès'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SMR', 'fr', 'Saint-Marin'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VCT', 'fr', 'Saint-Vincent-et-les-Grenadines'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('LCA', 'fr', 'Sainte-Lucie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SLV', 'fr', 'Salvador'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('WSM', 'fr', 'Samoa'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('STP', 'fr', 'Sao Tomé-et-Principe'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SEN', 'fr', 'Sénégal'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SRB', 'fr', 'Serbie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYC', 'fr', 'Seychelles'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SGP', 'fr', 'Singapour'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVK', 'fr', 'Slovaquie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SVN', 'fr', 'Slovénie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SOM', 'fr', 'Somalie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SDN', 'fr', 'Soudan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SSD', 'fr', 'Soudan du Sud'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SWE', 'fr', 'Suède'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CHE', 'fr', 'Suisse'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SUR', 'fr', 'Suriname'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('SYR', 'fr', 'Syrie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TJK', 'fr', 'Tadjikistan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TZA', 'fr', 'Tanzanie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TCD', 'fr', 'Tchad'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('CZE', 'fr', 'Tchéquie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TLS', 'fr', 'Timor oriental'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TGO', 'fr', 'Togo'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TON', 'fr', 'Tonga'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TTO', 'fr', 'Trinité-et-Tobago'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUN', 'fr', 'Tunisie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TKM', 'fr', 'Turkménistan'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUR', 'fr', 'Turquie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('TUV', 'fr', 'Tuvalu'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('UKR', 'fr', 'Ukraine'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('URY', 'fr', 'Uruguay'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VUT', 'fr', 'Vanuatu'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VEN', 'fr', 'Venezuela'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('VNM', 'fr', 'Vietnam'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('YEM', 'fr', 'Yémen'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZMB', 'fr', 'Zambie'); " +
                "INSERT INTO i18n.country(country_id, language_id, name) VALUES('ZWE', 'fr', 'Zimbabwe'); "

        );
    }
}
