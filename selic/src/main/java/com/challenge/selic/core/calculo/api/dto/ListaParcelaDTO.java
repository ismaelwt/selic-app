package com.challenge.selic.core.calculo.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(description = "Informação referente ao retorno do cálculo.")
public class ListaParcelaDTO {

    private Integer numeroParcela;
    private Double valorComJuros;
    private Double valorSemJuros;
    private Double taxaJurosAoMes;
    private Double valorTaxaJurosAoMes;
}
