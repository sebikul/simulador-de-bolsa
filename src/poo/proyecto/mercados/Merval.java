package poo.proyecto.mercados;

import poo.proyecto.Algoritmos.Algorithm;
import poo.proyecto.modelos.Mercado;
import poo.proyecto.modelos.titulos.Accion;

public final class Merval extends Mercado {

    public Merval(Class<? extends Algorithm> algorithmClass) {
        super(algorithmClass);
    }

    @Override
    public void cargar() {

        addTitulo(new Accion("YPFD", 40, 2000));
        addTitulo(new Accion("GGAL", 100, 2000));
        addTitulo(new Accion("TS", 100, 2000));
        addTitulo(new Accion("PAMP", 100, 2000));

    }
}
