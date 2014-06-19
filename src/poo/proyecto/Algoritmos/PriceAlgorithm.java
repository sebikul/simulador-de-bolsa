package poo.proyecto.Algoritmos;

import java.util.List;

import poo.proyecto.modelos.Ciclo;
import poo.proyecto.modelos.HistoricData;
/**
 * Algoritmo "inteligente" para calcular la variacion de los precios.
 */
public class PriceAlgorithm implements Algorithm {

	private HistoricData historico;

	public PriceAlgorithm(HistoricData historico) {
		this.historico = historico;
	}

	/**
	 * Devuelve una cota inferior del nuevo precio.
	 **/

	private double limiteInf() {
		List<Ciclo> subHistorico = historico.getSubHistorico(30);
		double aux = subHistorico.get(0).getValor();
		for (int i = 1; i < subHistorico.size(); i++) {
			if (subHistorico.get(i).getValor() < aux)
				aux = subHistorico.get(i).getValor();
		}
		return aux * 0.99;
	}

	/**
	 * Devuelve una cota superior del nuevo precio.
	 **/

	private double limiteSup() {
		List<Ciclo> subHistorico = historico.getSubHistorico(30);
		double aux = subHistorico.get(0).getValor();
		for (int i = 1; i < subHistorico.size(); i++) {
			if (subHistorico.get(i).getValor() > aux)
				aux = subHistorico.get(i).getValor();
		}
		return aux * 1.001;
	}

	/**
	 * Devuelve la variacion del precio.
	 * 
	 * @param cantidad
	 *            : cantidad de acciones vendidas o compradas.
	 **/

	public double getDiff(int cantidad) {
		double precioAnt = historico.get(historico.size() - 1).getValor();
		double var = (0.40 * nuevoPrecDiario() + 0.25 * nuevoPrecSemanal() + 0.15 * nuevoPrecMensual())
				/ (cantidad * 100);
		var = signoVariacion() * var;
		while (Math.abs(precioAnt + var) <= limiteInf()
				|| Math.abs(precioAnt + var) >= limiteSup()) {
			// Si la variacion es nula por alguna razon, generamos una nueva de
			// forma aleatoria entre 0 y 2
			if (var == 0)
				var = signoVariacion() * Math.random() * 2;
			if (var < 0)
				var /= 20;
			else
				var /= 15;
		}
		return var;
	}

	/**
	 * Devuelve el nuevo precio.
	 * 
	 * @param cantidad
	 *            : cantidad de acciones vendidas o compradas.
	 **/

	private double getNuevoPrecio(int cantidad) {
		double precioAnt = historico.get(historico.size() - 1).getValor();
		return Math.abs(precioAnt + getDiff(cantidad));
	}

	/**
	 * Devuelve el nuevo precio.
	 * 
	 * @param cantidad
	 *            : cantidad de acciones compradas.
	 **/

	public double getNuevoPrecioCompra(int cantidad) {
		return getNuevoPrecio(cantidad);
	}

	/**
	 * Devuelve el nuevo precio.
	 * 
	 * @param cantidad
	 *            : cantidad de acciones vendidas.
	 **/

	public double getNuevoPrecioVenta(int cantidad) {
		return getNuevoPrecio(cantidad);
	}

	/**
	 * Devuelve el nuevo precio a partir de la variacion de tiempo.
	 * 
	 * @param varTemp
	 *            : variacion de tiempo.
	 **/

	private double getPrecioTemp(int varTemp) {
		return Math.abs(getVarProm(varTemp) + getResistenciaCambio(varTemp));
	}

	/**
	 * Devuelve el nuevo precio a partir de la variacion diaria.
	 **/

	private double nuevoPrecDiario() {
		return getPrecioTemp(2);
	}

	/**
	 * Devuelve el nuevo precio a partir de la variacion semanal.
	 **/

	private double nuevoPrecSemanal() {
		return getPrecioTemp(7);
	}

	/**
	 * Devuelve el nuevo precio a partir de la variacion mensual.
	 **/

	private double nuevoPrecMensual() {
		return getPrecioTemp(30);
	}

	/**
	 * Devuelve el opuesto de la derivada del flujo de acciones en funcion del
	 * tiempo. Sirve para suavizar los cambios de precio.
	 * 
	 * @param varTemp
	 *            : cantidad de dias en lo que se evalua la fluctuacion de
	 *            acciones.
	 **/

	public double getResistenciaCambio(int varTemp) {
		int indiceFinal = historico.size() - 1;
		int indiceInicial = indiceFinal - varTemp;
		if (indiceInicial < 0) {
			indiceInicial = 0;
			varTemp = indiceFinal;
		}
		double varFlujo = flujo(indiceFinal) - flujo(indiceInicial);
		double aux = Math.sqrt(Math.pow(varTemp, 2) + Math.pow(varFlujo, 2));
		if (varTemp == 0 || varFlujo == 0)
			return aux;
		return -signo(varTemp * varFlujo) * aux;
	}

	/**
	 * Devuelve el flujo de acciones en un determinado dia. Si el indice es
	 * negativo se devuelve el del primer dia, y si esta fuera de rango devuelve
	 * el flujo de la ultima fecha registrada.
	 * 
	 * @param indice
	 *            : dia del que me interesa saber el flujo.
	 **/

	private double flujo(int indice) {
		if (indice >= historico.size())
			indice = historico.size() - 1;
		if (indice < 0)
			indice = 0;
		return historico.get(indice).getCompras()
				- historico.get(indice).getVentas();
	}

	/**
	 * Devuelve el signo de la variacion del precio.
	 **/

	private int signoVariacion() {
		double varDiaria = flujo(historico.size() - 1)
				- flujo(historico.size() - 1 - 2);
		double varSemanal = flujo(historico.size() - 1)
				- flujo(historico.size() - 1 - 7);
		double varMensual = flujo(historico.size() - 1)
				- flujo(historico.size() - 1 - 30);
		if (signo(varDiaria) != signo(varSemanal)) {
			if (signo(varDiaria) == signo(varMensual))
				return 1;
			return signo(varDiaria);
		}
		int sign = -(signo(varDiaria * varSemanal * varMensual));
		while (sign == 0)
			sign = 1;
		return sign;
	}

	/**
	 * Devuelve el signo del numero que se pasa como parametro.
	 **/
	private int signo(double num) {
		if (num == 0)
			return 0;
		return (int) (num / Math.abs(num));
	}

	/**
	 * Devuelve la variacion promedio del flujo en un tiempo pasado como
	 * parametro.
	 * 
	 * @param varTemp
	 *            : variacion del tiempo.
	 **/
	private double getVarProm(int varTemp) {
		double rta = 0;
		int aux = historico.size(), count = 0;
		int ind = aux - varTemp - 1;
		if (ind < 0)
			ind = 0;
		for (; ind <= aux - 1; ind++, count++) {
			rta += historico.get(ind).getCompras();
			rta -= historico.get(ind).getVentas();
		}
		return rta / count;
	}

	@Override
	public void notificarComienzoCiclo() {
		// TODO Auto-generated method stub

	}

}