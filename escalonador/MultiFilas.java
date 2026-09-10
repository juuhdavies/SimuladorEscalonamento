package escalonador;

import modelo.Processo;
import modelo.EstadoProcesso;
import java.util.*;


public class MultiFilas implements Escalonador {
    //todas são round robin internamente, sem preempção (Só libera da fila ao terminar ou bloquear)
    private Queue<Processo> fila1 = new LinkedList<>(); //Quantum = 2, tempo_real e interativo, maior prioridade
    private Queue<Processo> fila2 = new LinkedList<>(); //Quantum = 4, io_bound e misto
    private Queue<Processo> fila3 = new LinkedList<>(); //Quantum = 8, cpu_bound e batch, menor prioridade

    @Override
    public void adicionarProcesso(Processo p) {
        p.setEstado(EstadoProcesso.PRONTO);
        switch (p.getFilaSugerida()) {
            case 1:
                fila1.add(p);
                break;
            case 2:
                fila2.add(p);
                break;
            case 3:
                fila3.add(p);
                break;
            default:
                throw new IllegalArgumentException("Fila sugerida inválida: " + p.getFilaSugerida());
        }
    }

    @Override
    public boolean temProcessoPendente() {
        return !fila1.isEmpty() || !fila2.isEmpty() || !fila3.isEmpty();
    }

    //método para promover processos da fila3 para 2 e da 2 para 3 caso estejam esperando a muito tempo
    private void verificarInanição(int tempoAtual) {
        Queue<Processo> novaFila2 = new LinkedList<>();
        Queue<Processo> novaFila3 = new LinkedList<>();

        while(!fila3.isEmpty()) {
            Processo p = fila3.poll();
            int tempoEspera = tempoAtual - p.getTempoChegadaFila();
            if (tempoEspera > 20) { // Se o processo está esperando há mais de 20 unidades de tempo
                p.setFilaSugerida(2); // Promove para a fila 2
                novaFila2.add(p);
                System.out.println("[Tempo " + tempoAtual + "] Processo " + p.getNomeProcesso() + " (PID: " + p.getPid() + ") promovido da fila 3 para a fila 2 devido à inanição.");
            } else {
                novaFila3.add(p);
            }
        }

        while(!fila2.isEmpty()) {
            Processo p = fila2.poll();
            int tempoEspera = tempoAtual - p.getTempoChegadaFila();
            if (tempoEspera > 40) { // Se o processo está esperando há mais de 10 unidades de tempo
                p.setFilaSugerida(1); // Promove para a fila 1
                fila1.add(p);
                System.out.println("[Tempo " + tempoAtual + "] Processo " + p.getNomeProcesso() + " (PID: " + p.getPid() + ") promovido da fila 2 para a fila 1 devido à inanição.");
            } else {
                novaFila2.add(p);
            }
            
        }

        fila3 = novaFila3;
        fila2 = novaFila2;
    }

    @Override
    public Processo proximoProcesso(int tempoAtual) {

        int tempoExecucao = 0; // Variável para armazenar o tempo de execução do processo atual
        int quantum = 0; // Variável para armazenar o quantum do processo atual
        int origemFila = 0; // Variável para armazenar a origem da fila do processo atual

        if (!temProcessoPendente()) {
            return null;
        }

        verificarInanição(tempoAtual); // Verifica se há processos que precisam ser promovidos

        Processo processoAtual = null;

        if (!fila1.isEmpty()) {
            processoAtual = fila1.poll();
            quantum = 2;
            origemFila = 1;
        } else if (!fila2.isEmpty()) {
            processoAtual = fila2.poll();
            quantum = 4;
            origemFila = 2;
        } else if (!fila3.isEmpty()) {
            processoAtual = fila3.poll();
            quantum = 8;
            origemFila = 3;
        }

        //se o processo atual for nulo, retorna null
        if (processoAtual == null) {
            return null;
        }

        //se é a primeira vez que o processo está sendo executado, define o primeiro tempo de CPU
        if (processoAtual.getPrimeiroTempoCPU() == -1) {
            processoAtual.setPrimeiroTempoCPU(tempoAtual);
        }
        if(processoAtual.getTempoInicio() == -1) {
            processoAtual.setTempoInicio(tempoAtual);
        }

        processoAtual.setEstado(EstadoProcesso.EXECUTANDO);

        tempoExecucao = Math.min(processoAtual.getTempoRestante(), quantum);
        processoAtual.setTempoRestante(processoAtual.getTempoRestante() - tempoExecucao);

        if(processoAtual.isOperacaoES() && Math.random() < processoAtual.getProbES()) {
            //se o processo realiza operação de E/S e a probabilidade for atendida, bloqueia o processo
            processoAtual.setEstado(EstadoProcesso.BLOQUEADO);
            processoAtual.setTempoBloqRestante(processoAtual.getDuracaoES());
        }


        else if(processoAtual.getTempoRestante() > 0) {
            //se ainda tem tempo restante, adiciona de volta na fila correspondente
            switch (origemFila) {
                case 1:
                    fila1.add(processoAtual);
                    break;
                case 2:
                    fila2.add(processoAtual);
                    break;
                case 3:
                    fila3.add(processoAtual);
                    break;
            }
            processoAtual.setEstado(EstadoProcesso.PRONTO);
            processoAtual.setTempoChegadaFila(tempoAtual + tempoExecucao); // Atualiza o tempo de chegada na fila do processo
        } else {
            processoAtual.setEstado(EstadoProcesso.FINALIZADO);
            processoAtual.setTempoConclusao(tempoAtual + tempoExecucao);
        }

        return processoAtual;

    }


}
