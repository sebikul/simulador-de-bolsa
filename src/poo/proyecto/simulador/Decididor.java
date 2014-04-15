package poo.proyecto.simulador;

import poo.proyecto.exceptions.NoHayElementosException;

import java.util.ArrayList;
import java.util.Random;

public class Decididor {

    private final Random random = new Random();
    private final ArrayList<Decision> decisiones;

    public Decididor() {
        this.decisiones = new ArrayList<Decision>();
    }

    public Decision getDecision() throws NoHayElementosException {

        if (decisiones.size() == 0) {
            throw new NoHayElementosException();
        }


        int pesoTotal = 0;
        int i;

        for (Decision decision : decisiones) {
            pesoTotal += decision.getPeso();
        }

        int seleccion = this.random.nextInt(pesoTotal);

        pesoTotal = decisiones.get(0).getPeso();

        for (i = 0; (i < (decisiones.size() - 1)) && (pesoTotal <= seleccion); i++) {
            pesoTotal += decisiones.get(i + 1).getPeso();
        }

        return decisiones.get(i);


    }


    public void addDecision(Decision decision) throws IllegalArgumentException {

        if (decision == null) {
            throw new IllegalArgumentException();
        }

        this.decisiones.add(decision);
    }

}
