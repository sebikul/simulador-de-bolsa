package poo.proyecto.modelos;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.NoHayElementosException;
import poo.proyecto.exceptions.TituloNoExisteException;
import poo.proyecto.simulador.Decididor;
import poo.proyecto.simulador.Decision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AgenteDeBolsa {

	private final String nombre;
	private HashMap<Inversor, Double> capitalClientes = new HashMap<Inversor, Double>();
	private double riesgoPersonal = 0.5;

	public AgenteDeBolsa(String nombre) {
		this.nombre = nombre;

	}

	public Map<Inversor, Double> getClientes() {
		return Collections.unmodifiableMap(capitalClientes);
	}

	public double getCapitalFrom(Inversor inversor) {

		if (capitalClientes.containsKey(inversor)) {
			return capitalClientes.get(inversor);
		}

		return 0.0;

	}

	public void agregarInversor(Inversor inversor) {

		double cap = inversor.getCapitalParaCuenta();

		capitalClientes.put(inversor, cap);

		try {
			inversor.notificarTransferenciaDeCapital(cap);
		} catch (CapitalInsuficienteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notifica al agente que debe ejecutar el equivalente a una iteracion en el
	 * contexto de _mercado_
	 * 
	 * @param mercado
	 */
	public void notificarIteracion(Mercado mercado) {

		Decididor<Titulo> decididorDeTitulos = new Decididor<Titulo>();

		for (Titulo titulo : mercado.getTitulos().values()) {
			decididorDeTitulos.addDecision(new Decision<Titulo>(titulo,
					(int) titulo.getValor() + 1));
		}

		try {
			for (Inversor inversor : capitalClientes.keySet()) {

				if (!inversor.deseaInvertir()) {
					continue;
				}

				Titulo titulo = decididorDeTitulos.getDecision().getObject();

				try {
					String simbolo = titulo.getSimbolo();

					double capital = capitalClientes.get(inversor);
					double costo = titulo.getValor();
					double riesgo = inversor.getRiesgo();

					int cantidad = (int) (capital / costo * riesgo);

					comprarTitulo(inversor, mercado, simbolo, cantidad + 1);

				} catch (TituloNoExisteException e) {
					e.printStackTrace();
				} catch (CapitalInsuficienteException e) {
					// System.out.println(inversor
					// + " no tiene capital suficiente para comprar "
					// + titulo);
				}

			}

			for (Inversor inversor : capitalClientes.keySet()) {

				if (!inversor.deseaInvertir()) {
					continue;
				}

				Titulo titulo = decididorDeTitulos.getDecision().getObject();

				if (inversor.getTitulos().containsKey(titulo.getSimbolo())) {
					try {

						double capital = capitalClientes.get(inversor);
						double costo = titulo.getValor();
						double riesgo = inversor.getRiesgo();

						int cantidad = (int) (capital / costo * riesgo);

						venderTitulo(inversor, mercado, titulo.getSimbolo(),
								cantidad + 1);
					} catch (TituloNoExisteException e) {
						e.printStackTrace();
					} catch (CapitalInsuficienteException e) {
						// System.out.println(inversor
						// + " no tiene titulos para vender " + titulo);
					}
				}

			}

		} catch (NoHayElementosException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Ejecuta la compra de _simbolo_ pedida por _inversor_
	 * <p/>
	 * <p/>
	 * Agregar el titulo a la cartera del cliente.
	 * <p/>
	 * Modificar la entrada del capital en la cuenta con el agente.
	 * 
	 * @param inversor
	 * @param mercado
	 * @param simbolo
	 * @param cantidad
	 * @throws TituloNoExisteException
	 * @throws CapitalInsuficienteException
	 */
	public void comprarTitulo(Inversor inversor, Mercado mercado,
			String simbolo, int cantidad) throws TituloNoExisteException,
			CapitalInsuficienteException {

		if (mercado == null || (simbolo == null || (simbolo.isEmpty()))
				|| cantidad < 1) {
			throw new IllegalArgumentException();
		}

		Titulo titulo = mercado.getFromSimbolo(simbolo);
		double precio = titulo.getValor() * cantidad;

		double capitalViejo = capitalClientes.get(inversor);

		if (!capitalClientes.containsKey(inversor) || capitalViejo < precio) {
			throw new CapitalInsuficienteException();
		}

		capitalClientes.put(inversor, capitalViejo - precio);

		inversor.notificarCompra(titulo, cantidad);
		mercado.notificarCompra(titulo);

		// FIXME debug
		// System.out.println(inversor.getNombre() + " ha comprado " + cantidad
		// + "*" + simbolo + " por $" + precio + "\t| Capital restante: $"
		// + capitalClientes.get(inversor));

	}

	/**
	 * Ejecuta la venta de _simbolo_ pedida por _inversor_
	 * 
	 * @param inversor
	 * @param mercado
	 * @param simbolo
	 * @param cantidad
	 * @throws CapitalInsuficienteException
	 * @throws TituloNoExisteException
	 */
	public void venderTitulo(Inversor inversor, Mercado mercado,
			String simbolo, int cantidad) throws CapitalInsuficienteException,
			TituloNoExisteException {

		if (mercado == null || (simbolo == null || (simbolo.isEmpty()))
				|| cantidad < 1) {
			throw new IllegalArgumentException();
		}

		Titulo titulo = mercado.getFromSimbolo(simbolo);
		double precio = titulo.getValor() * cantidad;

		double capitalViejo = capitalClientes.get(inversor);

		if (!capitalClientes.containsKey(inversor)
				|| cantidad > inversor.getTitulos().get(simbolo).getAmount()) {

			throw new CapitalInsuficienteException();
		}

		capitalClientes.put(inversor, capitalViejo + precio);

		inversor.notificarVenta(titulo, cantidad);
		mercado.notificarVenta(titulo);

		// FIXME debug
		// System.out.println(inversor.getNombre() + " ha vendido " + cantidad
		// + "*" + simbolo + " por $" + precio + "\t| Capital restante: $"
		// + capitalClientes.get(inversor));

	}

	public double getRiesgoPersonal() {
		return riesgoPersonal;
	}

	public void setRiesgoPersonal(double riesgoPersonal) {
		this.riesgoPersonal = riesgoPersonal;
	}

	public String printDebugInfo() {

		String ret = "Nombre:\t" + this.nombre + "\nClientes:\n\n";

		for (Inversor inversor : capitalClientes.keySet()) {

			ret += inversor.printDebugInfo() + "\n\n";

		}

		return ret;

	}

	public String getNombre() {
		return nombre;
	}
}