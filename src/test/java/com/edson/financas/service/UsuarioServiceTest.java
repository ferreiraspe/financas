package com.edson.financas.service;

import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.entity.Usuario;
import com.edson.financas.model.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveValidarEmail(){
        //cenário
        repository.deleteAll();

        //ação
        Assertions.assertDoesNotThrow(() -> service.validarEmail(("email@email.com")));
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
        repository.save(usuario);

        //ação
        Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
    }

}
