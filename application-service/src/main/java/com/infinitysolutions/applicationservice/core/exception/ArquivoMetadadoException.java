package com.infinitysolutions.applicationservice.core.exception;

public class ArquivoMetadadoException extends CoreLayerException {
    public ArquivoMetadadoException(String message) {
        super("recurso_existente", message);
    }
    public static com.infinitysolutions.applicationservice.core.exception.ArquivoMetadadoException ErroAoSetarAtributo(String erro) {
        return new com.infinitysolutions.applicationservice.core.exception.ArquivoMetadadoException("Erro ao setar atributo: " + erro);
    }
}
