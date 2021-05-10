package com.edson.financas.api.resource;

import com.edson.financas.api.dto.UsuarioDTO;
import com.edson.financas.exception.ErroAutenticacao;
import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.entity.Usuario;
import com.edson.financas.service.LancamentoService;
import com.edson.financas.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

        Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                          .post(API.concat("/autenticar"))
                                                          .accept(JSON)
                                                          .contentType(JSON)
                                                          .content(json);

        mvc.perform(request)
           .andExpect(MockMvcResultMatchers.status().isOk())
           .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
           .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
           .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }

    @Test
    public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception{
        //cenário
        String emai = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(emai).senha(senha).build();
        Mockito.when(service.autenticar(emai, senha)).thenThrow(ErroAutenticacao.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post(API.concat("/autenticar"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

        Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post(API)
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request)
           .andExpect(MockMvcResultMatchers.status().isCreated())
           .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
           .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
           .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

        Mockito.when(service.salvarUsuario((Mockito.any(Usuario.class)))).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post(API)
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request)
           .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
