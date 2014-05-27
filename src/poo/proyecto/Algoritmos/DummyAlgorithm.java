package poo.proyecto.Algoritmos;

import poo.proyecto.modelos.HistoricData;
import poo.proyecto.modelos.Titulo;

import java.util.Random;

/**
 * Algoritmo "tonto" para calcular la variacion de los precios.
 */
public class DummyAlgorithm implements Algorithm {

    /**
     * Instancia de Random utilizado para calcular la variacion del valor en
     * cada compra o venta del titulo.
     */
    private transient Random rdm = null;

    /**
     * Almacen de los datos historicos del objeto sobre el cual se le va a
     * calcular una variacion.
     */
    private HistoricData historico;

    public DummyAlgorithm(HistoricData historico) {
        this.historico = historico;
    }

    @Override
    public double getNuevoPrecioCompra(int cantidad) {
        double valor = historico.get(historico.size() - 1).getValor();

        double diff = valor * rdm.nextGaussian() / (Titulo.PRICE_NORMALIZER);

        //System.out.println("" + valor + " --> " + (valor + diff * cantidad));

        return valor + diff * cantidad;
    }

    @Override
    public double getNuevoPrecioVenta(int cantidad) {

        double valor = historico.get(historico.size() - 1).getValor();

        double diff = valor * rdm.nextGaussian() / (Titulo.PRICE_NORMALIZER);

        //System.out.println("" + valor + " --> " + (valor - diff * cantidad));


        return valor - diff * cantidad;
    }

    @Override
    public void notificarComienzoCiclo() {

        rdm = new Random();

    }
}
