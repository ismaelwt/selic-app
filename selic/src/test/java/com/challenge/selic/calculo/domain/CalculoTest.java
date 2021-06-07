package com.challenge.selic.calculo.domain;

import com.challenge.selic.core.calculo.api.dto.ListaParcelaDTO;
import com.challenge.selic.core.calculo.dominio.Calculo;
import com.challenge.selic.core.calculo.dominio.event.CalculoExecutadoEvent;
import com.challenge.selic.core.calculo.exception.CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import com.challenge.selic.util.TestUtils;
import com.challenge.selic.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Dominio | Teste de dominio")
public class CalculoTest {

    @Autowired
    private SelicTableClient selicTableClient;

    @Test
    @DisplayName("Deve Calcular com sucesso")
    public void deveCalcularComSucesso() {

        Set<ListaParcelaDTO> resultSet = new HashSet<>();

        var calculo = Calculo.builder()
                .valorProduto(3500D)
                .valorEntrada(0.0)
                .qtdParcelas(6)
                .taxaSelic(this.getTaxaMesAtual())
                .build();

        calculo.calcularJurosSelic();

        assertNotNull(calculo);
        assertEquals(Utils.format(calculo.getJuros()), 46.44);

        var event = TestUtils.getEvent(calculo.getEvents(), CalculoExecutadoEvent.class);

        assertEquals(event.getJuros(), calculo.getJuros());
        assertEquals(event.getTaxaJurosAoMes(), calculo.getTaxaJurosAoMes());
        assertEquals(event.getValorComJuros(), calculo.getValorComJuros());
        assertEquals(event.getValorSemJuros(), calculo.getValorSemJuros());
    }

    @Test
    @DisplayName("Não deve Calcular com campos obrigatorios nulos")
    public void naoDeveCalcularComCamposNulo() {

        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorProduto(null)
                    .valorEntrada(0.0)
                    .qtdParcelas(6)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });


        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorEntrada(0.0)
                    .qtdParcelas(6)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });
    }

    @Test
    @DisplayName("Deve Calcular com pagamento a vista")
    public void deveReceberAvisoDePagamentoVista() {

        assertThrows(CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException.class, () -> {

            var c = Calculo.builder()
                    .valorProduto(3500D)
                    .valorEntrada(3500D)
                    .qtdParcelas(1)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();


            c.calcularSemJuros();


        });
    }

    @Test
    @DisplayName("Não deve Calcular com campos obrigatorios nulos")
    public void naoDeveCalcularComCamposNulos() {

        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorProduto(3500D)
                    .valorEntrada(0.0)
                    .qtdParcelas(null)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });


        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorProduto(3500D)
                    .valorEntrada(0.0)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });
    }


    @Test
    @DisplayName("Não deve Calcular com campos obrigatorios nulos")
    public void naoDeveCalcularComCamposNulosValorEntrada() {

        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorProduto(3500D)
                    .valorEntrada(null)
                    .qtdParcelas(null)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });


        assertThrows(NullPointerException.class, () -> {

            Calculo.builder()
                    .valorProduto(3500D)
                    .qtdParcelas(5)
                    .taxaSelic(this.getTaxaMesAtual())
                    .build();
        });
    }


    public SelicTableClient.SelicObject getTaxaMesAtual() {

        var sortedList = selicTableClient.fetch().stream().sorted(Comparator.comparing(SelicTableClient.SelicObject::getData)).collect(Collectors.toList());

        var dataAtual = LocalDate.now();

        var taxasDoMes = sortedList.stream().filter(p -> LocalDate.parse(p.getData()).getYear() == (dataAtual.getYear()) &&
                LocalDate.parse(p.getData()).getMonth().equals(dataAtual.getMonth()))
                .collect(Collectors.toList());

        return taxasDoMes.get(taxasDoMes.size() - 1);
    }

}
