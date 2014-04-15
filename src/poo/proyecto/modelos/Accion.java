package poo.proyecto.modelos;


public class Accion extends Titulo {

    public Accion(String simbolo, double valorInicial) {
        super(simbolo, valorInicial);
    }

    @Override
    public final String getName() {
        return this.getClass().getName();
    }

    //TODO
    public void notifyCompra(int cant){

    }

    //TODO
    public void notifyVenta(int cant){

    }


}
