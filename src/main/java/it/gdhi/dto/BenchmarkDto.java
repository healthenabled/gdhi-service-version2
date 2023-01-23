package it.gdhi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkDto {
    private Integer benchmarkScore;
    private String benchmarkValue;
}
