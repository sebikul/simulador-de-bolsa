package poo.proyecto.simulador;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.NoHayElementosException;
import poo.proyecto.exceptions.TituloNoExisteException;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.Mercado;
import poo.proyecto.modelos.Titulo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class Simulador {

    protected boolean running = false;

    protected final ArrayList<Inversor> inversores = new ArrayList<Inversor>();
    protected final Mercado mercado = new Mercado();

    public List<Inversor> getInversores() {
        return Collections.unmodifiableList(inversores);
    }

    public Mercado getMercado() {
        return mercado;
    }

    public abstract void start();

    public void run() {
        this.running = true;
    }

    public boolean getStatus() {
        return this.running;
    }

    public void stop() {
        this.running = false;
    }

    protected void iterate() {

        System.out.println("Comenzando iteracion:");

        Decididor decididorDeInversores = new Decididor();
        Decididor decididorDeTitulos = new Decididor();

        for (Inversor inversor : inversores) {
            decididorDeInversores.addDecision(new Decision<Inversor>(inversor, (int) (inversor.getRiesgo() * 100)));
        }

        for (Titulo titulo : mercado.getTitulos().values()) {
            decididorDeTitulos.addDecision(new Decision<Titulo>(titulo, ((int) titulo.getValor())));
        }


        try {
            for (int i = 0; i < inversores.size(); i++) {

                Inversor inversor = (Inversor) decididorDeInversores.getDecision().getObject();

                Titulo titulo = (Titulo) decididorDeTitulos.getDecision().getObject();

                try {
                    String simbolo = titulo.getSimbolo();

                    double capital = inversor.getCapital();
                    double costo = titulo.getValor();
                    double riesgo = inversor.getRiesgo();

                    int cantidad = (int) (capital / costo * riesgo);


                    inversor.comprarTitulo(mercado, simbolo, cantidad + 1);


                } catch (TituloNoExisteException e) {
                    e.printStackTrace();
                } catch (CapitalInsuficienteException e) {
                    System.out.println(inversor + " no tiene capital suficiente para comprar " + titulo);
                }

            }


            for (int i = 0; i < inversores.size(); i++) {

                Inversor inversor = (Inversor) decididorDeInversores.getDecision().getObject();

                Titulo titulo = (Titulo) decididorDeTitulos.getDecision().getObject();

                if (inversor.getTitulos().containsKey(titulo.getSimbolo())) {
                    try {

                        double capital = inversor.getCapital();
                        double costo = titulo.getValor();
                        double riesgo = inversor.getRiesgo();

                        int cantidad = (int) (capital / costo * riesgo);

                        inversor.venderTitulo(mercado, titulo.getSimbolo(), cantidad + 1);
                    } catch (TituloNoExisteException e) {
                        e.printStackTrace();
                    } catch (CapitalInsuficienteException e) {
                        System.out.println(inversor + " no tiene titulos para vender " + titulo);
                    }
                }


            }

            System.out.println("Finalizando iteracion.\n");


        } catch (NoHayElementosException e) {
            e.printStackTrace();
        }

    }

    public abstract void mainLoop();

}


