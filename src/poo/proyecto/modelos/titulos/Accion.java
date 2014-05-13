package poo.proyecto.modelos.titulos;


import poo.proyecto.modelos.Titulo;

public class Accion extends Titulo {
    /**
     * Construye un nuevo titulo.
     *
     * @param simbolo      Simbolo del titulo.
     * @param valorInicial Valor inicial del titulo.
     * @param volumen      Volumen en circulacion
     */
    public Accion(String simbolo, double valorInicial, int volumen) {
        super(simbolo, valorInicial, volumen);
    }


}
