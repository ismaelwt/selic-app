package com.challenge.selic.core.calculo.api;

import com.challenge.selic.core.calculo.api.dto.CalcularJuroDTO;
import com.challenge.selic.core.calculo.api.dto.ListaParcelaDTO;
import com.challenge.selic.core.calculo.application.CalculoApplicationService;
import com.challenge.selic.core.calculo.application.command.CalcularJuroCommand;
import com.challenge.selic.core.calculo.exception.CalculoConstraintException;
import com.challenge.selic.util.Utils;
import com.challenge.selic.util.ValidateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping(path = CalculoController.PATH, produces = APPLICATION_JSON_VALUE)
public class CalculoController {

    public static final String PATH = "api/v1/calcularJuros";

    @Autowired
    private final ValidateService validateService;

    @Autowired
    private final CalculoApplicationService calculoService;

    @PostMapping
    @ApiOperation(value = "Recebe um produto e condição de pagamento para a realização de um cálculo da taxa selic.", httpMethod = "POST", consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "cálculo realizado com sucesso"), @ApiResponse(code = 400, message = "o cálculo falhou")})
    public Collection<ListaParcelaDTO> create(@RequestBody CalcularJuroDTO dto) {

        this.validateService.validate(dto).ifPresent(violations -> {
            throw new CalculoConstraintException(violations);
        });

        var produto = CalcularJuroCommand.ProdutoCmd.builder()
                .nome(dto.getProduto().getNome())
                .codigo(dto.getProduto().getCodigo())
                .valor(dto.getProduto().getValor())
                .build();

        var condicaoPagamento = CalcularJuroCommand.CondicaoPagamentoCmd.of(dto.getCondicaoPagamento().getValorEntrada(), dto.getCondicaoPagamento().getQuantidadeParcelas());

        var cmd = CalcularJuroCommand.builder()
                .produto(produto)
                .condicaoPagamento(condicaoPagamento)
                .build();

        var calculo = calculoService.handle(cmd);

        var resultSet = new ArrayList<ListaParcelaDTO>();

        for (int i = 0; i < cmd.getCondicaoPagamento().getQuantidadeParcelas(); i++) {


            resultSet.add(ListaParcelaDTO.builder().numeroParcela(i + 1)
                    .taxaJurosAoMes(Utils.format(calculo.getTaxaJurosAoMes()))
                    .valorComJuros(Utils.format(calculo.getValorComJuros()))
                    .valorSemJuros(Utils.format(calculo.getValorSemJuros()))
                    .valorTaxaJurosAoMes(Utils.format(calculo.getJuros()))
                    .build());

        }

        return resultSet;
    }
}
