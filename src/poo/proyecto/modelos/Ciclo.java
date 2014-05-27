package poo.proyecto.modelos;

import java.io.Serializable;

public class Ciclo implements Serializable {

    private double valor;
    private int compras;
    private int ventas;

    public Ciclo(double valor) {
        this.valor = valor;
        this.compras = 0;
        this.ventas = 0;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getCompras() {
        return compras;
    }

    public void setCompras(int compras) {
        this.compras = compras;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }


}
