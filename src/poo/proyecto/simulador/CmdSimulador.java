package poo.proyecto.simulador;

import poo.proyecto.modelos.Accion;
import poo.proyecto.modelos.AgenteDeBolsa;
import poo.proyecto.modelos.Inversor;

import java.io.Console;

public final class CmdSimulador extends Simulador {

	private final Console c;

	public CmdSimulador() {
		c = System.console();
		InputHelper.init(c);
	}

	public void cargar() {

		agentes.add(new AgenteDeBolsa("Agente 1"));

		agentes.get(0).agregarInversor(
				new Inversor("Pepe", 1000, agentes.get(0)));
		agentes.get(0).agregarInversor(
				new Inversor("Jose", 800, agentes.get(0)));
		agentes.get(0).agregarInversor(
				new Inversor("Ignacio", 400, agentes.get(0)));
		agentes.get(0).agregarInversor(
				new Inversor("Roberto", 3000, agentes.get(0)));

		mercado.addTitulo(new Accion("GOOG", 40));
		mercado.addTitulo(new Accion("APPL", 100));
		mercado.addTitulo(new Accion("TWTR", 100));

		// try {
		// inversores.get(3).comprarTitulo(mercado, "TWTR", 3);
		//
		// inversores.get(3).comprarTitulo(mercado, "APPL", 2);
		//
		// inversores.get(3).comprarTitulo(mercado, "GOOG", 1);
		//
		//
		// } catch (TituloNoExisteException e) {
		// e.printStackTrace();
		// System.out.println(e.getSimbolo());
		// } catch (CapitalInsuficienteException e) {
		// e.printStackTrace();
		// }

		// for (Inversor inversor : inversores) {
		// System.out.println(inversor.printDebugInfo() + "\n");
		//
		// }
		//
		// System.out.println(mercado);

	}

	private char menu() {

		c.flush();

		c.printf("*******************************************\n");

		c.printf("\n\nMenu principal:\n");
		c.printf("a: Agregar un agente.\n");
		c.printf("l: Ver agentes.\n");

		c.printf("d: Agregar un inversor.\n");
		c.printf("i: Listar info de un agente.\n");

		c.printf("s: Iniciar el simulador.\n");
		c.printf("q: Salir\n");

		String io;

		do {
			io = c.readLine("Ingrese una opcion: ");
		} while (io.length() == 0);

		return io.toCharArray()[0];

	}

	public void agregarInversor() {

		String nombre = InputHelper.getString(
				"Ingrese un nombre para el inversor",
				"El nombre no puede estar vacio!");

		double capital = InputHelper.getDouble(
				"Ingrese un capital inicial para el inversor",
				"El capital debe ser un valor numerico!", true, 0);

		AgenteDeBolsa agente = getAgente();

		agente.agregarInversor(new Inversor(nombre, capital, agente));

	}

	public AgenteDeBolsa getAgente() {

		int agenteInd = 0;

		do {
			agenteInd = InputHelper.getInt(
					"Ingrese el indice del agente de bolsa.",
					"El indice debe ser un valor entero mayor o igual que 0.");
		} while (agenteInd < 0 || agenteInd >= agentes.size());

		AgenteDeBolsa agente = agentes.get(agenteInd);

		return agente;

	}

	public void agregarAgente() {

		String nombre = InputHelper.getString(
				"Ingrese un nombre para el agente",
				"El nombre no puede estar vacio!");

		agentes.add(new AgenteDeBolsa(nombre));

	}

	public void listarAgentes() {

		for (AgenteDeBolsa agente : agentes) {
			c.printf(agente.printDebugInfo() + '\n');
		}

		menu();

	}

	// TODO
	public void listarInfoAgente() {

		AgenteDeBolsa agente = getAgente();

		c.printf(agente.printDebugInfo() + '\n');

		menu();

	}

	@Override
	public void start() {

		cargar();
		mainLoop();

		while (true) {
			char option = menu();

			switch (option) {
			case 'q':
				System.exit(0);
				break;
			case 'a':
				agregarAgente();
				break;
			case 'd':
				listarAgentes();
				agregarInversor();
				break;
			case 'l':
				listarAgentes();
				break;
			case 'i':
				listarInfoAgente();
				break;
			case 's':
				mainLoop();
				break;
			}
		}

	}

	@Override
	public void mainLoop() {

		int i = 10000000;
		while (i-- > 0) {
			this.iterate();
			// try {
			// Thread.sleep(500);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
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

	public static int getInt(String m, String e) {
		return InputHelper.getInt(m, e, false, 0);
	}

	public static double getDouble(String msg, String err, boolean hasDef,
			double def) {

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
				ans = 0.0;
			}

		} while (ansInvalida);

		return ans;

	}

	public static int getInt(String msg, String err, boolean hasDef, int def) {

		int ans = 0;
		String strIn;
		boolean ansInvalida = false;

		do {
			if (ansInvalida) {
				c.printf("\n" + err + "\n");
			}
			strIn = ask(msg + (hasDef ? " [" + 0 + "]" : "") + ": ");

			if (!strIn.isEmpty()) {

				ansInvalida = false;
				try {
					ans = Integer.parseInt(strIn);
				} catch (Exception e) {
					ansInvalida = true;
				}
			} else if (hasDef) {
				ansInvalida = false;
				ans = 0;
			}

		} while (ansInvalida);

		return ans;

	}

	public static String getString(String msg, String err) {
		return InputHelper.getString(msg, err, false, null);

	}

	public static String getString(String msg, String err, boolean hasDef,
			String def) {
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