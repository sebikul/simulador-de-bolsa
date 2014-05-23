package poo.proyecto.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ResultadosSimulacion implements Serializable {

    private final Collection<Titulo> titulos;
    private final ArrayList<Inversor> inversores;
    private final int ciclos;

    public ResultadosSimulacion(ArrayList<Titulo> titulos, ArrayList<Inversor> inversores, int ciclos) {
        this.titulos = titulos;
        this.inversores = inversores;
        this.ciclos = ciclos;
    }

    public int getCiclos() {
        return ciclos;
    }

    public Collection<Titulo> getTitulos() {
        return titulos;
    }


    public ArrayList<Inversor> getInversores() {
        return inversores;
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
}
