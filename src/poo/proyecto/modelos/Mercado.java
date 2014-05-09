package poo.proyecto.modelos;

import poo.proyecto.exceptions.TituloNoExisteException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Mercado {

    private final HashMap<String, Titulo> titulos;

    public Mercado() {
        titulos = new HashMap<String, Titulo>();

    }

    public abstract void cargar();

    @Override
    public String toString() {
        return titulos.toString();
    }

    public final Map<String, Titulo> getTitulos() {
        return Collections.unmodifiableMap(titulos);
    }


    public final Titulo getFromSimbolo(String simbolo)
            throws TituloNoExisteException {
        if (!titulos.containsKey(simbolo)) {
            throw new TituloNoExisteException(simbolo);
        }

        return titulos.get(simbolo);
    }

    public final void addTitulo(Titulo t) {
        titulos.put(t.getSimbolo(), t);

    }

    public final void notificarCompra(Titulo titulo) {

        titulo.notificarCompra();

    }

    public final void notificarVenta(Titulo titulo) {

        titulo.notificarVenta();


    }

}
