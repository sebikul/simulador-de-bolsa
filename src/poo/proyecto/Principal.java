package poo.proyecto;


import poo.proyecto.helpers.GraficarTitulo;
import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.Titulo;
import poo.proyecto.simulador.CmdSimulador;

public class Principal {

    public static void main(String[] args) {


        CmdSimulador simulador1 = new CmdSimulador();

        simulador1.generarAgentes(7);
        try {
            simulador1.generarInversores(70);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            simulador1.setMercado(new Merval());
        } catch (Exception e) {
            e.printStackTrace();
        }

        simulador1.run();

        try {
            simulador1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(simulador1.resultados());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Titulo titulo : simulador1.getMercado().getTitulos().values()) {
            GraficarTitulo.graficar(titulo);
        }


    }

}
