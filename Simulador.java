import modelo.Processo;
import modelo.EstadoProcesso;
import escalonador.Escalonador;
import escalonador.RoundRobin;
import escalonador.MultiFilas;
import util.PosEscalonamento;
import util.ReadCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Simulador {

    public static void simular(String arquivoCSV, Escalonador escalonador, String nomeEscalonador) {
        System.out.println("Simulando com escalonador: " + nomeEscalonador);


        List<Processo> processos = ReadCSV.carregarProcessos(arquivoCSV);
        List<Processo> finalizados = new ArrayList<>();
        List<Processo> bloqueados = new ArrayList<>();
        List<PosEscalonamento> historicoGantt = new ArrayList<>();

        int tempoAtual = 0; //tempo atual da simulação
        int indexProcessosEntrada = 0; // índice para controlar a entrada de processos na simulação

        while(indexProcessosEntrada < processos.size() || escalonador.temProcessoPendente() || !bloqueados.isEmpty()) {
            //adiciona processos que chegaram no tempo atual
            while(indexProcessosEntrada < processos.size() && processos.get(indexProcessosEntrada).getTempoChegada() <= tempoAtual) {
                escalonador.adicionarProcesso(processos.get(indexProcessosEntrada));
                indexProcessosEntrada++;
            }

            //verifica se algum processo bloqueado terminou a operação ES
            List<Processo> desbloqueados = new ArrayList<>();
            for(Processo p : bloqueados) {
                p.setTempoBloqRestante(p.getTempoBloqRestante() - 1);
                p.somarTempoES(1);
                if(p.getTempoBloqRestante() <= 0) {
                    desbloqueados.add(p);
                }
            }
            for(Processo p : desbloqueados) {
                bloqueados.remove(p);
                escalonador.adicionarProcesso(p);
                p.setTempoChegadaFila(tempoAtual); // Atualiza o tempo de chegada na fila do processo
            }

            //obtem o próximo processo a ser executado
            Processo processoAtual = escalonador.proximoProcesso(tempoAtual);

            if(processoAtual != null) {
                historicoGantt.add(new PosEscalonamento(tempoAtual, processoAtual.getPid(), processoAtual.getNomeProcesso(), processoAtual.getEstado())); //adiciona ao histórico Gantt

                System.out.println("[Tempo " + tempoAtual + "] Executando processo: " + processoAtual.getNomeProcesso() 
                        + " (PID: " + processoAtual.getPid() + ") | Início da execução: " + tempoAtual);

                processoAtual.setTempoRestante(processoAtual.getTempoRestante() - 1);
                tempoAtual++;

                if(processoAtual.getTempoRestante() <= 0) {
                    processoAtual.setEstado(EstadoProcesso.FINALIZADO);
                    processoAtual.setTempoConclusao(tempoAtual);
                    finalizados.add(processoAtual);
                    System.out.println("[Tempo " + tempoAtual + "] Processo finalizado: " + processoAtual.getNomeProcesso() 
                            + " (PID: " + processoAtual.getPid() + ") | Tempo de conclusão: " + tempoAtual);
                } 
                else if(processoAtual.getEstado() == EstadoProcesso.BLOQUEADO) {
                    bloqueados.add(processoAtual);
                    System.out.println("[Tempo " + tempoAtual + "] Processo bloqueado para E/S: " + processoAtual.getNomeProcesso() 
                            + " (PID: " + processoAtual.getPid() + ") | Tempo de bloqueio: " + processoAtual.getDuracaoES());
                }
                else{
                    System.out.println("[Tempo " + tempoAtual + "] Processo retornou para a fila: " + processoAtual.getNomeProcesso() 
                            + " (PID: " + processoAtual.getPid() + ") | Tempo restante: " + processoAtual.getTempoRestante());
                }
            }
            else{
                tempoAtual++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String caminhoCSV; //caminho do arquivo CSV

        caminhoCSV =  scanner.nextLine();

        
        
        
        System.out.println("==================================================");
        System.out.println("       SIMULADOR DE ESCALONAMENTO DE CPU          ");
        System.out.println("==================================================");
        System.out.println("Escolha o algoritmo de simulação:");
        System.out.println("1 - Round Robin");
        System.out.println("2 - MultiFilas");
        System.out.println("3 - IMPLEMENTAR MÉTODO PROPRIO");
        System.out.print("Digite sua opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                simular(caminhoCSV, new RoundRobin(), "Round-Robin");
                break;
            case 2:
                simular(caminhoCSV, new MultiFilas(), "Múltiplas Filas");
                break;
            case 3:
                simular(caminhoCSV, new RoundRobin(), "Round-Robin");
                simular(caminhoCSV, new MultiFilas(), "Múltiplas Filas");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }

        scanner.close();
    }
}