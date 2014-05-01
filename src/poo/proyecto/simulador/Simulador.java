package poo.proyecto.simulador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Mercado;

public abstract class Simulador {

	protected boolean running = false;

	protected final ArrayList<AgenteDeBolsa> agentes = new ArrayList<AgenteDeBolsa>();
	protected final Mercado mercado = new Mercado();

	public List<AgenteDeBolsa> getAgentes() {
		return Collections.unmodifiableList(agentes);
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

		for (AgenteDeBolsa agente : agentes) {
			agente.notificarIteracion(mercado);
		}

		System.out.println("Finalizando iteracion.\n");

	}

	public abstract void mainLoop();

}
