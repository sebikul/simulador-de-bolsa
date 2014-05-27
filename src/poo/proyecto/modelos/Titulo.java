package poo.proyecto.modelos;

import poo.proyecto.Algoritmos.Algorithm;

import java.io.Serializable;

/**
 * Representa un titulo sobre el cual se puede operar.
 */
public abstract class Titulo implements Serializable, HistoricStore {

    /**
     * Constante que se usa para minimizar la variacion de los precios.
     */
    static public final double PRICE_NORMALIZER = 70;

    /**
     * Almacena el simbolo del titulo.
     */
    private final String simbolo;

    /**
     * Almacena el valor inicial del titulo.
     */
    private final double valorInicial;

    /**
     * Almacena una instancia de consulta de los
     * valores historicos del titulo.
     */
    private final HistoricData historico;

    /**
     * Almacena el volumen maximo disponible en la simulacion..
     */
    private final int volumen;

    /**
     * Almacena el valor actual del titulo.
     */
    private double valor;

    /**
     * Almacena el volumen en circulacion del titulo.
     */
    private int volumenEnCirculacion = 0;

    /**
     * Almacena la cantidad de acciones compradas en un ciclo.
     */
    private int compras;

    /**
     * Almacena la cantidad de acciones vendidas en un ciclo.
     */
    private int ventas;

    /**
     * Almacena la instancia del algoritmo utilizado para
     * calcular la variacion de precios.
     */
    private Algorithm algoritmo;

    /**
     * Construye un nuevo titulo.
     *
     * @param simbolo      Simbolo del titulo.
     * @param valorInicial Valor inicial del titulo.
     * @param volumen      Volumen en circulacion
     */
    public Titulo(String simbolo, double valorInicial, int volumen) {
        this.simbolo = simbolo;
        this.valor = this.valorInicial = valorInicial;
        this.volumen = volumen;

        historico = new HistoricData(valorInicial);
    }

    /**
     * Setea el algoritmo que se usara para calcular el precio luego de cada compra/venta.
     *
     * @param algoritmo Algoritmo a utilizar para recalcular el precio.
     */
    public void setAlgoritmo(Algorithm algoritmo) {
        this.algoritmo = algoritmo;
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
     * @return Instancia de HistoricData que almacena los valores historicos del
     * titulo.
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

    /**
     * Devuelve el simbolo del titulo.
     *
     * @return Simbolo del titulo.
     */
    public final String getSimbolo() {
        return simbolo;
    }


    /**
     * Retorna el volumen maximo disponible del titulo.
     *
     * @return Volumen maximo disponible del titulo.
     */
    public final int getVolumen() {
        return volumen;
    }

    /**
     * Retorna el volumen en circulacion del titulo.
     *
     * @return Volumen en circulacion del titulo.
     */
    public final int getVolumenEnCirculacion() {
        return volumenEnCirculacion;
    }

    /**
     * Retorna el volumen disponible para la compra del titulo.
     *
     * @return Volumen disponible para la compra del titulo.
     */
    public final int getVolumenDisponible() {
        return volumen - volumenEnCirculacion;
    }

    /**
     * Notifica al titulo que se ejecuto una venta.
     *
     * @param cantidad Cantidad operada en la venta.
     */
    public final void notificarVenta(int cantidad) {

        valor = algoritmo.getNuevoPrecioVenta(cantidad);

        volumenEnCirculacion -= cantidad;

        ventas += cantidad;

    }

    /**
     * Notifica al titulo que se ejecuto una compra.
     *
     * @param cantidad Cantidad operada en la compra.
     */
    public final void notificarCompra(int cantidad) {

        valor = algoritmo.getNuevoPrecioCompra(cantidad);

        volumenEnCirculacion += cantidad;

        compras += cantidad;

    }

    /**
     * Notifica al titulo que se comenzo un ciclo nuevo.
     */
    public final void notificarComienzoCiclo() {

        compras = 0;
        ventas = 0;
        historico.agregar(valor);

        algoritmo.notificarComienzoCiclo();

    }

    /**
     * Notifica al titulo que finalizo el ciclo.
     */
    public final void notificarFinCiclo() {

        historico.actualizar(valor, compras, ventas);


    }

    @Override
    public String toString() {
        return this.simbolo + " (" + this.valor + ") | ( "
                + getVolumenEnCirculacion() + "/" + getVolumen() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Titulo))
            return false;

        Titulo titulo = (Titulo) o;

        if (Double.compare(titulo.valorInicial, valorInicial) != 0)
            return false;
        return simbolo.equals(titulo.simbolo);

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

}