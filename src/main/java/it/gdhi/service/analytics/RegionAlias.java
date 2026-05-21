package it.gdhi.service.analytics;

import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;

public final class RegionAlias {

    private static final Map<String, String> REGION_IDS_BY_ALIAS = Map.ofEntries(
            Map.entry("afro", "AFRO"),
            Map.entry("afro region", "AFRO"),
            Map.entry("africa", "AFRO"),
            Map.entry("african region", "AFRO"),
            Map.entry("who african region", "AFRO"),
            Map.entry("emro", "EMRO"),
            Map.entry("eastern mediterranean", "EMRO"),
            Map.entry("eastern mediterranean region", "EMRO"),
            Map.entry("euro", "EURO"),
            Map.entry("europe", "EURO"),
            Map.entry("european region", "EURO"),
            Map.entry("paho", "PAHO"),
            Map.entry("americas", "PAHO"),
            Map.entry("region of the americas", "PAHO"),
            Map.entry("searo", "SEARO"),
            Map.entry("south east asia", "SEARO"),
            Map.entry("south-east asia", "SEARO"),
            Map.entry("south-east asia region", "SEARO"),
            Map.entry("wpro", "WPRO"),
            Map.entry("western pacific", "WPRO"),
            Map.entry("western pacific region", "WPRO")
    );

    private RegionAlias() {
    }

    public static String normalize(String regionIdOrName) {
        if (!StringUtils.hasText(regionIdOrName)) {
            return regionIdOrName;
        }
        String normalized = regionIdOrName.trim().toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
        return REGION_IDS_BY_ALIAS.getOrDefault(normalized, regionIdOrName.trim().toUpperCase(Locale.ROOT));
    }
}
