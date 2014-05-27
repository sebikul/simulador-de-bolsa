package poo.proyecto.modelos;

import poo.proyecto.Algoritmos.Algorithm;
import poo.proyecto.exceptions.TituloNoExisteException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa un conjunto de titulos que tienen una base de operacion similar.
 */
public abstract class Mercado {

    /**
     * Almacena los titulos que seran operados dentro del mercado.
     */
    private final HashMap<String, Titulo> titulos = new HashMap<String, Titulo>();

    private final Class<? extends Algorithm> algorithmClass;

    /**
     * Construye una nueva instancia de un mercado.
     *
     * @param algorithmClass
     */
    public Mercado(Class<? extends Algorithm> algorithmClass) {
        this.algorithmClass = algorithmClass;

    }

    /**
     * Carga los titulos que seran operados.
     */
    public abstract void cargar();

    @Override
    public String toString() {
        return titulos.toString();
    }

    /**
     * Devuelve los titulos que operan en este mercado.
     *
     * @return Mapa con los titulos que operan en el mercado.
     */
    public final Map<String, Titulo> getTitulos() {
        return Collections.unmodifiableMap(titulos);
    }

    /**
     * Devuelve un sibmolo basado en su nombre.
     *
     * @param simbolo Simbolo del titulo.
     * @return Instancia del titulo.
     * @throws TituloNoExisteException El titulo no existe en el mercado.
     */
    public final Titulo getFromSimbolo(String simbolo)
            throws TituloNoExisteException {
        if (!titulos.containsKey(simbolo)) {
            throw new TituloNoExisteException(simbolo);
        }

        return titulos.get(simbolo);
    }

    /**
     * Agrega un nuevo titulo al mercado.
     *
     * @param titulo Instancia del titulo que se desea agregar.
     */
    public final void addTitulo(Titulo titulo) {

        try {

            Algorithm algoritmo = (Algorithm) algorithmClass.getConstructors()[0].newInstance(titulo.getHistorico());

            titulo.setAlgoritmo(algoritmo);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        titulos.put(titulo.getSimbolo(), titulo);

    }
}