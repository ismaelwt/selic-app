package com.challenge.selic.calculo.api;

import com.challenge.selic.config.Config;
import com.challenge.selic.core.calculo.api.CalculoController;
import com.challenge.selic.core.calculo.api.TaxaSelicController;
import com.challenge.selic.core.calculo.api.dto.CalcularJuroDTO;
import com.challenge.selic.core.calculo.application.command.CalcularJuroCommand;
import com.challenge.selic.core.calculo.dominio.Calculo;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import com.challenge.selic.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API Test | Calculo com Taxa Selic")
public class CalculoAPITest extends Config {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deveria enviar um request com os parametros de produto e condição de pagamaneto e ser executado com sucesso.")
    public void deveCalcularComSucesso() throws Exception {

        var calculo = Calculo.builder()
                .valorProduto(3500D)
                .valorEntrada(0.0)
                .qtdParcelas(6)
                .taxaSelic(this.getTaxaMesAtual())
                .build();

        calculo.calcularJurosSelic();

        var produto = CalcularJuroDTO.ProdutoDTO.of(123, "Produto 1", 3500D);

        var condicaoPagamento = CalcularJuroDTO.CondicaoPagamentoDTO.of(0.0, 6);

        var dto = CalcularJuroDTO.builder()
                .produto(produto)
                .condicaoPagamento(condicaoPagamento)
                .build();

        var cmd = CalcularJuroCommand.builder()
                .produto(CalcularJuroCommand.ProdutoCmd.builder()
                        .codigo(dto.getProduto().getCodigo())
                        .nome(dto.getProduto().getNome())
                        .valor(dto.getProduto().getValor())
                        .build())
                .condicaoPagamento(CalcularJuroCommand.CondicaoPagamentoCmd.of(dto.getCondicaoPagamento().getValorEntrada(), dto.getCondicaoPagamento().getQuantidadeParcelas()))
                .build();

        Mockito.when(calculoApplicationService.handle(cmd)).thenReturn(calculo);


        mockMvc.perform(request(HttpMethod.POST, "/" + CalculoController.PATH).contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJson(dto)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(calculoApplicationService).handle(cmd);

    }

    @Test
    @DisplayName("Deveria enviar um request com os parametros de produto e condição de pagamaneto e ser executado com sucesso sem juros.")
    public void deveCalcularComSucessoSemJuros() throws Exception {

        var calculo = Calculo.builder()
                .valorProduto(3500D)
                .valorEntrada(0.0)
                .qtdParcelas(6)
                .taxaSelic(this.getTaxaMesAtual())
                .build();

        calculo.calcularSemJuros();

        var produto = CalcularJuroDTO.ProdutoDTO.of(123, "Produto 1", 3500D);

        var condicaoPagamento = CalcularJuroDTO.CondicaoPagamentoDTO.of(0.0, 3);

        var dto = CalcularJuroDTO.builder()
                .produto(produto)
                .condicaoPagamento(condicaoPagamento)
                .build();

        var cmd = CalcularJuroCommand.builder()
                .produto(CalcularJuroCommand.ProdutoCmd.builder()
                        .codigo(dto.getProduto().getCodigo())
                        .nome(dto.getProduto().getNome())
                        .valor(dto.getProduto().getValor())
                        .build())
                .condicaoPagamento(CalcularJuroCommand.CondicaoPagamentoCmd.of(dto.getCondicaoPagamento().getValorEntrada(), dto.getCondicaoPagamento().getQuantidadeParcelas()))
                .build();

        Mockito.when(calculoApplicationService.handle(cmd)).thenReturn(calculo);

        mockMvc.perform(request(HttpMethod.POST, "/" + CalculoController.PATH).contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJson(dto)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(calculoApplicationService).handle(cmd);

    }

    @Test
    @DisplayName("Deveria enviar um request para pegar a taxa do mes atual ")
    public void deveReceberRequestTaxaMesAtual() throws Exception {

        var obj = SelicTableClient.SelicObject.builder()
                .valor("7.15")
                .data("2021/01/01")
                .build();

        Mockito.when(calculoApplicationService.getTaxaMesAtual()).thenReturn(obj);

        mockMvc.perform(request(HttpMethod.GET, "/" + TaxaSelicController.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(calculoApplicationService).getTaxaMesAtual();

    }

    @Test
    @DisplayName("Deveria enviar um request para pegar a taxa acumulada dos ultimos 30 dias ")
    public void deveReceberRequestTaxaUltimos30Dias() throws Exception {

        Map map = new HashMap<>();

        map.put("data", LocalDate.now().minusMonths(1) + " - " + LocalDate.now());
        map.put("valor", "0,26");

        Mockito.when(calculoApplicationService.getTaxaSelicAcumulada()).thenReturn(map);

        mockMvc.perform(request(HttpMethod.GET, "/" + TaxaSelicController.PATH + "/selic-acumulada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(calculoApplicationService).getTaxaSelicAcumulada();

    }

    @Test
    @DisplayName("Não Deveria enviar um request com os parametros obrigatorios nulos")
    public void naoDeveCalcularComSucesso() throws Exception {

        var produto = CalcularJuroDTO.ProdutoDTO.of(null, null, null);

        var condicaoPagamento = CalcularJuroDTO.CondicaoPagamentoDTO.of(null, null);

        var dto = CalcularJuroDTO.builder()
                .produto(produto)
                .condicaoPagamento(condicaoPagamento)
                .build();

        var violation = List.of("com.challenge.selic.core.calculo.api.dto.CalcularJuroDTO");

        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/" + CalculoController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJson(dto)))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(violation);

    }

    public SelicTableClient.SelicObject getTaxaMesAtual() {

        return SelicTableClient.SelicObject.builder()
                .data("202-06-05")
                .valor("0.013269")
                .build();
    }
}
