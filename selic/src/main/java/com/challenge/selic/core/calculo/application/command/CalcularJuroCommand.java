package com.challenge.selic.core.calculo.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CalcularJuroCommand {

    @NonNull
    private ProdutoCmd produto;

    @NonNull
    private CondicaoPagamentoCmd condicaoPagamento;

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class CondicaoPagamentoCmd {

        @NonNull
        private final Double valorEntrada;

        @NonNull
        private final Integer quantidadeParcelas;
    }

    @Data
    @Builder
    public static class ProdutoCmd {

        @NonNull
        private Integer codigo;

        @NonNull
        private String nome;

        @NonNull
        private Double valor;
    }


}
