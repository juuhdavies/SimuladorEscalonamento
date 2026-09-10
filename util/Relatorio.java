package util;

import modelo.Processo;
import java.util.*;

public class Relatorio {
    

    public static void gerarRelatorio(List<Processo> finalizados, String nomeEscalonador, List<PosEscalonamento> historicoGantt, List<String> ordemExecucao, int trocasContexto){
        System.out.println("\n\nRelatório de Execução - Escalonador: " + nomeEscalonador);
        System.out.println("--------------------------------------------------");

        //ordem de execução
        System.out.println("\nOrdem de execução dos processos:");
        for(String pid : ordemExecucao){
            System.out.print(pid + " -> ");
        }

        System.out.println("--------------------------------------------------");

        //gantt
        System.out.println("\n\nGráfico de Gantt:");
        for(PosEscalonamento pos : historicoGantt){
            System.out.println("[" + pos.getTempo() + "] " + pos.getNomeProcesso() + " (PID: " + pos.getPid() + ") | ");
        }

        System.out.println("--------------------------------------------------");

        //metricas

        int somaRetorno = 0;
        int somaEspera = 0;
        int somaResposta = 0;

        System.out.println("\n\nMétricas de desempenho:");
        for(Processo p : finalizados){
            System.out.println("\nProcesso: " + p.getNomeProcesso() + " (PID: " + p.getPid() + ")");
            //tempo de inicio
            System.out.println("Tempo de início do processo " + p.getTempoInicio());
            //conclusão
            System.out.println("Tempo de conclusão do processo " + + p.getTempoConclusao());
            //retorno
            System.out.println("Tempo de retorno do processo " +  p.getTempoRetorno());
            somaRetorno += p.getTempoRetorno();
            //espera
            System.out.println("Tempo de espera do processo " + p.getTempoEspera());
            somaEspera += p.getTempoEspera();
            //resposta
            System.out.println("Tempo de resposta do processo "  + p.getTempoResposta());
            somaResposta += p.getTempoResposta();

            System.out.println("--------------------------------------------------");
        }

        System.out.println("--------------------------------------------------");

        int total = finalizados.size();
        int mediaEspera = somaEspera / total;
        int mediaRetorno = somaRetorno / total;
        int mediaResposta = somaResposta / total;

        //trocas de contexto
        System.out.println("\n\nNúmero de trocas de contexto: " + trocasContexto);

        //medias

        System.out.println("\n\nMédias das métricas de desempenho:");
        //espera
        System.out.println("Média do tempo de espera: " + mediaEspera);
        //retorno
        System.out.println("Média do tempo de retorno: " + mediaRetorno);
        //resposta
        System.out.println("Média do tempo de resposta: " + mediaResposta);
    }
}
