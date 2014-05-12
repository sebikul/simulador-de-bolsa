package poo.proyecto.modelos;

import poo.proyecto.simulador.Simulador;

import java.util.ArrayList;
import java.util.List;

/**
 * Almacena valores historicos.
 */
public class HistoricData {

    /**
     * Lista de valores almacenados.
     */
    private List<Double> historico = new ArrayList<Double>(
            Simulador.DEFAULT_SIM_CYCLES);

    /**
     * Notifica el comienzo de un ciclo.
     */
    public void notificarComienzoCiclo() {
    }

    /**
     * Notifica el fin de un ciclo.
     *
     * @param valor Valor de cierre del ciclo que sera agregado a la lista.
     */
    public void notificarFinCiclo(double valor) {
        synchronized (this) {
            historico.add(valor);

            if (historico.size() > 10 * Simulador.DEFAULT_SIM_CYCLES) {
                historico = historico.subList(0, historico.size()
                        - Simulador.DEFAULT_SIM_CYCLES);
            }
        }

    }


}
