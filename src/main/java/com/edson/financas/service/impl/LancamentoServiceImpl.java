package com.edson.financas.service.impl;

import com.edson.financas.model.entity.Lancamento;
import com.edson.financas.model.enums.StatusLancamento;
import com.edson.financas.model.repository.LancamentoRepository;
import com.edson.financas.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Override
    public Lancamento salvar(Lancamento lancamento) {
        return repository.save(lancamento);
    }

    @Override
    public Lancamento atualizar(Lancamento lancamento) {
        return null;
    }

    @Override
    public void deletar(Lancamento lancamento) {

    }

    @Override
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        return null;
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {

    }
}
