package poo.proyecto.modelos;

import poo.proyecto.exceptions.TituloNoExisteException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Mercado {

    private final HashMap<String, Titulo> titulos;

    public Mercado() {
        titulos = new HashMap<String, Titulo>();

    }

    @Override
    public String toString() {
        return  titulos.toString();
    }

    public final Map<String,Titulo> getTitulos() {
        return Collections.unmodifiableMap(titulos);
    }

    public final boolean exists(String simbolo) {
        return titulos.containsKey(simbolo);
    }

    public final Titulo getFromSimbolo(String simbolo) throws TituloNoExisteException {
        if (!titulos.containsKey(simbolo)) {
            throw new TituloNoExisteException(simbolo);
        }

        return titulos.get(simbolo);
    }

    public final void addTitulo(Titulo t) {
        titulos.put(t.getSimbolo(), t);

    }

}
