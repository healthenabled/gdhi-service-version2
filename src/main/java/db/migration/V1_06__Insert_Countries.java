 package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

 public class V1_06__Insert_Countries   {
     
     public void doMigrate(JdbcTemplate jdbcTemplate) {
         jdbcTemplate.execute("INSERT INTO master.countries (id, name) VALUES\n" +
                 "('ARG', 'Argentina'),\n" +
                 "('AUS', 'Australia'),\n" +
                 "('BRA', 'Brazil'),\n" +
                 "('CHN', 'China'),\n" +
                 "('FRA', 'France'),\n" +
                 "('DEU', 'Germany'),\n" +
                 "('IND', 'India'),\n" +
                 "('IDN', 'Indonesia'),\n" +
                 "('ITA', 'Italy'),\n" +
                 "('JPN', 'Japan'),\n" +
                 "('KOR', 'Korea, Rep.'),\n" +
                 "('MEX', 'Mexico'),\n" +
                 "('NLD', 'Netherlands'),\n" +
                 "('RUS', 'Russian Federation'),\n" +
                 "('SAU', 'Saudi Arabia'),\n" +
                 "('ESP', 'Spain'),\n" +
                 "('CHE', 'Switzerland'),\n" +
                 "('TUR', 'Turkey'),\n" +
                 "('GBR', 'United Kingdom'),\n" +
                 "('USA', 'United States'),\n" +
                 "('AFG', 'Afghanistan'),\n" +
                 "('ALB', 'Albania'),\n" +
                 "('DZA', 'Algeria'),\n" +
                 "('ASM', 'American Samoa'),\n" +
                 "('AND', 'Andorra'),\n" +
                 "('AGO', 'Angola'),\n" +
                 "('ATG', 'Antigua and Barbuda'),\n" +
                 "('ARB', 'Arab World'),\n" +
                 "('ARM', 'Armenia'),\n" +
                 "('ABW', 'Aruba'),\n" +
                 "('AUT', 'Austria'),\n" +
                 "('AZE', 'Azerbaijan'),\n" +
                 "('BHS', 'Bahamas, The'),\n" +
                 "('BHR', 'Bahrain'),\n" +
                 "('BGD', 'Bangladesh'),\n" +
                 "('BRB', 'Barbados'),\n" +
                 "('BLR', 'Belarus'),\n" +
                 "('BEL', 'Belgium'),\n" +
                 "('BLZ', 'Belize'),\n" +
                 "('BEN', 'Benin'),\n" +
                 "('BMU', 'Bermuda'),\n" +
                 "('BTN', 'Bhutan'),\n" +
                 "('BOL', 'Bolivia'),\n" +
                 "('BIH', 'Bosnia and Herzegovina'),\n" +
                 "('BWA', 'Botswana'),\n" +
                 "('VGB', 'British Virgin Islands'),\n" +
                 "('BRN', 'Brunei Darussalam'),\n" +
                 "('BGR', 'Bulgaria'),\n" +
                 "('BFA', 'Burkina Faso'),\n" +
                 "('BDI', 'Burundi'),\n" +
                 "('CPV', 'Cabo Verde'),\n" +
                 "('KHM', 'Cambodia'),\n" +
                 "('CMR', 'Cameroon'),\n" +
                 "('CAN', 'Canada'),\n" +
                 "('CSS', 'Caribbean small states'),\n" +
                 "('CYM', 'Cayman Islands'),\n" +
                 "('CAF', 'Central African Republic'),\n" +
                 "('CEB', 'Central Europe and the Baltics'),\n" +
                 "('TCD', 'Chad'),\n" +
                 "('CHI', 'Channel Islands'),\n" +
                 "('CHL', 'Chile'),\n" +
                 "('COL', 'Colombia'),\n" +
                 "('COM', 'Comoros'),\n" +
                 "('COD', 'Congo, Dem. Rep.'),\n" +
                 "('COG', 'Congo, Rep.'),\n" +
                 "('CRI', 'Costa Rica'),\n" +
                 "('CIV', 'Cote d''Ivoire'),\n" +
                 "('HRV', 'Croatia'),\n" +
                 "('CUB', 'Cuba'),\n" +
                 "('CUW', 'Curacao'),\n" +
                 "('CYP', 'Cyprus'),\n" +
                 "('CZE', 'Czech Republic'),\n" +
                 "('DNK', 'Denmark'),\n" +
                 "('DJI', 'Djibouti'),\n" +
                 "('DMA', 'Dominica'),\n" +
                 "('DOM', 'Dominican Republic'),\n" +
                 "('EAR', 'Early-demographic dividend'),\n" +
                 "('EAS', 'East Asia & Pacific'),\n" +
                 "('EAP', 'East Asia & Pacific (excluding high income)'),\n" +
                 "('TEA', 'East Asia & Pacific (IDA & IBRD countries)'),\n" +
                 "('ECU', 'Ecuador'),\n" +
                 "('EGY', 'Egypt, Arab Rep.'),\n" +
                 "('SLV', 'El Salvador'),\n" +
                 "('GNQ', 'Equatorial Guinea'),\n" +
                 "('ERI', 'Eritrea'),\n" +
                 "('EST', 'Estonia'),\n" +
                 "('ETH', 'Ethiopia'),\n" +
                 "('EMU', 'Euro area'),\n" +
                 "('ECS', 'Europe & Central Asia'),\n" +
                 "('ECA', 'Europe & Central Asia (excluding high income)'),\n" +
                 "('TEC', 'Europe & Central Asia (IDA & IBRD countries)'),\n" +
                 "('EUU', 'European Union'),\n" +
                 "('FRO', 'Faroe Islands'),\n" +
                 "('FJI', 'Fiji'),\n" +
                 "('FIN', 'Finland'),\n" +
                 "('FCS', 'Fragile and conflict affected situations'),\n" +
                 "('PYF', 'French Polynesia'),\n" +
                 "('GAB', 'Gabon'),\n" +
                 "('GMB', 'Gambia, The'),\n" +
                 "('GEO', 'Georgia'),\n" +
                 "('GHA', 'Ghana'),\n" +
                 "('GIB', 'Gibraltar'),\n" +
                 "('GRC', 'Greece'),\n" +
                 "('GRL', 'Greenland'),\n" +
                 "('GRD', 'Grenada'),\n" +
                 "('GUM', 'Guam'),\n" +
                 "('GTM', 'Guatemala'),\n" +
                 "('GIN', 'Guinea'),\n" +
                 "('GNB', 'Guinea-Bissau'),\n" +
                 "('GUY', 'Guyana'),\n" +
                 "('HTI', 'Haiti'),\n" +
                 "('HPC', 'Heavily indebted poor countries (HIPC)'),\n" +
                 "('HIC', 'High income'),\n" +
                 "('HND', 'Honduras'),\n" +
                 "('HKG', 'Hong Kong SAR, China'),\n" +
                 "('HUN', 'Hungary'),\n" +
                 "('IBD', 'IBRD only'),\n" +
                 "('ISL', 'Iceland'),\n" +
                 "('IBT', 'IDA & IBRD total'),\n" +
                 "('IDB', 'IDA blend'),\n" +
                 "('IDX', 'IDA only'),\n" +
                 "('IDA', 'IDA total'),\n" +
                 "('IRN', 'Iran, Islamic Rep.'),\n" +
                 "('IRQ', 'Iraq'),\n" +
                 "('IRL', 'Ireland'),\n" +
                 "('IMN', 'Isle of Man'),\n" +
                 "('ISR', 'Israel'),\n" +
                 "('JAM', 'Jamaica'),\n" +
                 "('JOR', 'Jordan'),\n" +
                 "('KAZ', 'Kazakhstan'),\n" +
                 "('KEN', 'Kenya'),\n" +
                 "('KIR', 'Kiribati'),\n" +
                 "('PRK', 'Korea, Dem. People’s Rep.'),\n" +
                 "('XKX', 'Kosovo'),\n" +
                 "('KWT', 'Kuwait'),\n" +
                 "('KGZ', 'Kyrgyz Republic'),\n" +
                 "('LAO', 'Lao PDR'),\n" +
                 "('LTE', 'Late-demographic dividend'),\n" +
                 "('LCN', 'Latin America & Caribbean'),\n" +
                 "('LAC', 'Latin America & Caribbean (excluding high income)'),\n" +
                 "('TLA', 'Latin America & the Caribbean (IDA & IBRD countries)'),\n" +
                 "('LVA', 'Latvia'),\n" +
                 "('LDC', 'Least developed countries: UN classification'),\n" +
                 "('LBN', 'Lebanon'),\n" +
                 "('LSO', 'Lesotho'),\n" +
                 "('LBR', 'Liberia'),\n" +
                 "('LBY', 'Libya'),\n" +
                 "('LIE', 'Liechtenstein'),\n" +
                 "('LTU', 'Lithuania'),\n" +
                 "('LMY', 'Low & middle income'),\n" +
                 "('LIC', 'Low income'),\n" +
                 "('LMC', 'Lower middle income'),\n" +
                 "('LUX', 'Luxembourg'),\n" +
                 "('MAC', 'Macao SAR, China'),\n" +
                 "('MKD', 'Macedonia, FYR'),\n" +
                 "('MDG', 'Madagascar'),\n" +
                 "('MWI', 'Malawi'),\n" +
                 "('MYS', 'Malaysia'),\n" +
                 "('MDV', 'Maldives'),\n" +
                 "('MLI', 'Mali'),\n" +
                 "('MLT', 'Malta'),\n" +
                 "('MHL', 'Marshall Islands'),\n" +
                 "('MRT', 'Mauritania'),\n" +
                 "('MUS', 'Mauritius'),\n" +
                 "('FSM', 'Micronesia, Fed. Sts.'),\n" +
                 "('MEA', 'Middle East & North Africa'),\n" +
                 "('MNA', 'Middle East & North Africa (excluding high income)'),\n" +
                 "('TMN', 'Middle East & North Africa (IDA & IBRD countries)'),\n" +
                 "('MIC', 'Middle income'),\n" +
                 "('MDA', 'Moldova'),\n" +
                 "('MCO', 'Monaco'),\n" +
                 "('MNG', 'Mongolia'),\n" +
                 "('MNE', 'Montenegro'),\n" +
                 "('MAR', 'Morocco'),\n" +
                 "('MOZ', 'Mozambique'),\n" +
                 "('MMR', 'Myanmar'),\n" +
                 "('NAM', 'Namibia'),\n" +
                 "('NRU', 'Nauru'),\n" +
                 "('NPL', 'Nepal'),\n" +
                 "('NCL', 'New Caledonia'),\n" +
                 "('NZL', 'New Zealand'),\n" +
                 "('NIC', 'Nicaragua'),\n" +
                 "('NER', 'Niger'),\n" +
                 "('NGA', 'Nigeria'),\n" +
                 "('NAC', 'North America'),\n" +
                 "('MNP', 'Northern Mariana Islands'),\n" +
                 "('NOR', 'Norway'),\n" +
                 "('INX', 'Not classified'),\n" +
                 "('OED', 'OECD members'),\n" +
                 "('OMN', 'Oman'),\n" +
                 "('OSS', 'Other small states'),\n" +
                 "('PSS', 'Pacific island small states'),\n" +
                 "('PAK', 'Pakistan'),\n" +
                 "('PLW', 'Palau'),\n" +
                 "('PAN', 'Panama'),\n" +
                 "('PNG', 'Papua New Guinea'),\n" +
                 "('PRY', 'Paraguay'),\n" +
                 "('PER', 'Peru'),\n" +
                 "('PHL', 'Philippines'),\n" +
                 "('POL', 'Poland'),\n" +
                 "('PRT', 'Portugal'),\n" +
                 "('PST', 'Post-demographic dividend'),\n" +
                 "('PRE', 'Pre-demographic dividend'),\n" +
                 "('PRI', 'Puerto Rico'),\n" +
                 "('QAT', 'Qatar'),\n" +
                 "('ROU', 'Romania'),\n" +
                 "('RWA', 'Rwanda'),\n" +
                 "('WSM', 'Samoa'),\n" +
                 "('SMR', 'San Marino'),\n" +
                 "('STP', 'Sao Tome and Principe'),\n" +
                 "('SEN', 'Senegal'),\n" +
                 "('SRB', 'Serbia'),\n" +
                 "('SYC', 'Seychelles'),\n" +
                 "('SLE', 'Sierra Leone'),\n" +
                 "('SGP', 'Singapore'),\n" +
                 "('SXM', 'Sint Maarten (Dutch part)'),\n" +
                 "('SVK', 'Slovak Republic'),\n" +
                 "('SVN', 'Slovenia'),\n" +
                 "('SST', 'Small states'),\n" +
                 "('SLB', 'Solomon Islands'),\n" +
                 "('SOM', 'Somalia'),\n" +
                 "('ZAF', 'South Africa'),\n" +
                 "('SAS', 'South Asia'),\n" +
                 "('TSA', 'South Asia (IDA & IBRD)'),\n" +
                 "('SSD', 'South Sudan'),\n" +
                 "('LKA', 'Sri Lanka'),\n" +
                 "('KNA', 'St. Kitts and Nevis'),\n" +
                 "('LCA', 'St. Lucia'),\n" +
                 "('MAF', 'St. Martin (French part)'),\n" +
                 "('VCT', 'St. Vincent and the Grenadines'),\n" +
                 "('SSF', 'Sub-Saharan Africa'),\n" +
                 "('SSA', 'Sub-Saharan Africa (excluding high income)'),\n" +
                 "('TSS', 'Sub-Saharan Africa (IDA & IBRD countries)'),\n" +
                 "('SDN', 'Sudan'),\n" +
                 "('SUR', 'Suriname'),\n" +
                 "('SWZ', 'Swaziland'),\n" +
                 "('SWE', 'Sweden'),\n" +
                 "('SYR', 'Syrian Arab Republic'),\n" +
                 "('TJK', 'Tajikistan'),\n" +
                 "('TZA', 'Tanzania'),\n" +
                 "('THA', 'Thailand'),\n" +
                 "('TLS', 'Timor-Leste'),\n" +
                 "('TGO', 'Togo'),\n" +
                 "('TON', 'Tonga'),\n" +
                 "('TTO', 'Trinidad and Tobago'),\n" +
                 "('TUN', 'Tunisia'),\n" +
                 "('TKM', 'Turkmenistan'),\n" +
                 "('TCA', 'Turks and Caicos Islands'),\n" +
                 "('TUV', 'Tuvalu'),\n" +
                 "('UGA', 'Uganda'),\n" +
                 "('UKR', 'Ukraine'),\n" +
                 "('ARE', 'United Arab Emirates'),\n" +
                 "('UMC', 'Upper middle income'),\n" +
                 "('URY', 'Uruguay'),\n" +
                 "('UZB', 'Uzbekistan'),\n" +
                 "('VUT', 'Vanuatu'),\n" +
                 "('VEN', 'Venezuela, RB'),\n" +
                 "('VNM', 'Vietnam'),\n" +
                 "('VIR', 'Virgin Islands (U.S.)'),\n" +
                 "('PSE', 'West Bank and Gaza'),\n" +
                 "('WLD', 'World'),\n" +
                 "('YEM', 'Yemen, Rep.'),\n" +
                 "('ZMB', 'Zambia'),\n" +
                 "('ZWE', 'Zimbabwe');");
     }
 }