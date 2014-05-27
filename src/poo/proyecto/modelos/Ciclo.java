package poo.proyecto.modelos;

import java.io.Serializable;

/**
 * Almacena los datos de un ciclo.
 */
public class Ciclo implements Serializable {

    private double valor;
    private int compras;
    private int ventas;

    /**
     * Construye una nueva instancia para almacenar los datos de un ciclo.
     *
     * @param valor Valor inicial
     */
    public Ciclo(double valor) {
        this.valor = valor;
        this.compras = 0;
        this.ventas = 0;
    }

    /**
     * Retorna el valor del elemento.
     *
     * @return Valor del elemento
     */
    public double getValor() {
        return valor;
    }

    /**
     * Setea el valor del elemento.
     *
     * @param valor Nuevo valor del elemento
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * Retorna la cantidad de compras ejecutadas en este ciclo.
     *
     * @return Cantidad de compras ejecutadas en este ciclo
     */
    public int getCompras() {
        return compras;
    }

    /**
     * Setea la cantidad de compras realizadas en este ciclo.
     *
     * @param compras Nuevo valor de compras
     */
    public void setCompras(int compras) {
        this.compras = compras;
    }

    /**
     * Retorna la cantidad de ventas realizadas en este ciclo.
     *
     * @return Cantidad de ventas realizadas en este ciclo
     */
    public int getVentas() {
        return ventas;
    }

    /**
     * Setea la cantidad de ventas realizadas en este ciclo.
     *
     * @param ventas Cantidad de ventas realizadas en este ciclo
     */
    public void setVentas(int ventas) {
        this.ventas = ventas;
    }


}
