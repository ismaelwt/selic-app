package com.challenge.selic.core.calculo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Informação referente ao corpo da requisição para se fazer um cálculo.")
public class CalcularJuroDTO {

    @Valid
    @NotNull(message = "Classe condição pagamento contendo os campos de valor entrada e quantidade de parcelas")
    private ProdutoDTO produto;

    @Valid
    @NotNull(message = "Classe condição pagamento contendo os campos de valor entrada e quantidade de parcelas")
    private CondicaoPagamentoDTO condicaoPagamento;

    @Data
    @NoArgsConstructor(force = true)
    @AllArgsConstructor(staticName = "of")
    @ApiModel(description = "Classe Condição de pagamento.")
    public static final class CondicaoPagamentoDTO {

        @NotNull(message = "Valor entrada não pode estar vazio.")
        @ApiModelProperty(value = "valor da entrada caso existir", example = "5000")
        private final Double valorEntrada;

        @NotNull(message = "Quantidade parcelas não pode estar vazio.")
        @ApiModelProperty(value = "Quantidade de parcelas caso existir | taxa só se aplica para o valor quando pareclado acima de 6x", example = "6")
        private final Integer quantidadeParcelas;
    }

    @Data
    @NoArgsConstructor(force = true)
    @AllArgsConstructor(staticName = "of")
    @ApiModel(description = "Classe Produto")
    public static final class ProdutoDTO {

        @NotNull(message = "Código do produto é um campo requerido.")
        @ApiModelProperty(value = "Código do produto.", required = true, example = "5680015")
        private Integer codigo;

        @NotNull(message = "Nome do produto é um campo requerido.")
        @ApiModelProperty(value = "Nome do produto.", required = true, example = "Produto X")
        private String nome;

        @NotNull(message = "Valor é um campo requerido.")
        @ApiModelProperty(value = "Valor do produto.", required = true, example = "3500")
        private Double valor;

    }

}
