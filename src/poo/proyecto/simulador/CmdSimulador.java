package poo.proyecto.simulador;

import poo.proyecto.helpers.InputHelper;
import poo.proyecto.mercados.Merval;

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

        generarInversores(countInversores);

    }


    @Override
    public void start() {


        try {
            setMercado(new Merval());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getMercado().Initialize();
        generarAgentes(2);
        generarInversores(5);


        mainLoop();

//        while (true) {
//            char option = menu();
//
//            switch (option) {
//                case 'q':
//                    System.exit(0);
//                    break;
//                case 'a':
//                    generarAgentes();
//                    break;
//                case 'i':
//                    generarInversores();
//                    break;
//                case 's':
//                    mainLoop();
//                    break;
//            }
//        }

    }

    @Override
    public void mainLoop() {

        int i = 10000000;
        while (i-- > 0) {
            this.iterate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

