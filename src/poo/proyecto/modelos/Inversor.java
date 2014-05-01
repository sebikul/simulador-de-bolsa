package poo.proyecto.modelos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import poo.proyecto.exceptions.TituloNoExisteException;

public class Inversor {

	private final String nombre;
	private final HashMap<String, EntradaCartera> cartera;
	private double capital;
	private double riesgo = 0.5;
	private AgenteDeBolsa agente;

	public Inversor(String nombre, double capital, AgenteDeBolsa agente) {

		this.capital = capital;
		this.nombre = nombre;
		this.cartera = new HashMap<String, EntradaCartera>();
		this.agente = agente;
	}

	public final Map<String, EntradaCartera> getTitulos() {
		return Collections.unmodifiableMap(cartera);
	}

	public final double getCapital() {
		return capital;
	}

	public final double getRiesgo() {
		return riesgo;
	}

	public final void setRiesgo(double riesgo) {
		if (riesgo <= 0 || riesgo >= 1) {
			return;
		}
		this.riesgo = riesgo;
	}

	public final String getNombre() {
		return nombre;
	}

	public String printDebugInfo() {
		return "Nombre:\t" + this.nombre + "\n" + "Capital:\t$"
				+ agente.getCapitalFrom(this) + "\n" + "Titulos:\t"
				+ cartera.toString();
	}

	@Override
	public final String toString() {
		return this.nombre;
	}

	/**
	 * Notifica al inversor de la compra de un titulo efectuada por el agente.
	 * 
	 * Modifica la entrada en la cartera para coincidir con la cantidad
	 * adquirida.
	 * 
	 * @param titulo
	 * @param cantidad
	 */
	public void notificarCompra(Titulo titulo, int cantidad) {

		if (cartera.containsKey(titulo.getSimbolo())) {

			EntradaCartera entrada = cartera.get(titulo.getSimbolo());

			entrada.setAmount(entrada.getAmount() + cantidad);
		} else {
			cartera.put(titulo.getSimbolo(), new EntradaCartera(cantidad,
					titulo));
		}

	}

	/**
	 * Notifica al inversor de la venta de un titulo efectuada por el agente.
	 * 
	 * Modifica la entrada en la cartera para coincidir con la cantidad vendida.
	 * 
	 * @param titulo
	 * @param cantidad
	 * @throws TituloNoExisteException
	 */
	public void notificarVenta(Titulo titulo, int cantidad)
			throws TituloNoExisteException {

		if (cartera.containsKey(titulo.getSimbolo())) {

			EntradaCartera entrada = cartera.get(titulo.getSimbolo());

			entrada.setAmount(entrada.getAmount() - cantidad);
		} else {
			throw new TituloNoExisteException();
		}

	}

	public AgenteDeBolsa getAgente() {
		return agente;
	}

	/**
	 * Transfiere la totalidad del capital al agente de bolsa para que opere a
	 * su nombre.
	 * 
	 * TODO: Basado en el riesgo
	 * 
	 * @return
	 */
	public Double notificarAsignacionDeAgente() {

		double cap = capital;

		this.capital = 0;

		return cap;

	}
}
