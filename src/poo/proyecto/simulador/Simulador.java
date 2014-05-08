package poo.proyecto.simulador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poo.proyecto.exceptions.NoHayElementosException;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.Mercado;
import poo.proyecto.modelos.ResultadosSimulacion;
import poo.proyecto.modelos.Titulo;

public abstract class Simulador {

	static public final int DEFAULT_SIM_CYCLES = 365;
	protected final ArrayList<AgenteDeBolsa> agentes = new ArrayList<AgenteDeBolsa>();
	protected boolean running = false;
	private Mercado mercado;
	private long ciclo = 0;

	public long getCiclo() {
		return ciclo;
	}

	public List<AgenteDeBolsa> getAgentes() {
		return Collections.unmodifiableList(agentes);
	}

	public Mercado getMercado() {
		return mercado;
	}

	public void setMercado(Mercado mercado) throws Exception {
		if (mercado == null)
			throw new Exception();

		this.mercado = mercado;
	}

	public abstract void start();

	public void generarAgentes(int cantidad) {

		for (int i = 0; i < cantidad; i++) {
			agentes.add(new AgenteDeBolsa("Agente " + (i + 1)));
		}

	}

	public void generarInversores(int cantidad) {

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

	}

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

		//System.out.println("Comenzando iteracion " + getCiclo() + ":");

		for (Titulo titulo : this.getMercado().getTitulos().values()) {

			titulo.notificarComienzoCiclo();
		}

		for (AgenteDeBolsa agente : agentes) {
			agente.notificarIteracion(mercado);
		}

		for (Titulo titulo : this.getMercado().getTitulos().values()) {

			titulo.notificarFinCiclo();
		}

		ciclo++;

		//System.out.println("Finalizando iteracion.\n");

	}

	public ResultadosSimulacion resultados() {
		ResultadosSimulacion resultados = new ResultadosSimulacion();
		resultados.setTitulos(mercado.getTitulos().values());
		resultados.setInversores(agentes);

		return resultados;
	}

	public abstract void mainLoop();

}
