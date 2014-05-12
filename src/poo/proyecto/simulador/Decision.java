package poo.proyecto.simulador;

/**
 * Representa una decision a tomar.
 *
 * @param <T> Tipo de objeto a decidir.
 */
public class Decision<T> {

    /**
     * Almacena el objeto sobre el cual se tomara una decision.
     */
    private final T o;

    /**
     * Almacena el peso que pondera a la decision actual.
     */
    private final int peso;

    /**
     * Construye un objeto de decision.
     *
     * @param o    Objeto sobre el cual se tomara una decision.
     * @param peso Peso ponderado del objeto actual.
     */
    public Decision(T o, int peso) {
        this.o = o;
        this.peso = peso;
    }

    /**
     * Devuelve el objeto de la decision.
     *
     * @return Objeto de la decision.
     */
    public final T getObject() {
        return this.o;
    }

    /**
     * Devuelve el peso de la decision.
     *
     * @return peso de la decision.
     */
    public final int getPeso() {
        return this.peso;
    }


}