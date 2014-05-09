package poo.proyecto.modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poo.proyecto.simulador.Simulador;

public class HistoricData implements Cloneable {

    private List<Double> historico = new ArrayList<Double>(
            Simulador.DEFAULT_SIM_CYCLES);

    public void notificarComienzoCiclo() {
    }

    public void notificarFinCiclo(double valor) {
        synchronized (this) {
            historico.add(valor);

            if (historico.size() > 10 * Simulador.DEFAULT_SIM_CYCLES) {
                historico = historico.subList(0, historico.size()
                        - Simulador.DEFAULT_SIM_CYCLES);
            }
        }


    }

    public int getSize() {
        return historico.size();
    }

    public double get(int iteracion) {
        synchronized (this) {
            return historico.get(iteracion);
        }
    }

    public List<Double> get() {
        return Collections.unmodifiableList(historico);
    }

}
