package poo.proyecto.modelos.titulos;


import poo.proyecto.modelos.Titulo;

import java.util.Date;

public class Bono extends Titulo {

    private final Date vencimiento;
    private long interes;
    private double valor;


    public Bono(String simbolo, double valorInicial, Date vencimiento) {
        super(simbolo, valorInicial);
        this.vencimiento = vencimiento;
    }

    //TODO
    public double calcularDividendo() {

        return 0;
    }
}
