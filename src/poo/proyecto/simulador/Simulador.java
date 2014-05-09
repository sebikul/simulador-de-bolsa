package poo.proyecto.simulador;

import poo.proyecto.exceptions.NoHayElementosException;
import poo.proyecto.modelos.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Simulador extends Thread {

    static public final int DEFAULT_SIM_CYCLES = 365;
    protected final ArrayList<AgenteDeBolsa> agentes = new ArrayList<AgenteDeBolsa>();
    private final SimuladorHook hooks;
    protected boolean running = false;
    private boolean hasStarted = false;
    private Mercado mercado;
    private long ciclo = 0;
    private boolean generoAgentes = false;
    private boolean generoInversores = false;
    private boolean mercadoSeteado = false;

    public Simulador() {

        this.hooks = null;
    }

    public Simulador(SimuladorHook hook) {
        this.hooks = hook;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public final long getCiclo() {
        return ciclo;
    }

    public final List<AgenteDeBolsa> getAgentes() {
        return Collections.unmodifiableList(agentes);
    }

    public final Mercado getMercado() {
        return mercado;
    }

    public final void setMercado(Mercado mercado) throws Exception {
        if (mercado == null || this.mercado != null)
            throw new Exception();

        this.mercado = mercado;
        this.mercado.cargar();
        mercadoSeteado = true;
    }

    public final void generarAgentes(int cantidad) {

        for (int i = 0; i < cantidad; i++) {
            agentes.add(new AgenteDeBolsa("Agente " + (i + 1)));
        }

        generoAgentes = true;
    }

    public final void generarInversores(int cantidad) throws Exception {

        if (!generoAgentes) {
            throw new Exception("No se generaron agentes de bolsa");
        }

        Decididor<AgenteDeBolsa> decididorDeAgentes = new Decididor<AgenteDeBolsa>();

        for (AgenteDeBolsa agente : agentes) {
            decididorDeAgentes.addDecision(new Decision<AgenteDeBolsa>(agente,
                    1));
        }

        AgenteDeBolsa agente;

        for (int i = 0; i < cantidad; i++) {

            try {
                agente = decididorDeAgentes.getDecision().getObject();
            } catch (NoHayElementosException e) {
                e.printStackTrace();
                break;
            }

            agente.agregarInversor(new Inversor("Inversor " + (i + 1), 1000,
                    agente));
        }

        generoInversores = true;
    }

    public final void run() throws IllegalStateException {

        if (!generoAgentes || !generoInversores || !mercadoSeteado) {
            throw new IllegalStateException();
        }

        running = true;
        hasStarted = true;
        mainLoop();
    }

    public boolean isReady() {
        return (generoAgentes && generoInversores && mercadoSeteado);
    }

    public final boolean isRunning() {
        return this.running;
    }

    public final void detenerSimulador() {
        this.running = false;
    }

    public final void resumirSimulador() {
        this.running = true;
    }


    public final ResultadosSimulacion resultados() throws Exception {

        if (running) {
            throw new Exception("La simulacion debe estar detenida");
        }
        ResultadosSimulacion resultados = new ResultadosSimulacion();
        resultados.setTitulos(mercado.getTitulos().values());
        resultados.setInversores(agentes);

        return resultados;
    }

    private final void mainLoop() {

        Collection<Titulo> titulos = this.getMercado().getTitulos().values();

        while (true) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!running) {
                continue;
            }

            if (hooks != null) {
                hooks.preIteracion();
            }

            for (Titulo titulo : titulos) {

                titulo.notificarComienzoCiclo();
            }

            for (AgenteDeBolsa agente : agentes) {
                agente.notificarIteracion(mercado);
            }

            for (Titulo titulo : titulos) {

                titulo.notificarFinCiclo();
            }

            if (hooks != null) {
                hooks.postIteracion();
            }


            ciclo++;
        }

    }

}
