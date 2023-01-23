package it.gdhi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CountryDTO {

    private String id;
    private String name;
    private UUID uniqueId;
    private String alpha2Code;

}
