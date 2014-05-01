package poo.proyecto.modelos;


public class Accion extends Titulo {

    //private long volumen;


    public Accion(String simbolo, double valorInicial) {
        super(simbolo, valorInicial);
    }

    @Override
    public final String getName() {
        return this.getClass().getName();
    }


}
