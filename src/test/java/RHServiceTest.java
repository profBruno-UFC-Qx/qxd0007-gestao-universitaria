import br.ufc.quixada.gestao.model.Professor;
import br.ufc.quixada.gestao.model.STA;
import br.ufc.quixada.gestao.model.Terceirizado;
import br.ufc.quixada.gestao.RHService;
import br.ufc.quixada.gestao.contrato.IRHService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static br.ufc.quixada.gestao.contrato.IRHService.Tipo.STA;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void cadastrarTerceirizado(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(tercAdriana), "O terceirizado deveria ter sido adicionado");
        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertEquals(2, rh.getTotalFuncionarios(), "O RH deveria ter dois funcionarios registrados");
    }

    @Test
    public void cadastrarSTA(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(staMiriam), "O STA deveria ter sido adicionado");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    public void cadastrarProfessor(){
        assertEquals(0, rh.getTotalFuncionarios(), "O RH deve iniciar vazio");
        assertTrue(rh.cadastrar(profJonas), "O Professor deveria ter sido adicionado");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    public void cadastrarFuncionarioDuplicado(){
        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertFalse(rh.cadastrar(new Professor(cpfCarla, "claudio", 'C')), "Nao deve ser possivel adiciona o mesmo funcionario(cpf) duas vezes");
        assertEquals(1, rh.getTotalFuncionarios(), "O RH deveria ter um funcionario registrado");
    }

    @Test
    public void cadastrarProfessorComClasseInvalida(){
        assertFalse(rh.cadastrar(new Professor(cpfNulo, "claudio", 'X')), "Nao podemos cadastrar professor com classe invalida");
        assertEquals(0, rh.getTotalFuncionarios(), "Funcionario cadastrado indevidamente");
    }

    @Test
    public void cadastrarSTAComNivelInvalido(){
        assertFalse(rh.cadastrar(new STA(cpfNulo, "claudio", 35)), "Nao podemos cadastrar sta com nivel invalido");
        assertEquals(0, rh.getTotalFuncionarios(), "Funcionario cadastrado indevidamente");
    }

    private void inserirFuncionarios() {
        assertTrue(rh.cadastrar(profAlessio), "O terceirizado deveria ter sido adicionado");
        assertTrue(rh.cadastrar(profJonas), "O terceirizado deveria ter sido adicionado");

        assertTrue(rh.cadastrar(staMiriam), "O STA deveria ter sido adicionado");
        assertTrue(rh.cadastrar(staLacerda), "O STA deveria ter sido adicionado");

        assertTrue(rh.cadastrar(tercCarla), "O terceirizado deveria ter sido adicionado");
        assertTrue(rh.cadastrar(tercAdriana), "O terceirizado deveria ter sido adicionado");
    }

    @Test
    public void removerFuncionario(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfLacerda), "Deve ser possivel remover funcionario cadastrado");
        assertTrue(rh.remover(cpfAdriana), "Deve ser possivel remover funcionario cadastrado");
        assertTrue(rh.remover(cpfAlessio), "Deve ser possivel remover funcionario cadastrado");
        assertFalse(rh.remover(cpfNulo), "Nao e possivel remover um usuario nao cadastrado");
        assertEquals(3, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 3");

    }

    @Test
    public void removerFuncionarioDuasVezes(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfAlessio), "Deve ser possivel remover funcionario cadastrado");
        assertFalse(rh.remover(cpfAlessio), "Nao e possivel remover um usuario duas vezes");
        assertEquals(5, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 5");
    }

    @Test
    public void removerFuncionarioInexistente(){
        inserirFuncionarios();

        assertFalse(rh.remover(cpfNulo), "Nao e possivel remover um usuario nao cadastrado");
        assertEquals(6, rh.getTotalFuncionarios(), "O total de funcionarios deve ser 6");
    }

    @Test
    public void buscarProfessor(){
        inserirFuncionarios();
        assertEquals(profJonas, rh.obterFuncionario(cpfJonas),"Deve ser possivel achar esse professor");

    }

    @Test
    public void buscarSTA(){
        inserirFuncionarios();
        assertEquals(staMiriam, rh.obterFuncionario(cpfMiriam),"Deve ser possivel achar esse STA");

    }

    @Test
    public void buscarTerceirizado(){
        inserirFuncionarios();
        assertEquals(tercCarla, rh.obterFuncionario(cpfCarla), "Deve ser possivel achar esse terceirizado");
    }

    @Test
    public void buscarFuncionariosNaoExistente(){
        inserirFuncionarios();

        assertTrue(rh.remover(cpfLacerda));
        assertTrue(rh.remover(cpfAlessio));

        assertEquals(null, rh.obterFuncionario(cpfAlessio), "Este funcionario foi removido antes");
        assertEquals(null, rh.obterFuncionario(cpfLacerda), "Este funcionario foi removido antes");
    }

    @Test
    public void buscarTodosOsProfessores(){
        inserirFuncionarios();

        Professor profChico = new Professor("91", "Chico", 'E');
        Professor profX = new Professor("92", "Xarles", 'D');
        rh.cadastrar(profChico);
        rh.cadastrar(profX);

        assertEquals(Arrays.asList(profAlessio, profChico, profJonas, profX),
                rh.getFuncionariosPorCategoria(IRHService.Tipo.PROF),
                "A lista deve conter os mesmo funcionario e deve estar ordenada pelo nome funcionario");
    }

    @Test
    public void buscarTodosOsSTAs(){
        inserirFuncionarios();

        Professor profChico = new Professor("91", "Chico", 'E');
        Professor profX = new Professor("92", "Xarles", 'D');
        rh.cadastrar(profChico);
        rh.cadastrar(profX);

        assertEquals(Arrays.asList(staLacerda, staMiriam),
                rh.getFuncionariosPorCategoria(STA),
                "A lista deve conter os mesmo funcionario e deve estar ordenada pelo nome funcionario");
    }

    @Test
    public void buscarTodosOsTerceirizados(){
        inserirFuncionarios();

        assertEquals(Arrays.asList(tercAdriana, tercCarla),
                rh.getFuncionariosPorCategoria(IRHService.Tipo.TERC),
                "A lista deve conter os mesmo funcionario e deve estar ordenada pelo nome funcionario");
    }

    @Test
    public void buscarTodosOsFuncionarios(){
        inserirFuncionarios();

        assertEquals(Arrays.asList(tercAdriana, profAlessio, tercCarla, profJonas, staLacerda, staMiriam),
                rh.getFuncionarios(),
                "A lista deve conter os mesmo funcionario e deve estar ordenada pelo nome funcionario");
    }

    @Test
    public void calcularSalarioProfessor(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(profAlessio));

        assertEquals(7000.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Calculo incorreto");
        assertEquals(5000.0, rh.calcularSalarioDoFuncionario(cpfAlessio), 0.01, "Calculo incorreto");
    }

    @Test
    public void calcularSalarioSTA(){
        assertTrue(rh.cadastrar(staMiriam));
        assertEquals(2000.0, rh.calcularSalarioDoFuncionario(cpfMiriam), 0.01, "Calculo incorreto");
    }

    @Test
    public void calcularSalarioTerceirizados(){
        assertTrue(rh.cadastrar(tercCarla));
        assertEquals(1000.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01, "Calculo incorreto");
    }

    @Test
    public void salarioProfessorComDiaria(){
        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.solicitarDiaria(cpfJonas), "Um professor tem direito a tres diarias");
        assertEquals(7100.0, rh.calcularSalarioDoFuncionario(cpfJonas), 0.01, "Calculo de diaria incorreto");
    }

    @Test
    public void salarioSTAComDiaria(){
        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.solicitarDiaria(cpfLacerda), "Um sta tem direito a uma diaria");
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "Calculo de diaria incorreto");
    }

    @Test
    public void salarioTerceirizadoComDiaria(){
        assertTrue(rh.cadastrar(tercCarla));
        assertFalse(rh.solicitarDiaria(cpfCarla), "Um terceirizado nao tem direito a diaria");
        assertEquals(1000.0, rh.calcularSalarioDoFuncionario(cpfCarla), 0.01, "Calculo de diarias incorreto");
    }

    @Test
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
    public void diariaAlemDoLimiteSTA(){
        assertTrue(rh.cadastrar(staLacerda));
        assertTrue(rh.solicitarDiaria(cpfLacerda));
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "O calculo do salario esta incorreto");
        assertFalse(rh.solicitarDiaria(cpfLacerda), "O funcionario nao tem mais direito a diarias");
        assertEquals(1600.0, rh.calcularSalarioDoFuncionario(cpfLacerda), 0.01, "O calculo do salario esta incorreto");
    }

    @Test
    public void calcularFolhaVazia(){
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01, "Folha de pagamento esta vazia");
    }

    @Test
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
    public void participacaoNosLucros(){
        assertEquals(0.0, rh.calcularFolhaDePagamento(), 0.01);

        assertTrue(rh.cadastrar(profJonas));
        assertTrue(rh.cadastrar(staMiriam));
        assertTrue(rh.cadastrar(tercCarla));

        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01);

        rh.partilharLucros(6.00);

        assertEquals(2002.0, rh.calcularSalarioDoFuncionario(cpfMiriam), 0.01,
                "Salarios com participacao nos lucros incorreto");
    }

    @Test
    public void calcularFolhaComPL(){
        participacaoNosLucros();
        assertEquals(10006.0, rh.calcularFolhaDePagamento(), 0.01,
                "Soma de salarios com participacao nos lucros incorreta");
    }

    @Test
    public void iniciandoNovoMes(){
        calcularFolhaComPL();
        rh.iniciarMes();
        assertEquals(10000.0, rh.calcularFolhaDePagamento(), 0.01);

    }
}