package poo.proyecto.simulador;

import poo.proyecto.helpers.InputHelper;

import java.io.Console;

public final class CmdSimulador extends Simulador {

    private final Console c;
    private final InputHelper input;

    public CmdSimulador() {
        c = System.console();
        input = new InputHelper(c);
    }

    private char menu() {

        c.flush();

        c.printf("*******************************************\n");

        c.printf("\n\nMenu principal:\n");

        c.printf("a: Generar Agentes.\n");
        c.printf("i: Generar inversores.\n");

        c.printf("s: Iniciar el simulador.\n");
        c.printf("q: Salir\n");

        String io;

        do {
            io = c.readLine("Ingrese una opcion: ");
        } while (io.length() == 0);

        return io.toCharArray()[0];

    }

    public void generarAgentes() {

        int countAgentes = 0;

        do {
            countAgentes = input.getInt(
                    "Ingrese la cantidad de agentes a generar.",
                    "El numero debe ser un valor entero mayor que 0.");
        } while (countAgentes <= 0);

        generarAgentes(countAgentes);

    }

    public void generarInversores() {

        int countInversores = 0;

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
