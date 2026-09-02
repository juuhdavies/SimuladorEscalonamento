package modelo;

public class Processo {
    private String pid; //identificador processo
    private String nomeProcesso; //nome do processo
    private int tempoChegada; //tempo de chegada do processo
    private int tempoExecucao; //tempo de CPU total para execução
    private int prioridade; //prioridade do processo
    private String tipoProcesso; //tipo do processo
    private boolean operacaoES; //indica se o processo realiza operação de E/S
    private double probES; //probabilidade de realizar operaçao E/S
    private double mediaES; //num médio de solicitações E/S
    private int duracaoES; //tempo que o processo permanece bloqueado
    private int filaSugerida; //fila sugerida
    private int quantumSugerido; //quantum do processo


    //variaveis de controle dinamixas
    private int tempoRestante; //tempo de cpu restante
    private int tempoBloqRestante; //tempo bloqueado restante
    private int EstadoProcesso estado; //estado atual do processo
    
    //variaveis controle métricas
    private int tempoInicio = -1;
    private int tempoConclusao = 0;
    private int primeiroTempoCPU = -1;
    private int tempoTotalES = 0;


    //construtor
    public Processo(String pid, String nomeProcesso, int tempoChegada,
                    int tempoExecucao, int prioridade, String tipoProcesso, 
                    int operacaoES, double probES, double mediaES, int duracaoES,
                    int filaSugerida, int quantumSugerido){
         
      this.pid =  pid;
      this.nomeProcesso = nomeProcesso;
      this.tempoChegada = tempoChegada;
      this.tempoExecucao = tempoExecucao;
      this.tempoRestante = tempoExecucao;
      this.prioridade = prioridade;
      this.tipoProcesso = tipoProcesso;
      this.operacaoES = (operacaoES == 1);
      this.probES = probES;
      this.mediaES = mediaES;
      this.duracaoES = duracaoES;
      this.filaSugerida = filaSugerida;
      this.quantumSugerido = quantumSugerido;                 
    }

    //getters metricas
    public int getTempoRetorno(){
        return this.tempoConclusao - this.tempoChegada;
    }

    public int getTempoEspera(){
        return this.getTempoRetorno() - this.tempoExecucao - this.tempoTotalES;
    }

    public int getTempoResposta(){
        if(this.primeiroTempoCPU == -1) return 0;
        return this.primeiroTempoCPU - this.tempoChegada;
    }


    //getters
    public String getPid() { return pid; }
    public String getNomeProcesso() { return nomeProcesso; }
    public int getTempoChegada() { return tempoChegada; }
    public int getTempoExecucao() {return tempoExecucao; }
    public int getPrioridade() { return prioridade; }
    public String getTipoProcesso() { return tipoProcesso; }
    public boolean isOperacaoES() { return operacaoES; }
    public double getProbES() { return probES; }
    public double getMediaES() { return mediaES; }
    public int getDuracaoES() { return duracaoES; }
    public int getFilaSugerida() { return filaSugerida; }
    public int getQuantumSugerido() { return quantumSugerido; }

    public int getTempoRestante() { return tempoRestante; }
    public EstadoProcesso getEstado() { return estado; }
    public int getTempoBloqRestante() { return tempoBloqRestante; }
    public int getTempoInicio() { return tempoInicio; }
    public int getTempoConclusao() { return tempoConclusao; }
    public int getPrimeiroTempoCPU() { return primeiroTempoCPU; }
    public int getTempoTotalES() { return tempoTotalES; }

    //setters

    public void setTempoRestante(int tempoRestante){
        this.tempoRestante = tempoRestante;
    }

    public void setEstado (EstadoProcesso estado){
        this.estado = estado;
    }

    public void setTempoBloqRestante(int tempoBloqRestante) {
        this.tempoBloqRestante = tempoBloqRestante;
    }

    public void setTempoInicio(int tempoInicio) {
        if(this.tempoInicio == -1){
        this.tempoInicio = tempoInicio;
        }
    }
    
    public void setTempoConclusao(int tempoConclusao) {
        this.tempoConclusao = tempoConclusao;
    }

    public void setPrimeiroTempoCPU(int primeiroTempoCPU) {
        if(this.primeiroTempoCPU == -1){
            this.primeiroTempoCPU = primeiroTempoCPU;
        }
    }

    public void somarTempoES(int tempo) {
        this.tempoTotalES += tempo;
    }
}
