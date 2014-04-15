package poo.proyecto.simulador;

import poo.proyecto.modelos.Accion;
import poo.proyecto.modelos.Inversor;

import java.io.Console;

public final class CmdSimulador extends Simulador {

    private final Console c;

    public CmdSimulador() {
        c = System.console();
        InputHelper.init(c);
    }

    public void cargar() {

        inversores.add(new Inversor("Pepe", 1000));
        inversores.add(new Inversor("Jose", 800));
        inversores.add(new Inversor("Ignacio", 400));
        inversores.add(new Inversor("Roberto", 3000));


        mercado.addTitulo(new Accion("GOOG", 40));
        mercado.addTitulo(new Accion("APPL", 100));
        mercado.addTitulo(new Accion("TWTR", 100));

//        try {
//            inversores.get(3).comprarTitulo(mercado, "TWTR", 3);
//
//            inversores.get(3).comprarTitulo(mercado, "APPL", 2);
//
//            inversores.get(3).comprarTitulo(mercado, "GOOG", 1);
//
//
//        } catch (TituloNoExisteException e) {
//            e.printStackTrace();
//            System.out.println(e.getSimbolo());
//        } catch (CapitalInsuficienteException e) {
//            e.printStackTrace();
//        }


//        for (Inversor inversor : inversores) {
//            System.out.println(inversor.printDebugInfo() + "\n");
//
//        }
//
//        System.out.println(mercado);

    }


    private char menu() {

        c.flush();

        c.printf("\n\nMenu principal:\n");
        c.printf("a: Agregar un inversor.\n");
        c.printf("l: Ver inversores.\n");


        c.printf("s: Iniciar el simulador.\n");
        c.printf("q: Salir\n");

        String io;

        do {
            io = c.readLine("Ingrese una opcion: ");
        } while (io.length() != 1);

        return io.toCharArray()[0];


    }


    public void agregarInversor() {

        String nombre = InputHelper.getString("Ingrese un nombre para el inversor", "El nombre no puede estar vacio!");

        double capital = InputHelper.getDouble("Ingrese un capital inicial para el inversor", "El capital debe ser un valor numerico!", true, 0);

        this.inversores.add(new Inversor(nombre, capital));


    }

    public void listarInversores() {

        for (Inversor inversor : this.inversores) {
            c.printf(inversor.printDebugInfo() + '\n');
        }

        menu();

    }

    @Override
    public void start() {

        cargar();
        mainLoop();

        /*while (true) {
            char option = menu();

            switch (option) {
                case 'q':
                    System.exit(0);
                    break;
                case 'a':
                    agregarInversor();
                    break;
                case 'l':
                    listarInversores();
                    break;
                case 's':
                    mainLoop();
                    break;
            }
        }*/


    }

    @Override
    public void mainLoop() {

        while (true) {
            this.iterate();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class InputHelper {

    static private Console c;

    static public void init(Console c) {
        InputHelper.c = c;
    }

    private static String ask(String m) {

        return c.readLine(m).trim();
    }

    public static double getDouble(String m, String e) {
        return InputHelper.getDouble(m, e, false, 0);
    }

    public static double getDouble(String msg, String err, boolean hasDef, double def) {

        double ans = 0.0;
        String strIn;
        boolean ansInvalida = false;

        do {
            if (ansInvalida) {
                c.printf("\n" + err + "\n");
            }
            strIn = ask(msg + (hasDef ? " [" + (double) 0 + "]" : "") + ": ");

            if (!strIn.isEmpty()) {

                ansInvalida = false;
                try {
                    ans = Double.parseDouble(strIn);
                } catch (Exception e) {
                    ansInvalida = true;
                }
            } else if (hasDef) {
                ansInvalida = false;
                ans = (double) 0;
            }


        } while (ansInvalida);

        return ans;

    }

    public static String getString(String msg, String err) {
        return InputHelper.getString(msg, err, false, null);

    }

    public static String getString(String msg, String err, boolean hasDef, String def) {
        String strIn;
        boolean strInvalida = false;

        do {
            if (strInvalida) {
                c.printf("\n" + err + "\n");
            }

            strIn = ask(msg + (hasDef ? " [" + def + "]" : "") + ": ");

            if (strIn.isEmpty() && hasDef) {

                strInvalida = false;
                strIn = def;

            } else {
                strInvalida = strIn.isEmpty();
            }

        } while (strInvalida);

        return strIn;
    }

}