package com.infinitysolutions.applicationservice.core.exception;

public class CredencialException extends CoreLayerException {
    public CredencialException(String message) {
        super("credencial_invalida", message);
    }

    public static CredencialException emailExistente(String email) {
        return new CredencialException("Email já cadastrado " + email);
    }


    public static CredencialException usuarioIdExistente(String id) {
        return new CredencialException("Já existe uma credencial de um usuario com esse ID " + id);
    }

    public static CredencialException credencialNaoEncontrada(String id) {
        return new CredencialException("credencial não encontrada para o id: " + id);
    }
}
