import br.ufc.quixada.gestao.model.Funcionario;
import br.ufc.quixada.gestao.model.Professor;
import br.ufc.quixada.gestao.model.STA;
import br.ufc.quixada.gestao.model.Terceirizado;
import br.ufc.quixada.gestao.RHService;
import br.ufc.quixada.gestao.contrato.IRHService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gestão universitária")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class RHServiceTest {
    private Professor profJonas, profAlessio;
    private STA staLacerda, staMiriam;
    private Terceirizado tercCarla, tercAdriana;

    private String cpfJonas = "16";
    private String cpfAlessio = "15";
    private String cpfLacerda = "23";
    private String cpfMiriam = "43";
    private String cpfCarla = "12";
    private String cpfAdriana = "78";
    private String cpfNulo = "99";

    private IRHService rh;

    @BeforeEach
    public void setUp(){
        rh = new RHService();
        profJonas = new Professor(cpfJonas,  "Jonas", 'C');//salario 7000
        profAlessio = new Professor(cpfAlessio, "Alessio", 'B');//salario 5000

        staMiriam = new STA(cpfMiriam, "Miriam", 10);//salario 2000
        staLacerda = new STA(cpfLacerda, "Lacerda", 5);//salario 1500

        tercCarla = new Terceirizado(cpfCarla, "Carla", false);//salario 1000
        tercAdriana = new Terceirizado(cpfAdriana, "Adriana", true);//salario 1500
    }

    private void inserirFuncionarios() {
        assertTrue(rh.cadastrar(profAlessio), "O professor deveria ter sido adicionado");
        assertTrue(rh.cadastrar(profJonas), "O professor deveria ter sido adicionado");

        assertTrue(rh.cadastrar(staMiriam), "O STA deveria ter sido adicionado");
        assertTrue(rh.cadastrar(staLacerda), "O STA deveria ter sido adicionado");

        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertTrue(rh.cadastrar(tercAdriana), "O terceirizado deveria ter sido adicionado");
    }

    // ---------------------------------------------------------------- Modelo

    @Test
    @DisplayName("Modelo: Funcionario deve ser uma classe abstrata")
    public void funcionarioAbstrato(){
        assertTrue(Modifier.isAbstract(Funcionario.class.getModifiers()),
                "Funcionario deve ser abstrata (veja o diagrama): nao existe um funcionario que nao seja professor, STA ou terceirizado");
    }

    @Test
    @DisplayName("Modelo: os atributos das classes de funcionario devem ser privados")
    public void atributosPrivados(){
        for (Class<?> classe : List.of(Funcionario.class, Professor.class, STA.class, Terceirizado.class)) {
            for (Field atributo : classe.getDeclaredFields()) {
                if (atributo.isSynthetic()) continue;
                assertTrue(Modifier.isPrivate(atributo.getModifiers()),
                        "O atributo '" + atributo.getName() + "' de " + classe.getSimpleName() + " deveria ser privado");
            }
        }
    }

    @Test
    @DisplayName("Modelo: nome e CPF informados no construtor devem ser retornados pelos getters")
    public void gettersDeNomeECpf(){
        assertEquals("Jonas", profJonas.getNome(), "getNome() do professor incorreto");
        assertEquals(cpfJonas, profJonas.getCpf(), "getCpf() do professor incorreto");
        assertEquals("Miriam", staMiriam.getNome(), "getNome() do STA incorreto");
        assertEquals(cpfMiriam, staMiriam.getCpf(), "getCpf() do STA incorreto");
        assertEquals("Carla", tercCarla.getNome(), "getNome() do terceirizado incorreto");
        assertEquals(cpfCarla, tercCarla.getCpf(), "getCpf() do terceirizado incorreto");
    }

    @ParameterizedTest(name = "classe {0} -> R$ {1}")
    @CsvSource({"A, 3000", "B, 5000", "C, 7000", "D, 9000", "E, 11000"})
    @DisplayName("Modelo: salário base do professor depende da classe")
    public void salarioBaseProfessor(char classe, double salarioEsperado){
        assertEquals(salarioEsperado, new Professor(cpfNulo, "Fulano", classe).getSalarioBase(), 0.01,
                "Salario base incorreto para professor classe " + classe);
    }

    @ParameterizedTest(name = "nível {0} -> R$ {1}")
    @CsvSource({"1, 1100", "5, 1500", "10, 2000", "30, 4000"})
    @DisplayName("Modelo: salário base do STA é 1000 + 100 × nível")
    public void salarioBaseSTA(int nivel, double salarioEsperado){
        assertEquals(salarioEsperado, new STA(cpfNulo, "Fulano", nivel).getSalarioBase(), 0.01,
                "Salario base incorreto para STA nivel " + nivel);
    }

    @Test
    @DisplayName("Modelo: salário base do terceirizado depende da insalubridade")
    public void salarioBaseTerceirizado(){
        assertEquals(1000.0, tercCarla.getSalarioBase(), 0.01, "Terceirizado sem insalubridade recebe 1000");
        assertEquals(1500.0, tercAdriana.getSalarioBase(), 0.01, "Terceirizado com insalubridade recebe 1500");
    }

    // -------------------------------------------------------------- Cadastro

    @Test
    @DisplayName("Cadastro: deve cadastrar terceirizados")
    public void cadastrarTerceirizado(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(tercAdriana), "O terceirizado deveria ter sido adicionado");
        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertEquals(2, rh.getTotalFuncionarios(), "O RH deveria ter dois funcionarios registrados");
    }

    @Test
    @DisplayName("Cadastro: deve cadastrar STA")
    public void cadastrarSTA(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(staMiriam), "O STA deveria ter sido adicionado");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    @DisplayName("Cadastro: deve cadastrar professor")
    public void cadastrarProfessor(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(profJonas), "O Professor deveria ter sido adicionado");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    @DisplayName("Cadastro: não deve aceitar dois funcionários com o mesmo CPF")
    public void cadastrarFuncionarioDuplicado(){
        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertFalse(rh.cadastrar(tercCarla), "Nao deve ser possivel adicionar o mesmo funcionario duas vezes");
        assertFalse(rh.cadastrar(new Professor(cpfCarla, "claudio", 'C')), "Nao deve ser possivel adicionar outro funcionario com um CPF ja cadastrado");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    @DisplayName("Cadastro: não deve aceitar professor com classe inválida")
    public void cadastrarProfessorComClasseInvalida(){
        assertFalse(rh.cadastrar(new Professor(cpfNulo, "claudio", 'X')), "Nao podemos cadastrar professor com classe invalida");
        assertFalse(rh.cadastrar(new Professor(cpfNulo, "claudio", 'F')), "Nao podemos cadastrar professor com classe invalida");
        assertEquals(0, rh.getTotalFuncionarios(), "Funcionario cadastrado indevidamente");
    }

    @Test
    @DisplayName("Cadastro: não deve aceitar STA com nível inválido")
    public void cadastrarSTAComNivelInvalido(){
        assertFalse(rh.cadastrar(new STA(cpfNulo, "claudio", 35)), "Nao podemos cadastrar sta com nivel invalido");
        assertFalse(rh.cadastrar(new STA(cpfNulo, "claudio", 0)), "O nivel minimo do STA e 1");
        assertFalse(rh.cadastrar(new STA(cpfNulo, "claudio", 31)), "O nivel maximo do STA e 30");
        assertEquals(0, rh.getTotalFuncionarios(), "Funcionario cadastrado indevidamente");
    }

    @Test
    @DisplayName("Cadastro: deve aceitar os valores limite de classe (A e E) e de nível (1 e 30)")
    public void cadastrarValoresLimite(){
        assertTrue(rh.cadastrar(new Professor("1", "Ana", 'A')), "A classe A e valida");
        assertTrue(rh.cadastrar(new Professor("2", "Bia", 'E')), "A classe E e valida");
        assertTrue(rh.cadastrar(new STA("3", "Caio", 1)), "O nivel 1 e valido");
        assertTrue(rh.cadastrar(new STA("4", "Davi", 30)), "O nivel 30 e valido");
        assertEquals(4, rh.getTotalFuncionarios(), "Os quatro funcionarios deveriam ter sido cadastrados");
    }

    // --------------------------------------------------------------- Remoção

    @Test
    @DisplayName("Remoção: deve remover funcionários cadastrados")
    public void removerFuncionario(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfLacerda), "Deve ser possivel remover funcionario cadastrado");
        assertTrue(rh.remover(cpfAdriana), "Deve ser possivel remover funcionario cadastrado");
        assertTrue(rh.remover(cpfAlessio), "Deve ser possivel remover funcionario cadastrado");
        assertFalse(rh.remover(cpfNulo), "Nao e possivel remover um usuario nao cadastrado");
        assertEquals(3, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 3");
    }

    @Test
    @DisplayName("Remoção: não deve remover o mesmo funcionário duas vezes")
    public void removerFuncionarioDuasVezes(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfAlessio), "Deve ser possivel remover funcionario cadastrado");
        assertFalse(rh.remover(cpfAlessio), "Nao e possivel remover um usuario duas vezes");
        assertEquals(5, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 5");
    }

    @Test
    @DisplayName("Remoção: não deve remover CPF que não está cadastrado")
    public void removerFuncionarioInexistente(){
        inserirFuncionarios();

        assertFalse(rh.remover(cpfNulo), "Nao e possivel remover um usuario nao cadastrado");
        assertEquals(6, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 6");
    }

    @Test
    @DisplayName("Remoção: o CPF de um funcionário removido pode ser cadastrado novamente")
    public void recadastrarAposRemocao(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfCarla));
        assertTrue(rh.cadastrar(tercCarla), "Depois de removido, o funcionario pode ser cadastrado de novo");
        assertEquals(6, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 6");
    }

    // ----------------------------------------------------------------- Busca

    @Test
    @DisplayName("Busca: deve encontrar professor pelo CPF")
    public void buscarProfessor(){
        inserirFuncionarios();
        assertEquals(profJonas, rh.obterFuncionario(cpfJonas),"Deve ser possivel achar esse professor");
    }

    @Test
    @DisplayName("Busca: deve encontrar STA pelo CPF")
    public void buscarSTA(){
        inserirFuncionarios();
        assertEquals(staMiriam, rh.obterFuncionario(cpfMiriam),"Deve ser possivel achar esse STA");
    }

    @Test
    @DisplayName("Busca: deve encontrar terceirizado pelo CPF")
    public void buscarTerceirizado(){
        inserirFuncionarios();
        assertEquals(tercCarla, rh.obterFuncionario(cpfCarla), "Deve ser possivel achar esse terceirizado");
    }

    @Test
    @DisplayName("Busca: deve retornar null para CPF não cadastrado ou removido")
    public void buscarFuncionariosNaoExistente(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfLacerda));
        assertTrue(rh.remover(cpfAlessio));

        assertNull(rh.obterFuncionario(cpfAlessio), "Este funcionario foi removido antes");
        assertNull(rh.obterFuncionario(cpfLacerda), "Este funcionario foi removido antes");
        assertNull(rh.obterFuncionario(cpfNulo), "Este funcionario nunca foi cadastrado");
    }

    // ------------------------------------------------------------ Relatórios

    @Test
    @DisplayName("Relatórios: listagens de um RH vazio devem ser listas vazias (e não null)")
    public void listagensVazias(){
        assertEquals(List.of(), rh.getFuncionarios(), "Sem funcionarios, a lista deve ser vazia");
        assertEquals(List.of(), rh.getFuncionariosPorCategoria(IRHService.Tipo.PROF), "Sem professores, a lista deve ser vazia");

        rh.cadastrar(profJonas);
        assertEquals(List.of(), rh.getFuncionariosPorCategoria(IRHService.Tipo.TERC), "Sem terceirizados, a lista deve ser vazia");
    }

    @Test
    @DisplayName("Relatórios: deve listar os professores ordenados pelo nome")
    public void buscarTodosOsProfessores(){
        inserirFuncionarios();

        Professor profChico = new Professor("91", "Chico", 'E');
        Professor profX = new Professor("92", "Xarles", 'D');
        rh.cadastrar(profChico);
        rh.cadastrar(profX);

        assertEquals(Arrays.asList(profAlessio, profChico, profJonas, profX),
                rh.getFuncionariosPorCategoria(IRHService.Tipo.PROF),
                "A lista deve conter os mesmos funcionarios e deve estar ordenada pelo nome");
    }

    @Test
    @DisplayName("Relatórios: deve listar os STAs ordenados pelo nome")
    public void buscarTodosOsSTAs(){
        inserirFuncionarios();

        Professor profChico = new Professor("91", "Chico", 'E');
        Professor profX = new Professor("92", "Xarles", 'D');
        rh.cadastrar(profChico);
        rh.cadastrar(profX);

        assertEquals(Arrays.asList(staLacerda, staMiriam),
                rh.getFuncionariosPorCategoria(IRHService.Tipo.STA),
                "A lista deve conter os mesmos funcionarios e deve estar ordenada pelo nome");
    }

    @Test
    @DisplayName("Relatórios: deve listar os terceirizados ordenados pelo nome")
    public void buscarTodosOsTerceirizados(){
        inserirFuncionarios();

        assertEquals(Arrays.asList(tercAdriana, tercCarla),
                rh.getFuncionariosPorCategoria(IRHService.Tipo.TERC),
                "A lista deve conter os mesmos funcionarios e deve estar ordenada pelo nome");
    }

    @Test
    @DisplayName("Relatórios: deve listar todos os funcionários ordenados pelo nome")
    public void buscarTodosOsFuncionarios(){
        inserirFuncionarios();

        assertEquals(Arrays.asList(tercAdriana, profAlessio, tercCarla, profJonas, staLacerda, staMiriam),
                rh.getFuncionarios(),
                "A lista deve conter os mesmos funcionarios e deve estar ordenada pelo nome");
    }

    // --------------------------------------------------------------- Salário

    @Test
    @DisplayName("Salário: professor recebe o salário base da sua classe")
    public void calcularSalarioProfessor(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(profAlessio));

        assertEquals(7000.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Calculo incorreto");
        assertEquals(5000.0, rh.calcularSalarioDoFuncionario(cpfAlessio), 0.01, "Calculo incorreto");
    }

    @Test
    @DisplayName("Salário: STA recebe o salário base do seu nível")
    public void calcularSalarioSTA(){
        assertTrue(rh.cadastrar(staMiriam));
        assertEquals(2000.0, rh.calcularSalarioDoFuncionario(cpfMiriam), 0.01, "Calculo incorreto");
    }

    @Test
    @DisplayName("Salário: terceirizado recebe 1000, ou 1500 com insalubridade")
    public void calcularSalarioTerceirizados(){
        assertTrue(rh.cadastrar(tercCarla));
        assertTrue(rh.cadastrar(tercAdriana));
        assertEquals(1000.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01, "Calculo incorreto sem insalubridade");
        assertEquals(1500.0, rh.calcularSalarioDoFuncionario(cpfAdriana), 0.01, "Calculo incorreto com insalubridade");
    }

    @Test
    @DisplayName("Salário: deve retornar null para CPF não cadastrado ou removido")
    public void calcularSalarioFuncionarioInexistente(){
        assertNull(rh.calcularSalarioDoFuncionario(cpfNulo), "Funcionario nao cadastrado nao tem salario");

        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.remover(cpfJonas));
        assertNull(rh.calcularSalarioDoFuncionario(cpfJonas), "Funcionario removido nao tem salario");
    }

    // ---------------------------------------------------------------- Diárias

    @Test
    @DisplayName("Diárias: cada diária acrescenta R$ 100 ao salário do professor")
    public void salarioProfessorComDiaria(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas), "Um professor tem direito a tres diarias");
        assertEquals(7100.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Calculo de diaria incorreto");
    }

    @Test
    @DisplayName("Diárias: cada diária acrescenta R$ 100 ao salário do STA")
    public void salarioSTAComDiaria(){
        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.solicitarDiaria(cpfLacerda), "Um sta tem direito a uma diaria");
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "Calculo de diaria incorreto");
    }

    @Test
    @DisplayName("Diárias: terceirizado não tem direito a diárias")
    public void salarioTerceirizadoComDiaria(){
        assertTrue(rh.cadastrar(tercCarla));
        assertFalse(rh.solicitarDiaria(cpfCarla), "Um terceirizado nao tem direito a diaria");
        assertEquals(1000.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01, "Calculo de diarias incorreto");
    }

    @Test
    @DisplayName("Diárias: professor tem direito a no máximo 3 diárias por mês")
    public void diariaAlemDoLimiteProfessor(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas), "Um professor tem direito a tres diarias");
        assertEquals(7100.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "O calculo do salario esta incorreto");
        assertTrue(rh.solicitarDiaria(cpfJonas), "Um professor tem direito a tres diarias");
        assertTrue(rh.solicitarDiaria(cpfJonas), "Um professor tem direito a tres diarias");
        assertFalse(rh.solicitarDiaria(cpfJonas), "Diarias alem do limite foram concedidas");
        assertEquals(7300.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "O calculo do salario esta incorreto");
    }

    @Test
    @DisplayName("Diárias: STA tem direito a no máximo 1 diária por mês")
    public void diariaAlemDoLimiteSTA(){
        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.solicitarDiaria(cpfLacerda));
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "O calculo do salario esta incorreto");
        assertFalse(rh.solicitarDiaria(cpfLacerda), "O funcionario nao tem mais direito a diarias");
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "O calculo do salario esta incorreto");
    }

    @Test
    @DisplayName("Diárias: não deve conceder diária para CPF não cadastrado")
    public void diariaFuncionarioInexistente(){
        assertFalse(rh.solicitarDiaria(cpfNulo), "Nao e possivel conceder diaria a um funcionario nao cadastrado");
    }

    @Test
    @DisplayName("Diárias: as diárias de um funcionário não afetam o limite dos outros")
    public void diariasIndependentesEntreFuncionarios(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(profAlessio));

        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas));

        assertTrue(rh.solicitarDiaria(cpfAlessio), "O limite de diarias e individual de cada funcionario");
        assertEquals(5100.0, rh.calcularSalarioDoFuncionario(cpfAlessio), 0.01, "O calculo do salario esta incorreto");
    }

    // ------------------------------------------------- Participação nos lucros

    @Test
    @DisplayName("Lucros: o lucro é dividido igualmente entre os funcionários")
    public void participacaoNosLucros(){
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01);

        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));
        assertTrue(rh.cadastrar(tercCarla));

        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01);

        rh.partilharLucros(6.00);

        assertEquals(7002.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01,
                "Salario com participacao nos lucros incorreto");
        assertEquals(2002.0, rh.calcularSalarioDoFuncionario(cpfMiriam), 0.01,
                "Salario com participacao nos lucros incorreto");
        assertEquals(1002.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01,
                "Salario com participacao nos lucros incorreto");
    }

    @Test
    @DisplayName("Lucros: partilhas feitas no mesmo mês se acumulam")
    public void partilhasAcumulam(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));

        rh.partilharLucros(200);
        rh.partilharLucros(100);

        assertEquals(7150.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01,
                "As participacoes recebidas no mesmo mes devem ser somadas");
        assertEquals(2150.0, rh.calcularSalarioDoFuncionario(cpfMiriam), 0.01,
                "As participacoes recebidas no mesmo mes devem ser somadas");
    }

    @Test
    @DisplayName("Lucros: quem é cadastrado depois da partilha não recebe parte dela")
    public void cadastradoAposPartilhaNaoRecebe(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));

        rh.partilharLucros(200);
        assertTrue(rh.cadastrar(tercCarla));

        assertEquals(7100.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01,
                "O lucro deve ser dividido apenas entre os funcionarios cadastrados no momento da partilha");
        assertEquals(1000.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01,
                "Funcionario cadastrado depois da partilha nao deve receber parte dela");
    }

    @Test
    @DisplayName("Lucros: partilhar sem funcionários cadastrados não deve causar erro")
    public void partilharSemFuncionarios(){
        assertDoesNotThrow(() -> rh.partilharLucros(1000), "Partilhar lucros sem funcionarios nao deve lancar excecao");
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01, "Folha de pagamento esta vazia");

        assertTrue(rh.cadastrar(profJonas));
        assertEquals(7000.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01,
                "Funcionario cadastrado depois da partilha nao deve receber parte dela");
    }

    // ------------------------------------------------------ Folha de pagamento

    @Test
    @DisplayName("Folha: sem funcionários, a folha é zero")
    public void calcularFolhaVazia(){
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01, "Folha de pagamento esta vazia");
    }

    @Test
    @DisplayName("Folha: é a soma dos salários de todos os funcionários")
    public void calcularFolha(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));
        assertTrue(rh.cadastrar(tercCarla));

        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01, "Soma de salarios incorreta");

        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.cadastrar(profAlessio));
        assertTrue(rh.cadastrar(tercAdriana));

        assertEquals(18000.0, rh.calcularFolhaDePagamento(), 0.01, "Soma de salarios incorreta");
    }

    @Test
    @DisplayName("Folha: funcionários removidos não entram na folha")
    public void calcularFolhaAposRemocao(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfJonas));
        assertEquals(11000.0, rh.calcularFolhaDePagamento(), 0.01, "O salario de um funcionario removido nao deve entrar na folha");
    }

    @Test
    @DisplayName("Folha: deve incluir as diárias")
    public void calcularFolhaComDiarias(){
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01);

        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));
        assertTrue(rh.cadastrar(tercCarla));

        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01, "Soma de salarios incorreta");

        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.cadastrar(profAlessio));
        assertTrue(rh.cadastrar(tercAdriana));

        assertEquals(18000.0, rh.calcularFolhaDePagamento(), 0.01, "Soma de salarios incorreta");

        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertEquals(7100.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Soma de salarios com diaria incorreta");
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertFalse(rh.solicitarDiaria(cpfJonas));

        assertEquals(7300.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Soma de salarios com diaria incorreta");

        assertFalse(rh.solicitarDiaria(cpfCarla));

        assertTrue(rh.solicitarDiaria(cpfLacerda));
        assertFalse(rh.solicitarDiaria(cpfLacerda));

        assertEquals(18400.0, rh.calcularFolhaDePagamento(), 0.01, "Soma de salarios com diaria incorreta");
    }

    @Test
    @DisplayName("Folha: deve incluir a participação nos lucros")
    public void calcularFolhaComPL(){
        participacaoNosLucros();
        assertEquals(10006.0, rh.calcularFolhaDePagamento(), 0.01,
                "Soma de salarios com participacao nos lucros incorreta");
    }

    // --------------------------------------------------------------- Novo mês

    @Test
    @DisplayName("Novo mês: zera as diárias e a participação nos lucros")
    public void iniciandoNovoMes(){
        calcularFolhaComPL();
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfMiriam));

        rh.iniciarMes();

        assertEquals(7000.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01,
                "Diarias e participacao nos lucros devem ser zeradas no inicio do mes");
        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01,
                "Diarias e participacao nos lucros devem ser zeradas no inicio do mes");
    }

    @Test
    @DisplayName("Novo mês: o limite de diárias volta a valer do zero")
    public void novoMesReiniciaLimiteDeDiarias(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staLacerda));

        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas));
        assertTrue(rh.solicitarDiaria(cpfLacerda));

        rh.iniciarMes();

        assertTrue(rh.solicitarDiaria(cpfJonas), "No novo mes o professor volta a ter direito a diarias");
        assertTrue(rh.solicitarDiaria(cpfJonas), "No novo mes o professor volta a ter direito a diarias");
        assertTrue(rh.solicitarDiaria(cpfJonas), "No novo mes o professor volta a ter direito a diarias");
        assertFalse(rh.solicitarDiaria(cpfJonas), "O limite de 3 diarias continua valendo no novo mes");
        assertTrue(rh.solicitarDiaria(cpfLacerda), "No novo mes o STA volta a ter direito a uma diaria");

        assertEquals(7300.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "O calculo do salario esta incorreto");
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "O calculo do salario esta incorreto");
    }
}
