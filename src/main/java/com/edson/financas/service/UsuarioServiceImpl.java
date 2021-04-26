package com.edson.financas.service;

import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.entity.Usuario;
import com.edson.financas.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository repository;

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
        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
