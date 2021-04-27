package com.edson.financas.model.repository;

import com.edson.financas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail(){
        //cenário

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        //cenário
        Usuario usuario = criarUsuario();

        //ação
        Usuario usuarioSalvo = repository.save(usuario);

        //verificação
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUsuarioPorEmail(){
        //cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //verificação
        Optional<Usuario> result = repository.findByEmail("email@email.com");

        Assertions.assertThat(result.isPresent()).isTrue();

    }

    public static Usuario criarUsuario(){
        return Usuario.builder()
                .nome("usuario")
                .email("email@email.com")
                .senha("senha")
                .build();
    }
}
