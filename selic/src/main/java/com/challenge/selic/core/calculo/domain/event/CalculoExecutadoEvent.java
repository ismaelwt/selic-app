package com.challenge.selic.core.calculo.domain.event;

import com.challenge.selic.core.calculo.domain.Calculo;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import com.challenge.selic.util.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CalculoExecutadoEvent implements DomainEvent {

    private Double valorProduto;
    private Double valorEntrada;
    private Integer qtdParcelas;
    private SelicTableClient.SelicObject taxaSelic;

    private Double valorComJuros;
    private Double juros;
    private Double valorSemJuros;
    private Double taxaJurosAoMes;

    public static CalculoExecutadoEvent from(Calculo calculo) {
        return CalculoExecutadoEvent.builder()
                .valorProduto(calculo.getValorProduto())
                .valorEntrada(calculo.getValorEntrada())
                .qtdParcelas(calculo.getQtdParcelas())
                .taxaSelic(calculo.getTaxaSelic())
                .valorComJuros(calculo.getValorComJuros())
                .valorSemJuros(calculo.getValorSemJuros())
                .juros(calculo.getJuros())
                .taxaJurosAoMes(calculo.getTaxaJurosAoMes())
                .build();
    }
}
