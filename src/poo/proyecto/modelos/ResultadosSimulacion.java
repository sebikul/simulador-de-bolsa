package poo.proyecto.modelos;

import java.util.ArrayList;
import java.util.Collection;

public class ResultadosSimulacion {

    private Collection<Titulo> titulos = null;
    private ArrayList<Inversor> inversores = new ArrayList<Inversor>();

    public void setInversores(ArrayList<AgenteDeBolsa> agentes) {
        for (AgenteDeBolsa agente : agentes) {
            for (Inversor inversor : agente.getClientes().keySet()) {
                inversores.add(inversor);
            }
        }

    }

    @Override
    public String toString() {

        String ret = "\nInversores:\n";

        for (Inversor inversor : inversores) {
            ret += inversor.getNombre() + " [" + inversor.getCapitalInicial()
                    + " --> " + inversor.getPatrimonio() + "]\n";
        }

        ret += "\nTitulos:\n";

        for (Titulo titulo : titulos) {
            ret += titulo.getSimbolo() + " [" + titulo.getValorInicial()
                    + " --> " + titulo.getValor() + "]\n";
        }

        return ret;
    }

    public void setTitulos(Collection<Titulo> values) {
        this.titulos = values;

    }
}
