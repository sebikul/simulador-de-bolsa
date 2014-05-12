package poo.proyecto.modelos;

import java.util.Random;

/**
 * Representa un titulo sobre el cual se puede operar.
 */
public abstract class Titulo {

    static private final double PRICE_NORMALIZER = 100;

    /**
     * Almacena el simbolo del titulo.
     */
    private final String simbolo;

    /**
     * Almacena el valor inicial del titulo.
     */
    private final double valorInicial;
    /**
     * Almacena una instancia de consulta de los valores historicos del titulo.
     */
    private final HistoricData historico;
    /**
     * Almacena el valor actual del titulo.
     */
    private double valor;
    /**
     * Instancia de Random utilizado para calcular la variacion del valor
     * en cada compra o venta del titulo.
     */
    private Random rdm = null;

    /**
     * Construye un nuevo titulo.
     *
     * @param simbolo      Simbolo del titulo.
     * @param valorInicial Valor inicial del titulo.
     */
    public Titulo(String simbolo, double valorInicial) {
        this.simbolo = simbolo;
        this.valor = this.valorInicial = valorInicial;

        historico = new HistoricData();
    }

    /**
     * Devuelve el valor inicial del titulo.
     *
     * @return Valor inicial del titulo.
     */
    public final double getValorInicial() {
        return valorInicial;
    }

    /**
     * Devuelve un objeto de consulta de valores historicos.
     *
     * @return Instancia de HistoricData que almacena los valores historicos del titulo.
     */
    public final HistoricData getHistorico() {
        return historico;
    }

    /**
     * Devuelve el valor del titulo.
     *
     * @return Valor del titulo.
     */
    public final double getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Titulo)) return false;

        Titulo titulo = (Titulo) o;

        if (Double.compare(titulo.valorInicial, valorInicial) != 0) return false;
        if (!simbolo.equals(titulo.simbolo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = simbolo.hashCode();
        temp = Double.doubleToLongBits(valorInicial);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Devuelve el simbolo del titulo.
     *
     * @return Simbolo del titulo.
     */
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

    /**
     * Notifica al titulo que se ejecuto una compra.
     * TODO cantidad
     *
     * @param cantidad Cantidad operada en la compra.
     */
    public final void notificarCompra(int cantidad) {

        Double diff = valor * rdm.nextGaussian() / Titulo.PRICE_NORMALIZER;

        // System.out.println("Valor de " + simbolo + ": " + valor + " --> "
        //         + (valor + diff));

        valor += diff;
    }

    /**
     * Notifica al titulo que se ejecuto una venta.
     * TODO cantidad
     *
     * @param cantidad Cantidad operada en la venta.
     */
    public final void notificarVenta(int cantidad) {

        Double diff = valor * rdm.nextGaussian() / Titulo.PRICE_NORMALIZER;

        // System.out.println("Valor de " + simbolo + ": " + valor + " --> "
        //        + (valor - diff));

        valor -= diff;
    }

    /**
     * Notifica al titulo que se comenzo un ciclo nuevo.
     */
    public final void notificarComienzoCiclo() {

        rdm = new Random();

        historico.notificarComienzoCiclo();

    }

    /**
     * Notifica al titulo que finalizo el ciclo.
     */
    public final void notificarFinCiclo() {

        //synchronized (historico) {
            historico.notificarFinCiclo(valor);
        //}


        rdm = null;

    }
}