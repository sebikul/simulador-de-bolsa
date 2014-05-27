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

    /**
     * Retorna una lista con los datos en bruto.
     *
     * @return Lista con todos los datos almacenados.
     */
    public List<Ciclo> getRawData() {
        return Collections.unmodifiableList(historico);
    }

    /**
     * Informa si la lista de datos historicos esta vacia.
     *
     * @return true si la lista de datos esta vacia.
     * false en caso contrario.
     */
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

    /**
     * Actualiza los datos del ultimo ciclo almacenado a los valores finales.
     *
     * @param valorActual Valor final del objeto almacenado
     * @param compras     Valor final de las compras en el ultimo ciclo
     * @param ventas      Valor final de las ventas en el ultimo ciclo
     */
    public void actualizar(double valorActual, int compras, int ventas) {
        int index = historico.size() - 1;
        historico.get(index).setValor(valorActual);
        historico.get(index).setCompras(compras);
        historico.get(index).setVentas(ventas);
    }


    /**
     * Retorna la cantidad de elementos almacenados en la lista.
     *
     * @return Cantidad de elementos almacenados en la lista.
     */
    public int size() {
        return historico.size();
    }

    /**
     * Retorna los datos de un ciclo en una posicion determinada
     *
     * @param index Ciclo sobre el cual se quieren saber los valores finales.
     * @return Instancia de Ciclo que almacena los valores finales.
     */
    public Ciclo get(int index) {
        return historico.get(index);
    }

    /**
     * Retorna una sub lista con los ultimos n valores historicos almacenados.
     *
     * @param varTemp Cantidad de elementos a retornar
     * @return Sublista con varTemp elementos.
     */
    public List<Ciclo> getSubHistorico(int varTemp) {
        int start = 0;
        if (historico.size() > varTemp)
            start = historico.size() - varTemp;
        return historico.subList(start, historico.size());

    }

}
