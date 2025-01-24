package br.ufc.quixada.gestao.contrato;

import java.util.List;

import br.ufc.quixada.gestao.model.Funcionario;

/**
 * @author bruno
 *
 * Vamos modelar o controle de pagamento de uma empresa privada de ensino.
 *
 * Nessa universidade, existem tres tipos de funcionarios:
 * "prof", "sta", "terc" representando  respectivamente
 * professores, servidores tecnico administrativos e  terceirizados.
 *
 * Para que o pagamento seja realizado todo funcionario deve ter
 * seu cpf e nome cadastrado na folha de pagamento.
 *
 * Professores sao classificados em classes(A, B, C, D, E). A classe
 * a qual o professor pertence influencia diretamente no seu salario.
 *
 * STAs sao classificados em niveis, que vai do 1 ate 30.O nivel influencia
 * no salario do STA.
 *
 * Terceirizados podem receber por insalubridade ou nao.
 *
 * Nessa empresa o salario ao fim dos mes e calculado da seguinte forma:
 *
 *  	SalarioBase + DivisaoNosLucros + Diarias
 *
 *  Nem todo mundo tem direito a diarias. Veja as regras ao longo do arquivo.
 *
 *
 */
public interface IRHService{

    enum Tipo{PROF, STA, TERC};

    /**
     * Adiciona um funcionario na folha de pagamento
     *
     * @param funcionario Funcionario que deve ser adicionado
     * @return false caso o usuario ja tiver sido adicionado,
     * true caso contrario
     */
    boolean cadastrar(Funcionario funcionario);

    /**
     * Remove o funcionario do folha de pagamento
     *
     * @param cpf cpf do funcionario que deve ser removido
     * @return false se o usuario nao tiver cadastrado
     * true caso contrario
     */
    boolean remover(String cpf);


    /**
     * Retorna o funcionario de acordo com o cpf
     *
     * @param cpf do usuario
     * @return usuario caso ele esteja cadastrado,
     * caso contrario retorna null
     */
    Funcionario obterFuncionario(String cpf);

    /**
     * Retorna todos os usuarios cadastrados
     *
     * @return lista com todos os usuarios
     */
    List<Funcionario> getFuncionarios();

    /**
     * Retorna a lista de funcionario por tipo
     *
     * @param tipo de funcionario que devem ser retornados
     * @return lista de funcionario do tipo indicado
     */
    List<Funcionario> getFuncionariosPorCategoria(IRHService.Tipo tipo);


    /**
     * @return quantidade de funcionarios cadastrados
     */
    int getTotalFuncionarios();

    /**
     * Adiciona uma diaria ao funcionario indicado pelo cpf
     *
     * Regras das diarias:
     * 	Professores tem direito de ATE TRES diarias
     * 	STAs tem direito apenas a UMA diaria
     * 	Terceirados nao tem direito a diarias
     *
     * Cada diaria vale 100 reais
     *
     * Se a diaria nao for aplicavel false deve ser retornado
     *
     *
     * @param cpf do funcionario o qual a diaria seria adicionada
     * @return true caso a diaria seja adicionada e false caso contrario
     */
    boolean solicitarDiaria(String cpf);

    /**
     * Divide o lucro entre os funcionarios da empresa
     *
     * O lucro deve ser dividido igualmente entre os funcionarios.
     * Ex: Gratificacao de 500 reais. Se existirem 5 funcionarios,
     * cada funcionario deve receber 100 reais.
     *
     * @param valor do lucro a ser partilhado
     *
     */
    void partilharLucros(double valor);


    /**
     * Remove as diarias e a participacao do lucro de todos os funcionarios
     */
    void iniciarMes();

    /**
     * Calcula o salario do funcionario dono do cpf informado
     *
     * Regras:
     *
     * Professores: A "classe" e um char que pode ser A, B, C, D ou E.
     * O salario dos classes A, B, C, D, E Ã© respectivamente 3000, 5000, 7000, 9000 e 11000 reais.
     *
     * STA: O "nivel" e um int entre 1 e 30. O salario e calculado como 1000 + 100 * nivel;
     *
     * Terceirizado: Insalubridade e um boolean que define as condicoes de trabalho.
     * O salario e 1000 sem insalubridade e 1500 com insalubridade.
     *
     * As diarias devem ser incluidas no salario, bem como a participacao nos lucros
     *
     * @param cpf
     * @return salario do funcionario dono do cpf e null se o
     * funcionario nao estiver cadastrado
     */
    Double calcularSalarioDoFuncionario(String cpf);


    /**
     * @return a soma dos salarios de todos os funcionarios
     */
    double calcularFolhaDePagamento();
}