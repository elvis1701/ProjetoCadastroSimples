package com.sistema.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Criptografia {

    public static String criptografarSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de criptografia não disponível", e);
        }
    }

    public static boolean verificarSenha(String senha, String senhaCriptografada) {
        return criptografarSenha(senha).equals(senhaCriptografada);
    }
}