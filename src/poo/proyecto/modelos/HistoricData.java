package poo.proyecto.modelos;


import poo.proyecto.simulador.Simulador;

import java.util.ArrayList;
import java.util.List;

public class HistoricData {

    private List<Double> historico = new ArrayList<Double>(Simulador.DEFAULT_SIM_CYCLES);


    public void notificarComienzoCiclo() {
    }

    public void notificarFinCiclo(double valor) {

        historico.add(valor);

        if (historico.size() > 10 * Simulador.DEFAULT_SIM_CYCLES) {
            historico = historico.subList(0, historico.size() - Simulador.DEFAULT_SIM_CYCLES);
        }
    }

}
