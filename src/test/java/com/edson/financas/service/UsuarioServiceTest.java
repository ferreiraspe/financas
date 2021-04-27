package com.edson.financas.service;

import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {


    UsuarioService service;
    UsuarioRepository repository;

    @BeforeEach
    public void setUp(){
        repository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(repository);

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
        Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
    }

}
