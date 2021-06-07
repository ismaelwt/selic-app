package com.challenge.selic.core.calculo.api;

import com.challenge.selic.core.calculo.application.CalculoApplicationService;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping(path = TaxaSelicController.PATH, produces = APPLICATION_JSON_VALUE)
public class TaxaSelicController {

    public static final String PATH = "api/v1/taxaSelic";

    @Autowired
    private final CalculoApplicationService calculoService;

    @GetMapping
    @ApiOperation(value = "devolve a taxa selic atual ", httpMethod = "GET",  produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "request de taxa selic enviado sucesso"), @ApiResponse(code = 400, message = "request de taxa selic falhou")})
    public SelicTableClient.SelicObject get() {
        return this.calculoService.getTaxaMesAtual();
    }

    @GetMapping("/selic-acumulada")
    @ApiOperation(value = "devolve a taxa selic acumulada nos ultimos 30 dias ", httpMethod = "GET",  produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "request de taxa selic enviado sucesso"), @ApiResponse(code = 400, message = "request de taxa selic falhou")})
    public Map<String, String> getAcumulada() {
        return this.calculoService.getTaxaSelicAcumulada();
    }
}
