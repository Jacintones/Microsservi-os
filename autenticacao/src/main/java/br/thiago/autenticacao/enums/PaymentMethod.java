package br.thiago.autenticacao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {

    CREDIT_CARD("CREDITO"),

    DEBIT_CARD("DEBITO"),

    PIX("PIX"),
    ;

    private final String name;



}
