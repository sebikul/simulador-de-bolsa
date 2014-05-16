package poo.proyecto.simulador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import poo.proyecto.exceptions.NoHayElementosException;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;
import poo.proyecto.modelos.Mercado;
import poo.proyecto.modelos.ResultadosSimulacion;
import poo.proyecto.modelos.Titulo;

/**
 * Describe la implementacion de un simulador.
 */
public abstract class Simulador extends Thread {

	/**
	 * Cantidad de ciclos por defecto que seran simulados.
	 */
	static public final int DEFAULT_SIM_CYCLES = 365;

	/**
	 * Almacena los agentes de bolsa.
	 */
	private final ArrayList<AgenteDeBolsa> agentes = new ArrayList<AgenteDeBolsa>();

	/**
	 * Almacena la implementacion de SimuladorHook, de existir.
	 */
	protected SimuladorHook hooks;

	/**
	 * Determina si la simulacion esta corriendo.
	 */
	private boolean running = false;

	/**
	 * Determina si el simulador ya fue ejecutado por lo menos una vez.
	 */
	private boolean hasStarted = false;

	/**
	 * Almacena el mercado a simular.
	 */
	private Mercado mercado = null;

	/**
	 * Almacena el ciclo en el cual se encuentra la simulacion.
	 */
	private int ciclo = 0;

	/**
	 * Almacena la cantidad de ciclos que seran simulados.
	 */
	private int maxCiclos;

	/**
	 * True si los agentes ya fueron generados.
	 */
	private boolean generoAgentes = false;

	/**
	 * True si los inversores ya fueron generados.
	 */
	private boolean generoInversores = false;

	/**
	 * True si ya se seteo el mercado a simular.
	 */
	private boolean mercadoSeteado = false;

	/**
	 * Construye un simulador que iterara DEFAULT_SIM_CYCLES veces.
	 */
	public Simulador() {
		this(DEFAULT_SIM_CYCLES);
	}

	/**
	 * Construye un simulador.
	 * 
	 * @param maxCiclos
	 *            Cantidad de ciclos a simular.
	 */
	public Simulador(int maxCiclos) {
		this(maxCiclos, null);
	}

	/**
	 * Construye un simulador con una implementacion de SimuladorHook. Los
	 * metodos definidos en hook seran llamados en momentos especificos de una
	 * iteracion.
	 * 
	 * @param hook
	 *            Implementacion de SimuladorHook para ser llamada en momentos
	 *            especificos de la simulacion.
	 */
	public Simulador(SimuladorHook hook) {
		this(DEFAULT_SIM_CYCLES, hook);
	}

	/**
	 * Construye un simulador con una implementacion de SimuladorHook. Los
	 * metodos definidos en hook seran llamados en momentos especificos de una
	 * iteracion.
	 * 
	 * @param maxCiclos
	 *            Cantidad de ciclos a simular.
	 * @param hooks
	 *            Implementacion de SimuladorHook para ser llamada en momentos
	 *            especificos de la simulacion.
	 */
	public Simulador(int maxCiclos, SimuladorHook hooks) {
		this.hooks = hooks;
		this.maxCiclos = maxCiclos;

		this.setName("Simulador");

		if (this.hooks != null)
			hooks.prepararSimulacion();
	}

	/**
	 * Devuelve la cantidad de ciclos a simular.
	 * 
	 * @return Cantidad de ciclos a simular.
	 */
	public int getMaxCiclos() {
		return maxCiclos;
	}

	/**
	 * Setea la cantidad de ciclos a simular.
	 * 
	 * @param maxCiclos
	 *            Cantidad de ciclos a simular.
	 */
	public void setMaxCiclos(int maxCiclos) {
		this.maxCiclos = maxCiclos;
	}

	/**
	 * Devuelve True si el simulador comenzo a ejecutarse. Independiente de su
	 * estado actual.
	 * 
	 * @return True si el simulador de ejecuto alguna vez.
	 */
	public boolean hasStarted() {
		return hasStarted;
	}

	/**
	 * Devuelve el ciclo en el cual se encuentra el simulador.
	 * 
	 * @return Ciclo en el cual se encuentra el simulador
	 */
	public final int getCiclo() {
		return ciclo;
	}

	/**
	 * Devuelve una lista inmutable con los agentes de bolsa.
	 * 
	 * @return Lista inmutable de los agentes de bolsa.
	 */
	public final List<AgenteDeBolsa> getAgentes() {
		return Collections.unmodifiableList(agentes);
	}

	/**
	 * Devuelve el mercado que se esta simulando.
	 * 
	 * @return Mercado que se esta simulando.
	 */
	public final Mercado getMercado() {
		return mercado;
	}

	/**
	 * Setea el mercado que sera simulado. Solo puede llamarse una vez.
	 * 
	 * @param mercado
	 *            Mercado que se desea simular
	 * @throws Exception
	 *             Si el mercado es nulo o ya esta seteado.
	 */
	public final void setMercado(Mercado mercado) throws Exception {
		if (mercado == null || this.mercado != null)
			throw new Exception();

		this.mercado = mercado;
		this.mercado.cargar();
		mercadoSeteado = true;
	}

	/**
	 * Genera los agentes que podran operar en el mercado.
	 * 
	 * @param cantidad
	 *            Cantidad de agentes a crear.
	 */
	public final void generarAgentes(int cantidad) {

		for (int i = 0; i < cantidad; i++) {
			agentes.add(new AgenteDeBolsa("Agente " + (i + 1)));
		}

		generoAgentes = true;
	}

	public final int getVolumenEnCirculacion(Titulo titulo) {

		int volumen = 0;

		for (AgenteDeBolsa agenteDeBolsa : agentes) {
			for (Inversor inversor : agenteDeBolsa.getClientes().keySet()) {
				if (inversor.getTitulos().containsKey(titulo)) {
					volumen += inversor.getTitulos().get(titulo);
				}
			}
		}

		return volumen;

	}

	/**
	 * Genera los inversores que podran operar en el mercado. Se asignan
	 * aleatoriamente a los agentes creados con generarAgentes().
	 * 
	 * @param cantidad
	 *            Cantidad de inversores a crear.
	 * @throws Exception
	 *             Si no se generaron agentes de bolsa.
	 */
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

	/**
	 * Comienza a ejecutar la simulacion.
	 * 
	 * @throws IllegalStateException
	 *             si no se generaron agentes, inversores, o no se seteo el
	 *             mercado.
	 */
	public final void run() throws IllegalStateException {

		if (!generoAgentes || !generoInversores || !mercadoSeteado) {
			throw new IllegalStateException();
		}

		running = true;
		hasStarted = true;
		mainLoop();
	}

	/**
	 * Determina si el simulador puede ser ejecutado.
	 * 
	 * @return True si se puede ejecutar el simulador.
	 */
	public boolean isReady() {
		return (generoAgentes && generoInversores && mercadoSeteado);
	}

	/**
	 * Devuelve True si el simulador esta corriendo.
	 * 
	 * @return True si el simulador esta corriendo.
	 */
	public final boolean isRunning() {
		return this.running;
	}

	/**
	 * Detiene la simulacion.
	 */
	public final void detenerSimulador() {
		this.running = false;
	}

	/**
	 * Resume la simualacion.
	 */
	public final void resumirSimulador() {
		this.running = true;
	}

	/**
	 * Devuelve un objeto con los resultados de la simulacion.
	 * 
	 * @return Objecto con los resultados de la simulacion.
	 * @throws Exception
	 *             Si el simulador esta corriendo.
	 */
	public final ResultadosSimulacion resultados() throws Exception {

		if (running) {
			throw new Exception("La simulacion debe estar detenida");
		}
		ResultadosSimulacion resultados = new ResultadosSimulacion();
		resultados.setTitulos(mercado.getTitulos().values());
		resultados.setInversores(agentes);

		return resultados;
	}

	/**
	 * Bucle principal del simulador.
	 */
	private void mainLoop() {

		Collection<Titulo> titulos = this.getMercado().getTitulos().values();

		while (ciclo <= maxCiclos) {

			// Pequeña demora para no trabar el CPU
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Si el simulador esta en pausa saltear el resto del bucle.
			if (!running)
				continue;

			if (hooks != null)
				hooks.preIteracion();

			// Iteracion para notificar a los titulos del comienzo de un ciclo.
			for (Titulo titulo : titulos)
				titulo.notificarComienzoCiclo();

			// Iteracion para notificar a los agentes de bolsa de que deben
			// operar.
			for (AgenteDeBolsa agente : agentes)
				agente.notificarIteracion(mercado);

			// Iteracion para notificar a los titulos del fin de un ciclo.
			for (Titulo titulo : titulos)
				titulo.notificarFinCiclo();

			// TODO notificar a inversores para valor historico.

			if (hooks != null)
				hooks.postIteracion();

			ciclo++;
		}

	}

}