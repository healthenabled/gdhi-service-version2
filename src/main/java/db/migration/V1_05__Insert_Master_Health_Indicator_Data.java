package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class V1_05__Insert_Master_Health_Indicator_Data   {

    private LinkedHashMap<String, String> indicators = new LinkedHashMap<String, String>() {{
        put("Digital health prioritized at the national level through dedicated bodies / mechanisms for governance", "Does the country have a separate department / agency / national working group for digital health?");
        put("Digital Health prioritized at the national level through planning", "Is digital health included and budgeted for in national health or relevant national strategies and/or plan(s)?");
        put("National eHealth/ Digital Health Strategy or Framework", "Does the country have an eHealth or digital health strategy or framework and a costed digital health plan");
        put("Public funding for digital health", "What is the estimated percent (%) of the annual public spending on health committed to information and communications technology (ICT) ?");
        put("Legal Framework for Data Protection (Security)", "Is there a law on data security (storage, transmission, use) that is relevant to digital health?");
        put("Laws or Regulations for privacy, confidentiality and acess to health information (Privacy)", "Is there a law to protect individual privacy, governing ownership, access and sharing of individually identifiable digital health data ?");
        put("Protocol for regulating or certifying devices and/or digital health services", "Are there protocols, policies, frameworks or accepted processes governing the clinical and patient care use of connected medical devices and digital health services (e.g. telemedicine, applications), particularly in relation to safety, data integrity and quality of care?");
        put("Cross-border data security and sharing", "Are there protocols, policies, frameworks or accepted processes in place to support secure cross-border data exchange and storage? This includes health-related data coming into a country, going out of a country, and/or being used in a country related to an individual from another country.");
        put("Digital health integrated in health and related professional pre-service training (prior to deployment)", "Is digital health part of curriculum for health and health-related support professionals in training (as defined below)? [Break out this indicator to measure training across each of the following cadres: 1) community health workers, 2) nurses, 3) doctors, 4) allied health professions, health managers/administrators, and technologists]");
        put("Digital health integrated in health and related professional in-service training (after deployment)", "Is digital health part of curriculum for health and health-related support professionals in the workforce (as defined below)? [Defined as community health workers, nurses, doctors, allied health, health managers/administrators, and technologists]");
        put("Training of digital health work force", "Is training in digital health / health informatics / health information systems / biomedical informatics degree programs (in either public or private institutions) producing trained digital health workers?");
        put("Maturity of public sector digital health professional careers", "Are there public sector professional titles and career paths in digital health?");
        put("National digital health architecture and/or health information exchange", "Is there a national digital health (eHealth) architectural framework established?");
        put("Health information standards", "Are there digital health / health information standards for data exchange, transmission, messaging, security, privacy, and hardware?");
        put("Network readiness", "Extract the WEF Network Readiness Index score");
        put("Planning and support for ongoing digital health infrastructure maintenance", "Is there an articulated plan for supporting digital health infrastructure (including equipment- computers/ tablets/ phones, supplies, software, devices, etc.) provision and maintenance?");
        put("Nationally scaled digital health systems", "Public sector priorities (eg. 14 domains included in ISO TR 14639) are supported by nationally-scaled digital health systems. (Use separate worksheet to determine the country\"s specified priority areas, whether digital systems are in place, and whether those systems are national-scale.)  [eg. Country X chooses 4 priority areas, uses digital systems to address 2 of the 4, with only 1 being at national scale, receives a score of 25%.]");
        put("Identity management of service providers, administrators, and facilities for Digital Health, including location data for GIS mapping ", "Are health system digital registries of uniquely identifiable providers, administrators, and public facilities (and private if) available, accessible and current? Is the data geotagged to enable GIS mapping?");
        put("Identity management of individuals for Digital Health", "");
    }};

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        int i =1;
        for (Map.Entry<String, String> stringStringEntry : indicators.entrySet()) {
            jdbcTemplate.update("INSERT INTO master.health_indicators (indicator_id, name, definition) VALUES (?, ?,?);",
                    i++, stringStringEntry.getKey(), stringStringEntry.getValue());
        }
    }
}
