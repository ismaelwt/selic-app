package com.challenge.selic.core.calculo.application;

import com.challenge.selic.core.calculo.api.dto.ListaParcelaDTO;
import com.challenge.selic.core.calculo.application.command.CalcularJuroCommand;
import com.challenge.selic.core.calculo.dominio.Calculo;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import com.challenge.selic.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalculoApplicationService {

    private final SelicTableClient selicTableClient;

    public Calculo handle(CalcularJuroCommand cmd) {

        if (cmd.getCondicaoPagamento().getQuantidadeParcelas() >= 6) {

            Set<ListaParcelaDTO> resultSet = new HashSet<>();

            var calculo = Calculo.builder()
                    .valorProduto(cmd.getProduto().getValor())
                    .valorEntrada(cmd.getCondicaoPagamento().getValorEntrada())
                    .qtdParcelas(cmd.getCondicaoPagamento().getQuantidadeParcelas())
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();

            return calculo.calcularJurosSelic();
        }

        Set<ListaParcelaDTO> resultSet = new HashSet<>();

        var calculo = Calculo.builder()
                .valorProduto(cmd.getProduto().getValor())
                .valorEntrada(cmd.getCondicaoPagamento().getValorEntrada())
                .qtdParcelas(cmd.getCondicaoPagamento().getQuantidadeParcelas())
                .build();

        return calculo.calcularSemJuros();

    }

    public SelicTableClient.SelicObject getTaxaMesAtual() {

        var sortedList = selicTableClient.fetch().stream().sorted(Comparator.comparing(SelicTableClient.SelicObject::getData)).collect(Collectors.toList());

        var dataAtual = LocalDate.now();

        var taxasDoMes = sortedList.stream().filter(p -> LocalDate.parse(p.getData()).getYear() == (dataAtual.getYear()) &&
                LocalDate.parse(p.getData()).getMonth().equals(dataAtual.getMonth()))
                .collect(Collectors.toList());

        return taxasDoMes.get(taxasDoMes.size() - 1);
    }

    public Map<String, String> getTaxaSelicAcumulada() {

        var sortedList = selicTableClient.fetch().stream().sorted(Comparator.comparing(SelicTableClient.SelicObject::getData)).collect(Collectors.toList());

        var dataAtual = LocalDate.now();

        var taxasDoMes = sortedList.stream().filter(p -> LocalDate.parse(p.getData()).getYear() == (dataAtual.getYear()) &&
                LocalDate.parse(p.getData()).getMonth().equals(dataAtual.minusMonths(1).getMonth()))
                .collect(Collectors.toList());

        var sum = taxasDoMes.stream()
                .map(x -> Double.parseDouble(x.getValor()))
                .reduce(0.0, Double::sum);

        var date = LocalDate.now().minusMonths(1) + " - " + LocalDate.now();

        return Map.of("data", date, "valor", sum.toString());
    }
}
