package poo.proyecto.modelos.titulos;


import java.util.Date;

import poo.proyecto.modelos.Titulo;

public class Bono extends Titulo {

    private final Date vencimiento;
    private long interes;
    private double valor;


    public Bono(String simbolo, double valorInicial, int volumen, Date vencimiento) {
        super(simbolo, valorInicial, volumen);
        this.vencimiento = vencimiento;
    }


    //TODO
    public double calcularDividendo() {

        return 0;
    }
}
