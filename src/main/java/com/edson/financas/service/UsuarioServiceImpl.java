package com.edson.financas.service;

import com.edson.financas.model.entity.Usuario;
import com.edson.financas.model.repository.UsuarioRepository;

public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

    }
}
