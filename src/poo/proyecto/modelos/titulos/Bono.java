package poo.proyecto.modelos.titulos;


import poo.proyecto.modelos.Titulo;

import java.util.Date;

public class Bono extends Titulo {

    private final Date vencimiento;
    private long interes;


    public Bono(String simbolo, double valorInicial, int volumen, Date vencimiento) {
        super(simbolo, valorInicial, volumen);
        this.vencimiento = vencimiento;
    }


    //TODO
    public double calcularDividendo() {

        return 0;
    }
}
