package com.infinitysolutions.applicationservice.core.exception;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;

public class RecursoNaoProcessavelException extends CoreLayerException {
    public RecursoNaoProcessavelException(String message) {
        super("recurso_nao_processavel", message);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException usuarioComTipoIncorreto(TipoUsuario tipoUsuario){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException("O usuário possui um tipo incorreto para esta operação, TIPO: " + tipoUsuario.name());
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException credencialJaPossuiCargo(Cargo cargo){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException("O usuário já possui o cargo: " + cargo.getNome());
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException credencialNaoPossuiCargo(Cargo cargo){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException("O usuário não possui o cargo: " + cargo.getNome());
    }
}
