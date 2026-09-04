package util;

import modelo.EstadoProcesso;

public class PosEscalonamento {
    private int tempo;
    private String pid;
    private String nomeProcesso;
    private EstadoProcesso estado;
    private int quantumRestante;

    //contrutor
    public PosEscalonamento(int tempo, String pid, String nomeProcesso,
        EstadoProcesso estado, int quantumRestante) {
        
        this.tempo = tempo;
        this.pid = pid;
        this.nomeProcesso = nomeProcesso;
        this.estado = estado;
        this.quantumRestante = quantumRestante;
    }

    //construtor p/ Gantt
    public PosEscalonamento(int tempo, String pid, String nomeProcesso, EstadoProcesso estado){
        this.tempo = tempo;
        this.pid = pid;
        this.nomeProcesso = nomeProcesso;
        this.estado = estado;
        this.quantumRestante = 0;
    }

    //getters
    public int getTempo() { return tempo; }
    public String getPid() { return pid; }
    public String getNomeProcesso() { return nomeProcesso; }
    public EstadoProcesso getEstadoProcesso() { return estado; }
    public int getQuantumRestante() { return quantumRestante; }

    //setters
    public void setTempo (int tempo) {
        this.tempo = tempo;
    }
    public void setPid (String pid) {
        this.pid = pid;
    }
    public void setNomeProcesso (String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }
    public void setEstadoProcesso (EstadoProcesso estado) {
        this.estado = estado;
    }
    public void setQuantumRestante (int quantum) {
        this.quantumRestante = quantum;
    }
}