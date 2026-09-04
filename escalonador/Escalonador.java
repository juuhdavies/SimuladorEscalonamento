package escalonador;

import modelo.Processo;


//padronizar métodos para entrada, seleção e verificação de processos pendentes
public interface Escalonador {
    void adicionarProcesso(Processo p); //adiciona processo à fila de prontos
    Processo proximoProcesso(int tempoAtual); //seleciona o próximo processo a ser executado, atualiza o estado do processo e retorna o processo selecionado
    boolean temProcessoPendente(); //boolean que indica se há processos pendentes na fila de prontos
}
