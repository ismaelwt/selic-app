package com.challenge.selic.core.calculo.dominio;

import com.challenge.selic.core.calculo.application.command.CalcularJuroCommand;
import com.challenge.selic.core.calculo.dominio.event.CalculoExecutadoEvent;
import com.challenge.selic.core.calculo.exception.CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException;
import com.challenge.selic.core.calculo.repository.SelicTableClient;
import com.challenge.selic.util.AbstractDomainEvent;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public class Calculo extends AbstractDomainEvent {

    private Double valorProduto;
    private Double valorEntrada;
    private Integer qtdParcelas;
    private SelicTableClient.SelicObject taxaSelic;

    private Double valorComJuros;
    private Double juros;
    private Double valorSemJuros;
    private Double taxaJurosAoMes;

    @Builder
    public Calculo(@NonNull Double valorProduto, @NonNull Double valorEntrada, @NonNull Integer qtdParcelas, SelicTableClient.SelicObject taxaSelic) {

        this.valorProduto = valorProduto;
        this.valorEntrada = valorEntrada;
        this.qtdParcelas = qtdParcelas;
        this.taxaSelic = taxaSelic;
    }

    public Calculo calcularJurosSelic(){

        this.validarEntradaValorProduto();

        var taxa = ( 1 + Double.parseDouble(taxaSelic.getValor()) * this.qtdParcelas);

        var totalComJuros = (this.valorProduto - this.valorEntrada) * taxa;

        var juros = (this.valorProduto -  this.valorEntrada) * Double.parseDouble(taxaSelic.getValor()) * this.qtdParcelas;

        this.valorComJuros = (totalComJuros / this.qtdParcelas);
        this.valorSemJuros = ( (this.valorProduto - this.valorEntrada) / this.qtdParcelas);
        this.taxaJurosAoMes = taxa;
        this.juros = (juros / this.qtdParcelas);

        this.registerEvent(CalculoExecutadoEvent.from(this));

        return this;
    }

    public Calculo calcularSemJuros(){

        this.validarEntradaValorProduto();

        this.valorComJuros = null;
        this.valorSemJuros = (this.valorProduto / this.qtdParcelas);
        this.taxaJurosAoMes = null;
        this.juros = null;

        this.registerEvent(CalculoExecutadoEvent.from(this));

        return this;
    }

    private void validarEntradaValorProduto(){
        if (this.valorProduto <= this.valorEntrada) {
            throw new CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException(); //produto pode ser pago a vista.
        }
    }
}
