package com.edson.financas.service;

import com.edson.financas.exception.ErroAutenticacao;
import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.entity.Usuario;
import com.edson.financas.model.repository.UsuarioRepository;
import com.edson.financas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void deveSalvarUmUsuario(){
        //cenário
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                                 .id(1L)
                                 .nome("nome")
                                 .email("email@email.com")
                                 .senha("senha")
                                 .build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        //verificação
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");

    }
/*
    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        //cenário
        //TODO não está passando
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        //ação
        service.salvarUsuario(usuario);

        //verificação
        Mockito.verify(repository, Mockito.never()).save(usuario);
        assertThrows(RegraNegocioException.class, () -> Mockito.verify(repository, Mockito.never()).save(usuario));

    }
    */
    @Test
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //ação
        Usuario result = service.autenticar(email, senha);

        //verificação
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
        //cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //ação
        RuntimeException runtimeException = assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "senha"));

        //verificação
        org.assertj.core.api.Assertions.assertThat(runtimeException).isInstanceOf(ErroAutenticacao.class)
                                                                    .hasMessage("Usuário não encontrado para o email informado!");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        //cenário
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        //ação
        RuntimeException runtimeException = assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "123"));

        //verificação
        org.assertj.core.api.Assertions.assertThat(runtimeException).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
    }

    @Test
    public void deveValidarEmail(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //ação
        Assertions.assertDoesNotThrow(() -> service.validarEmail(("email@email.com")));
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //ação
        assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
    }

}
