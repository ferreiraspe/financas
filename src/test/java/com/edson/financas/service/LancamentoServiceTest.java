package com.edson.financas.service;

import com.edson.financas.exception.RegraNegocioException;
import com.edson.financas.model.entity.Lancamento;
import com.edson.financas.model.entity.Usuario;
import com.edson.financas.model.enums.StatusLancamento;
import com.edson.financas.model.repository.LancamentoRepository;
import com.edson.financas.model.repository.LancamentoRepositoryTest;
import com.edson.financas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento(){
        //cenário
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoSalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = service.salvar(lancamentoSalvar);

        //verificação
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
        //cenário
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvar);

        //execução e verificação
        Assertions.catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);

    }

    @Test
    public void deveAtualizarUmLancamento(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validar((lancamento));

        Mockito.when(repository.save(lancamento)).thenReturn(lancamento);

        //execução
        service.atualizar(lancamento);

        //verificação
        Mockito.verify(repository, Mockito.times(1)).save(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        //execução e verificação
        Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarUmLancamento(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        //execução
        service.deletar(lancamento);

        //verificação
        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalva(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        //execução
        Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

        //verificação
        Mockito.verify(repository, Mockito.never()).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamentos(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execução
        List<Lancamento> resultado = service.buscar(lancamento);

        //verificação
        Assertions.assertThat(resultado)
                  .isNotEmpty()
                  .hasSize(1)
                  .contains(lancamento);

    }

    @Test
    public void deveAtualizarStatusDeUmLancamento(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execução
        service.atualizarStatus(lancamento, novoStatus);

        //verificação
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);
    }

    @Test
    public void deveObterUmLancamentoPorId(){
        //cenário
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execução
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificação
        Assertions.assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioQuandoLancamentoNaoExiste(){
        //cenário
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificação
        Assertions.assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void deveLancarErrosAoValidarUmLancamento(){
        //cenário
        Lancamento lancamento = new Lancamento();

        Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
                                   .hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
                .hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Salario");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(0);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(13);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(1);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(202);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.setUsuario(new Usuario());

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.getUsuario().setId(1l);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.ZERO);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.valueOf(1));

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lançamento.");


    }

}
