package poo.proyecto.Algoritmos;

/**
 * Interfaz para establecer los metodos que un algoritmo debe implementar.
 */
public interface Algorithm {

    public double getNuevoPrecioCompra(int cantidad);

    public double getNuevoPrecioVenta(int cantidad);

    public void notificarComienzoCiclo();


}
