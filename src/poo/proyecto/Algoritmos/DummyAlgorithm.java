package poo.proyecto.Algoritmos;

import poo.proyecto.modelos.HistoricData;
import poo.proyecto.modelos.Titulo;

import java.util.Random;

/**
 * Created by sekul on 26/05/14.
 */
public class DummyAlgorithm implements Algorithm {

    /**
     * Instancia de Random utilizado para calcular la variacion del valor en
     * cada compra o venta del titulo.
     */
    private transient Random rdm = null;

    private HistoricData historico;

    public DummyAlgorithm(HistoricData historico) {
        this.historico = historico;
    }

    public double getDiferencial() {

        double valor = historico.get(historico.size() - 1).getValor();

        return valor * rdm.nextGaussian() / (Titulo.PRICE_NORMALIZER * 10);

    }

    @Override
    public double getNuevoPrecioCompra(int cantidad) {
        double valor = historico.get(historico.size() - 1).getValor();

        double diff = valor * rdm.nextGaussian() / (Titulo.PRICE_NORMALIZER);

        return valor + diff;
    }

    @Override
    public double getNuevoPrecioVenta(int cantidad) {

        double valor = historico.get(historico.size() - 1).getValor();

        double diff = valor * rdm.nextGaussian() / (Titulo.PRICE_NORMALIZER * 10);

        return valor - diff;
    }

    @Override
    public void notificarComienzoCiclo() {

        rdm = new Random();

    }
}
