package poo.proyecto.simulador;

public class Decision<T> {

    private final T o;
    private final int peso;


    public Decision(T o, int peso) {
        this.o = o;
        this.peso = peso;
    }

    public final T getObject() {
        return this.o;
    }


    public final int getPeso() {
        return this.peso;
    }


}
