package poo.proyecto.modelos;


import java.util.Date;

public class Bono extends Titulo {

    private long interes;
    private double valor;
    private final Date vencimiento;


    public Bono(String simbolo, double valorInicial, Date vencimiento) {
        super(simbolo, valorInicial);
        this.vencimiento = vencimiento;
    }

    @Override
    public final String getName() {
        return this.getClass().getName();
    }

    //TODO
    public double calcularDividendo() {

        return 0;
    }
}
