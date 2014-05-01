package poo.proyecto.modelos;

import java.util.Random;

public abstract class Titulo {

	private final String simbolo;
	private final double valorInicial;
	private double valor;

	public Titulo(String simbolo, double valorInicial) {
		this.simbolo = simbolo;
		this.valor = this.valorInicial = valorInicial;
	}

	public final double getValor() {
		return valor;
	}

	public final void setValor(double valor) {
		this.valor = valor;
	}

	public String getSimbolo() {
		return simbolo;
	}

	@Override
	public String toString() {
		return this.simbolo + " (" + this.valor + ")";
	}

	public String printDebugInfo() {
		return "$" + valorInicial + " --> $" + valor;
	}

	public abstract String getName();

	public void notificarCompra() {

		Random rdm = new Random();

		Double diff = valor * rdm.nextDouble() / 9E6;

		System.out.println("Valor de " + simbolo + ": " + valor + " --> "
				+ (valor + diff));

		valor = valor + diff;
	}

	public void notificarVenta() {

		Random rdm = new Random();

		Double diff = valor * rdm.nextDouble() / 10E6;

		System.out.println("Valor de " + simbolo + ": " + valor + " --> "
				+ (valor - diff));

		valor = valor - diff;
	}
}
