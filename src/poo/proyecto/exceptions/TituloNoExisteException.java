package poo.proyecto.exceptions;

public class TituloNoExisteException extends Exception {

	private final String simbolo;

	public TituloNoExisteException(String s) {
		simbolo = s;
	}

	public TituloNoExisteException() {
		simbolo = "";
	}

	public String getSimbolo() {
		return simbolo;
	}
}
