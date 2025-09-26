package com.infinitysolutions.applicationservice.core.exception;

import java.util.UUID;

public class RecursoExistenteException extends CoreLayerException {
    public RecursoExistenteException(String message) {
        super("recurso_existente", message);
    }
    public static com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException cpfJaEmUso(String cpf) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException("CPF já está em uso: " + cpf);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException rgJaEmUso(String rg) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException("RG já está em uso: " + rg);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException cnpjJaEmUso(String cnpj) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException("CNPJ já está em uso: " + cnpj);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException emailJaEmUso(String email) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException("Email já está em uso: " + email);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException credencialJaExiste(UUID idUsuario) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException("Credencial já existe para o usuário com ID: " + idUsuario);
    }
}
