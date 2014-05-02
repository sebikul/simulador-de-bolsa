package poo.proyecto.mercados;

import poo.proyecto.modelos.titulos.Accion;
import poo.proyecto.modelos.Mercado;

public final class Merval extends Mercado {
    @Override
    public void Initialize() {

        addTitulo(new Accion("YPFD", 40));
        addTitulo(new Accion("GGAL", 100));
        addTitulo(new Accion("TS", 100));
        addTitulo(new Accion("PAMP", 100));

    }
}
