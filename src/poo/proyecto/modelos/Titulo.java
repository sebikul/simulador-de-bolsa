package poo.proyecto.modelos;

import poo.proyecto.simulador.Simulador;

import java.util.Random;

public abstract class Titulo {

    static private final double PRICE_NORMALIZER = 100;

    private final String simbolo;
    private final double valorInicial;
    private double valor;
    private HistoricData historico;
    private Random rdm;

    public Titulo(String simbolo, double valorInicial, int histSize) {
        this.simbolo = simbolo;
        this.valor = this.valorInicial = valorInicial;

        historico = new HistoricData();
    }
    public Titulo(String simbolo, double valorInicial) {
        this(simbolo, valorInicial, Simulador.DEFAULT_SIM_CYCLES);
    }

    public final double getValorInicial() {
        return valorInicial;
    }

    public final HistoricData getHistorico() {
        return historico;
    }

    public final double getValor() {
        return valor;
    }

    public final void setValor(double valor) {
        this.valor = valor;
    }

    public final String getSimbolo() {
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

    public final void notificarCompra() {

        Double diff = valor * rdm.nextGaussian() / Titulo.PRICE_NORMALIZER;

        System.out.println("Valor de " + simbolo + ": " + valor + " --> "
                + (valor + diff));

        valor = valor + diff;
    }

    public final void notificarVenta() {


        Double diff = valor * rdm.nextGaussian() /Titulo.PRICE_NORMALIZER;

        System.out.println("Valor de " + simbolo + ": " + valor + " --> "
                + (valor - diff));

        valor = valor - diff;
    }

    public final void notificarComienzoCiclo() {

        rdm = new Random();

        historico.notificarComienzoCiclo();

    }

    public final void notificarFinCiclo() {

        historico.notificarFinCiclo(valor);

        rdm = null;

    }
}
