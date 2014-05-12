package poo.proyecto.simulador;

import poo.proyecto.helpers.InputHelper;

import java.io.Console;

/**
 * Implementacion de un simulador por consola.
 */
public final class CmdSimulador extends Simulador {

    /**
     * Almacena la consola con la cual se hara la interaccion con
     * el usuario.
     */
    private final Console c;

    /**
     * Instancia de InputHelper usada para leer datos desde la consola.
     */
    private final InputHelper input;

    /**
     * Construye una instancia del simulador.
     */
    public CmdSimulador() {
        hooks = new SimuladorHook() {

            @Override
            public void prepararSimulacion() {
                generarAgentes();
                generarInversores();
                pedirMaxCiclos();
            }
        };
        c = System.console();
        input = new InputHelper(c);

        this.start();
    }

    /**
     * Pide al usuario que ingrese la cantidad de agentes que desea crear.
     */
    public void generarAgentes() {

        int countAgentes;

        do {
            countAgentes = input.getInt(
                    "Ingrese la cantidad de agentes a generar.",
                    "El numero debe ser un valor entero mayor que 0.");
        } while (countAgentes <= 0);

        generarAgentes(countAgentes);

    }

    /**
     * Pide al usuario que ingrese la cantidad de ciclos a simular.
     */
    public void pedirMaxCiclos() {

        int ciclos;

        do {
            ciclos = input.getInt(
                    "Ingrese la cantidad de ciclos a simular.",
                    "El numero debe ser un valor entero mayor que 0.");
        } while (ciclos <= 0);

        setMaxCiclos(ciclos);

    }

    /**
     * Pide al usuario que ingrese la cantidad de inversores que desea crear.
     */
    public void generarInversores() {

        int countInversores;

        do {
            countInversores = input.getInt(
                    "Ingrese la cantidad de inversores a generar.",
                    "El numero debe ser un valor entero mayor que 0.");
        } while (countInversores <= 0);

        try {
            generarInversores(countInversores);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}