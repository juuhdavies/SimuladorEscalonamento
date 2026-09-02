package modelo;

public enum EstadoProcesso {
    PRONTO("Pronto"),
    EXECUTANDO("Executando"),
    BLOQUEADO("Bloqueado"),
    FINALIZADO("Finalizado");

    private final String descricao;

    EstadoProcesso(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
