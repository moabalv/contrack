package com.Contrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificacaoNaoExisteException extends RuntimeException {
    public NotificacaoNaoExisteException() {
        super("A notificação não existe.");
    }
}
