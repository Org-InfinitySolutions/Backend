package com.infinitysolutions.applicationservice.core.exception;

public class EstrategiaNaoEncontradaException extends CoreLayerException {
    public EstrategiaNaoEncontradaException(String message) {
        super("estragegia_nao_encontrada", message);
    }
}
