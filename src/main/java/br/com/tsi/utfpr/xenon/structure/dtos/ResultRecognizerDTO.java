package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResultRecognizerDTO {
    private CarResultDTO car;
    private Float confidence;
}
