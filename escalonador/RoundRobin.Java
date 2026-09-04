package escalonador;

import modelo.Processo;
import modelo.EstadoProcesso;
import java.util.*;

public class RoundRobin implements Escalonador {
    private Queue<Processo> filaProntos = new LinkedList<>();
    private final int quantum = 4;

    @Override
    public void adicionarProcesso(Processo p) {
        p.setEstado(EstadoProcesso.PRONTO);
        filaProntos.add(p);
    }

    @Override
    public boolean temProcessoPendente() {
        return !filaProntos.isEmpty();
    }

    @Override
    public Processo proximoProcesso(int tempoAtual) {
        if(!temProcessoPendente()){
            return null;
        } // Verifica se há processos pendentes

        Processo processoAtual = filaProntos.poll(); // Remove o processo da fila de prontos

        //se é a primeira vez que o processo está sendo executado, define o primeiro tempo de CPU
        if(processoAtual.getPrimeiroTempoCPU() == -1) {
            processoAtual.setPrimeiroTempoCPU(tempoAtual); // Define o primeiro tempo de CPU se ainda não foi definido
        }

        processoAtual.setEstado(EstadoProcesso.EXECUTANDO); // Atualiza o estado do processo para EXECUTANDO

        int tempoExecucao = Math.min(processoAtual.getTempoRestante(), quantum); // Determina o tempo de execução com base no quantum e no tempo restante do processo
        processoAtual.setTempoRestante(processoAtual.getTempoRestante() - tempoExecucao); // Atualiza o tempo restante do processo

        // verifica se o processo ainda tem tempo restante para execução
        if (processoAtual.getTempoRestante() > 0) {
            filaProntos.add(processoAtual); // se ainda tem quantum restante, adiciona o processo de volta à fila de prontos
            processoAtual.setEstado(EstadoProcesso.PRONTO); // Atualiza o estado do processo para PRONTO
        } else {
            processoAtual.setEstado(EstadoProcesso.FINALIZADO);
            processoAtual.setTempoConclusao(tempoAtual + tempoExecucao); // Define o tempo de conclusão do processo
        }

        return processoAtual;
    }
}
