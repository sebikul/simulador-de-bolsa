package poo.proyecto.simulador;

import poo.proyecto.exceptions.NoHayElementosException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Toma una decision sobre una lista de objetos ponderados.
 *
 * @param <T> Tipo de los objetos a decidir.
 */
public class Decididor<T> {

    private final Random random;

    /**
     * Lista de objetos sobre la cual hay que tomar una decision.
     */
    private final ArrayList<Decision<T>> decisiones;

    /**
     * Construye e inicializa el objeto para tomar una decision.
     */
    public Decididor() {
        this.random = new Random();
        this.decisiones = new ArrayList<Decision<T>>();
    }

    /**
     * Sobre la lista de objetos poderados devuelve una eleccion aleatoria.
     *
     * @return Objeto aleatorio dentro de la lista de decisiones.
     * @throws NoHayElementosException No hay elementos para realizar la decision.
     */
    public Decision<T> getDecision() throws NoHayElementosException {

        if (decisiones.size() == 0) {
            throw new NoHayElementosException();
        }

        int pesoTotal = 0;
        int i;

        for (Decision<T> decision : decisiones) {
            pesoTotal += decision.getPeso();
        }

        int seleccion = this.random.nextInt(pesoTotal);

        pesoTotal = decisiones.get(0).getPeso();

        for (i = 0; i < decisiones.size() && pesoTotal <= seleccion; i++) {
            pesoTotal += decisiones.get(i + 1).getPeso();
        }

        return decisiones.get(i);

    }

    /**
     * Agrega una decision posible a la lista.
     *
     * @param decision Decision a agregar.
     * @throws IllegalArgumentException Si la decision es un objeto nulo.
     */
    public void addDecision(Decision<T> decision)
            throws IllegalArgumentException {

        if (decision == null) {
            throw new IllegalArgumentException();
        }

        this.decisiones.add(decision);
    }

}