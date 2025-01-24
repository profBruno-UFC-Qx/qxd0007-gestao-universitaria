import br.ufc.quixada.gestao.RHService;
import br.ufc.quixada.gestao.contrato.IRHService;
import br.ufc.quixada.gestao.model.Funcionario;
import br.ufc.quixada.gestao.model.Professor;
import br.ufc.quixada.gestao.model.STA;
import br.ufc.quixada.gestao.model.Terceirizado;

public class Runner {

    public static void main(final String[] args) {


        RHService rh = new RHService();
        rh.cadastrar(new Professor("16",  "Jonas", 'C'));
        rh.cadastrar(new Professor("15", "Alessio", 'B'));
        System.out.println("Total de funcionarios = " + rh.getTotalFuncionarios()); //Total de funcionarios = 2

        rh.cadastrar(new STA("43", "Miriam", 10));
        rh.cadastrar(new STA("23", "Lacerda", 5));
        System.out.println("Total de funcionarios = " + rh.getTotalFuncionarios()); //Total de funcionarios = 4

        rh.cadastrar(new Terceirizado("12", "Carla", false));
        rh.cadastrar(new Terceirizado("78", "Adriana", true));
        System.out.println("Total de funcionarios = " + rh.getTotalFuncionarios()); //Total de funcionarios = 6

        rh.remover("12");
        System.out.println("Total de funcionarios = " + rh.getTotalFuncionarios()); //Total de funcionarios = 5
        System.out.println("Total de funcionarios = " + rh.getFuncionariosPorCategoria(IRHService.Tipo.TERC).size());

        rh.solicitarDiaria("16");
        rh.solicitarDiaria("16");
        rh.solicitarDiaria("16");
        rh.solicitarDiaria("23");
        rh.solicitarDiaria("23");

        System.out.println(rh.calcularSalarioDoFuncionario("16")); //7300.0
        System.out.println(rh.calcularSalarioDoFuncionario("23")); //1600.0
        System.out.println(rh.calcularSalarioDoFuncionario("12")); //null
        System.out.println(rh.calcularSalarioDoFuncionario("78")); //1500.0

        rh.iniciarMes();
        rh.partilharLucros(20000);

        for (Funcionario f: rh.getFuncionarios()) {
            System.out.println(f.getNome() + "(cpf=" + f.getCpf() + ") -> salario=" + f.getSalarioBase());
        }
        //Adriana(cpf=78) -> salario=5500.0
        //Alessio(cpf=15) -> salario=9000.0
        //Jonas(cpf=16) -> salario=11000.0
        //Lacerda(cpf=23) -> salario=5500.0
        //Miriam(cpf=43) -> salario=6000.0

        System.out.println("Folha do mes = " + rh.calcularFolhaDePagamento()); //Folha do mes = 37000.0
    }
}
