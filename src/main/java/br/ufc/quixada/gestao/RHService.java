package br.ufc.quixada.gestao;

import br.ufc.quixada.gestao.model.Funcionario;
import br.ufc.quixada.gestao.contrato.IRHService;

import java.util.List;

public class RHService implements IRHService {


    @Override
    public boolean cadastrar(Funcionario funcionario) {
        return false;
    }

    @Override
    public boolean remover(String cpf) {
        return false;
    }

    @Override
    public Funcionario obterFuncionario(String cpf) {
        return null;
    }

    @Override
    public List<Funcionario> getFuncionarios() {
        return null;
    }

    @Override
    public List<Funcionario> getFuncionariosPorCategoria(Tipo tipo) {
        return null;
    }

    @Override
    public int getTotalFuncionarios() {
        return 0;
    }

    @Override
    public boolean solicitarDiaria(String cpf) {
        return false;
    }

    @Override
    public void partilharLucros(double valor) {}

    @Override
    public void iniciarMes() {

    }

    @Override
    public Double calcularSalarioDoFuncionario(String cpf) {
        return null;
    }

    @Override
    public double calcularFolhaDePagamento() {
        return 0;
    }
}