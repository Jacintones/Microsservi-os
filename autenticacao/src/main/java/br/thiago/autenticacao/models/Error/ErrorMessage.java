package br.thiago.autenticacao.models.Error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String title;

    private Integer status;

    private String message;
}
