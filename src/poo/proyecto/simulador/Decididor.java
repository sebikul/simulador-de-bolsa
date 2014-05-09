package poo.proyecto.simulador;

import poo.proyecto.exceptions.NoHayElementosException;

import java.util.ArrayList;
import java.util.Random;

public class Decididor<T> {

    private final Random random;
    private final ArrayList<Decision<T>> decisiones;

    public Decididor() {
        this.random = new Random();
        this.decisiones = new ArrayList<Decision<T>>();
    }

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

    public void addDecision(Decision<T> decision)
            throws IllegalArgumentException {

        if (decision == null) {
            throw new IllegalArgumentException();
        }

        this.decisiones.add(decision);
    }

}
