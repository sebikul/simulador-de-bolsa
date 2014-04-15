package poo.proyecto.exceptions;


public class TituloNoExisteException extends Exception {


    private final String simbolo;

    public TituloNoExisteException(String s) {
        simbolo = s;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
