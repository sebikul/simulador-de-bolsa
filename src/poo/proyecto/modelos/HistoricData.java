package poo.proyecto.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Almacena valores historicos.
 */
public class HistoricData implements Serializable {

    /**
     * Lista de valores almacenados.
     */
    private final ArrayList<Ciclo> historico = new ArrayList<Ciclo>();


    /**
     * Construye un nuevo modelo de datos historicos.
     *
     * @param valor valor inicial del titulo
     */

    public HistoricData(double valor) {
        historico.add(new Ciclo(valor));
    }

    public List<Ciclo> getRawData() {
        return Collections.unmodifiableList(historico);
    }

    public boolean isEmpty() {
        return historico.isEmpty();
    }

    /**
     * Agregar nuevo valor al historico.
     * La cantidad de compras, ventas es nula porque se
     *
     * @param valorInicial
     */
    public void agregar(double valorInicial) {

        historico.add(new Ciclo(valorInicial));
    }

    public void actualizar(double valorActual, int compras, int ventas) {
        int index = historico.size() - 1;
        historico.get(index).setValor(valorActual);
        historico.get(index).setCompras(compras);
        historico.get(index).setVentas(ventas);
    }


    public int size() {
        return historico.size();
    }

    public Ciclo get(int index) {
        return historico.get(index);
    }

    public List<Ciclo> getSubHistorico(int varTemp) {
        int i = 0;
        if (historico.size() > varTemp)
            i = historico.size() - varTemp;
        return historico.subList(i, historico.size());

    }

}
