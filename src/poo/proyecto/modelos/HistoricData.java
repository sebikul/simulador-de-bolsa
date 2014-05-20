package poo.proyecto.modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Almacena valores historicos.
 */
public class HistoricData {


    /**
     * Largo máximo que va a tener la lista de valores almacenados.
     */
    static private final int MAX_SIZE = 80;
    /**
     * Largo que va a tener la lista de valores almacenados luego de que se limpie.
     */
    static private final int MIN_SIZE = 30;
    /**
     * Lista de valores almacenados.
     */
    private List<Ciclo> historico = new ArrayList<Ciclo>();


    /**
     * Construye un nuevo modelo de datos historicos.
     *
     * @param valor valor inicial del titulo
     */

    public HistoricData(double valor) {
        historico.add(new Ciclo(valor, 0, 0));
    }


    /**
     * Agregar nuevo valor al historico.
     * La cantidad de compras, ventas es nula porque se
     *
     * @param valorInicial
     */
    public void agregar(double valorInicial) {

        clean();

        historico.add(new Ciclo(valorInicial, 0, 0));
    }

    public void actualizar(double valorActual, int compras, int ventas) {
        int index = historico.size() - 1;
        historico.get(index).setValor(valorActual);
        historico.get(index).setCompras(compras);
        historico.get(index).setVentas(ventas);
    }

//    public Ciclo getUltimo() {
//        return historico.get(historico.size() - 1);
//    }

    private void clean() {
        if (historico.size() == MAX_SIZE) {
            List<Ciclo> aux = historico.subList(MAX_SIZE - 1 - MIN_SIZE, MAX_SIZE);
            historico = aux;
        }
    }

    public int size() {
        return historico.size();
    }

    public Ciclo get(int index) {
        return historico.get(index);
    }


}
