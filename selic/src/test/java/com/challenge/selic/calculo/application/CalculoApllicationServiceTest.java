package com.challenge.selic.calculo.application;

import com.challenge.selic.config.AdapterConfig;
import com.challenge.selic.core.calculo.application.CalculoApplicationService;
import com.challenge.selic.core.calculo.application.command.CalcularJuroCommand;
import com.challenge.selic.core.calculo.domain.event.CalculoExecutadoEvent;
import com.challenge.selic.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@DisplayName("Application Test | Calculo com Taxa Selic")
public class CalculoApllicationServiceTest extends AdapterConfig {

    @Autowired
    private CalculoApplicationService service;

    @Test
    @DisplayName("Deveria criar command com sucesso")
    public void deveriaCriarComSucessCmd() {

        var cmd = CalcularJuroCommand.builder()
                .produto(CalcularJuroCommand.ProdutoCmd.builder()
                        .codigo(123456)
                        .nome("teste123")
                        .valor(3500D)
                        .build())
                .condicaoPagamento(CalcularJuroCommand.CondicaoPagamentoCmd.of(0.d, 6))
                .build();

        var calculo = service.handle(cmd);

        TestUtils.assertThatEventhasBeenDispatch(calculo.getEvents().stream().toList(), Map.of(CalculoExecutadoEvent.class, 1));
    }

    @Test
    @DisplayName("Deveria retornar selic atual com sucesso")
    public void deveriaRetornarSelicAtualSucess() {

        var selicObject = service.getTaxaMesAtual();
        assertEquals(null, selicObject.getValor(), "0.013269");
    }

    @Test
    @DisplayName("Deveria retornar selic acumulada com sucesso")
    public void deveriaRetornarSelicAcumuladaSucess() {

        var map = service.getTaxaSelicAcumulada();
        assertEquals(null, map.get("valor"), "0.269979");
    }


    @Test
    @DisplayName("Deveria criar command com sucesso sem Juros")
    public void deveriaCriarComSucessSemJurosCmd() {

        var cmd = CalcularJuroCommand.builder()
                .produto(CalcularJuroCommand.ProdutoCmd.builder()
                        .codigo(123456)
                        .nome("teste123")
                        .valor(3500D)
                        .build())
                .condicaoPagamento(CalcularJuroCommand.CondicaoPagamentoCmd.of(0.d, 3))
                .build();

        var calculo = service.handle(cmd);

        TestUtils.assertThatEventhasBeenDispatch(calculo.getEvents().stream().toList(), Map.of(CalculoExecutadoEvent.class, 1));
    }
}
