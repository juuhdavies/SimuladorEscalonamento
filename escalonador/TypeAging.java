package escalonador;

import modelo.Processo;
import modelo.EstadoProcesso;
import java.util.*;


//objetivo do método de escalonamento: utilizar o tipo de processo para determinar prioridade e  após isso definir qual deve ser executado com uma média do tempo de espera e quantum sugerido
public class TypeAging implements Escalonador {
    
    private List<Processo> filaProntos = new ArrayList<>();


    @Override 
    public void adicionarProcesso(Processo p) {
        p.setEstado(EstadoProcesso.PRONTO);
        filaProntos.add(p);
    }

    @Override 
    public boolean temProcessoPendente() {
        return !filaProntos.isEmpty();
    }

    private int calcularPrioridade(Processo p) {
        // Define a prioridade com base no tipo de processo

        int peso; 
        switch (p.getTipoProcesso()) {
            case "tempo_real":
                peso = 1; // Maior prioridade
                break;
            case "interativo":
                peso = 2;
                break;
            case "io_bound":
                peso = 3;
                break;
            case "misto":
                peso = 4;
                break;
            case "batch":
                peso = 5;
                break;
            case "cpu_bound":
                peso = 6; // Menor prioridade
                break;
            default:
                peso = 6; // Caso não seja reconhecido, atribui a menor prioridade possível
                break;
        }

        int quantum = p.getQuantumSugerido(); // Obtém o quantum sugerido pelo processo
        int tempoEspera = p.getTempoChegadaFila() - p.getTempoChegada(); // Calcula o tempo de espera do processo

        return (peso * quantum) - tempoEspera; // Combina os fatores para calcular a prioridade final, quanto mais tempo esperado, maior a prioridade
    }

    @Override
    public Processo proximoProcesso(int tempoAtual) {
        if(!temProcessoPendente()){
            return null;
        } // Verifica se há processos pendentes

        Processo processoAtual = null;
        int melhorPrioridade = Integer.MAX_VALUE; 

        for (Processo p : filaProntos) {
            p.setTempoChegadaFila(tempoAtual);

            int prioridadeAtual = calcularPrioridade(p);
            if (prioridadeAtual < melhorPrioridade) {
                melhorPrioridade = prioridadeAtual;
                processoAtual = p;
            }
        }

        filaProntos.remove(processoAtual); // Remove o processo selecionado da fila de prontos
        processoAtual.setEstado(EstadoProcesso.EXECUTANDO); 

        if(processoAtual.getPrimeiroTempoCPU() == -1) {
            processoAtual.setPrimeiroTempoCPU(processoAtual.getTempoChegadaFila()); 
        }
        if(processoAtual.getTempoInicio() ==-1){
            processoAtual.setTempoInicio(tempoAtual);
        }
        
        int quantum = processoAtual.getQuantumSugerido(); // Obtém o quantum sugerido pelo processo
        int tempoExecucao = Math.min(processoAtual.getTempoRestante(), quantum); 
        processoAtual.setTempoRestante(processoAtual.getTempoRestante() - tempoExecucao);

        if(processoAtual.isOperacaoES() && Math.random() < processoAtual.getProbES()) {
            //se o processo realiza operação de E/S e a probabilidade for atendida, bloqueia o processo
            processoAtual.setEstado(EstadoProcesso.BLOQUEADO);
            processoAtual.setTempoBloqRestante(processoAtual.getDuracaoES());
        } 
        
        else if (processoAtual.getTempoRestante() > 0) {
            filaProntos.add(processoAtual); // se ainda tem quantum restante, adiciona o processo de volta à fila de prontos
            processoAtual.setEstado(EstadoProcesso.PRONTO); // Atualiza o estado do processo para PRONTO
            processoAtual.setTempoChegadaFila(tempoAtual + tempoExecucao); // Atualiza o tempo de chegada na fila do processo
        } else {
            processoAtual.setEstado(EstadoProcesso.FINALIZADO);
            processoAtual.setTempoConclusao(tempoAtual + tempoExecucao); // Define o tempo de conclusão do processo
        }

        return processoAtual;
    }

}
