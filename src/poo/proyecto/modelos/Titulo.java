package poo.proyecto.modelos;


public abstract class Titulo {

    private final String simbolo;
    private final double valorInicial;
    private double valor;

    public Titulo(String simbolo, double valorInicial) {
        this.simbolo = simbolo;
        this.valor = this.valorInicial = valorInicial;
    }

    public final double getValor() {
        return valor;
    }

    public final void setValor(double valor) {
        this.valor = valor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    @Override
    public String toString() {
        return this.simbolo + " (" + this.valor + ")";
    }

    public String printDebugInfo() {
        return "$" + valorInicial + " --> $" + valor;
    }

    public abstract String getName();
}
