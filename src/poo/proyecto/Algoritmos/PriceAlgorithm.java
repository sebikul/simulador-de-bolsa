package poo.proyecto.Algoritmos;


import poo.proyecto.modelos.Ciclo;
import poo.proyecto.modelos.HistoricData;

import java.util.List;

public class PriceAlgorithm implements Algorithm {

    private HistoricData historico;

    public PriceAlgorithm(HistoricData historico) {
        this.historico = historico;
    }

    private double limiteInf() {
        List<Ciclo> subHistorico = historico.getSubHistorico(30);
        double aux = subHistorico.get(0).getValor();
        for (int i = 1; i < subHistorico.size(); i++) {
            if (subHistorico.get(i).getValor() < aux)
                aux = subHistorico.get(i).getValor();
        }
        return aux * 0.99;
    }

    public double getDiferencial() {
        double precioAnt = historico.get(historico.size() - 1).getValor();
        double var = (0.35 * nuevoPrecDiario() + 0.25 * nuevoPrecSemanal() + 0.4 * nuevoPrecMensual()) / 300;
        var = signoVariacion() * var;
        //while(Math.abs(precioAnt+var)>=1.1*precioAnt || Math.abs(precioAnt+var)<=0.9*precioAnt || Math.abs(var+precioAnt)<39 ) {
        //	var=var/20;
        //}
        while (Math.abs(var + precioAnt) <= limiteInf() || Math.abs(var + precioAnt) < historico.get(0).getValor() * 3 / 4)
            if (var < 0)
                var /= 20;
            else
                var /= 2;
        return Math.abs(var);
    }

    private double getPrecioTemp(int varTemp) {
        return Math.abs(getVarProm(varTemp) + getResistenciaCambio(varTemp));
    }

    private double nuevoPrecDiario() {
        return getPrecioTemp(2);
    }

    private double nuevoPrecSemanal() {
        return getPrecioTemp(7);
    }

    private double nuevoPrecMensual() {
        return getPrecioTemp(30);
    }

    //SUAVIZAR PENDIENTES
    public double getResistenciaCambio(int varTemp) {
        int indiceFinal = historico.size() - 1;
        int indiceInicial = indiceFinal - varTemp;
        if (indiceInicial < 0) {
            indiceInicial = 0;
            varTemp = indiceFinal;
        }
        double varFlujo = flujo(indiceFinal) - flujo(indiceInicial);
        double aux = Math.sqrt(Math.pow(varTemp, 2) + Math.pow(varFlujo, 2));
        if (varTemp == 0 || varFlujo == 0)
            return aux;
        return -signo(varTemp * varFlujo) * aux * 0.8;
    }

    private double flujo(int indice) {
        if (indice >= historico.size())
            indice = historico.size() - 1;
        if (indice < 0)
            indice = 0;
        return historico.get(indice).getCompras() - historico.get(indice).getVentas();
    }

    //DECIDO SI CRECE O DECRECE
    private int signoVariacion() {
        double varDiaria = flujo(historico.size() - 1) - flujo(historico.size() - 1 - 2);
        double varSemanal = flujo(historico.size() - 1) - flujo(historico.size() - 1 - 7);
        double varMensual = flujo(historico.size() - 1) - flujo(historico.size() - 1 - 30);
        if (signo(varDiaria) != signo(varSemanal)) {
            if (signo(varDiaria) == signo(varMensual))
                return 1;
            return signo(varDiaria);
        }
        int sign = -(signo(varDiaria * varSemanal * varMensual));
        while (sign == 0)
            sign = signo(Math.random() * 2 - 0.5);
        return sign;
    }


    //FUNCION SIGNO
    private int signo(double num) {
        if (num == 0)
            return 0;
        return (int) (num / Math.abs(num));
    }

    private double getVarProm(int varTemp) {
        double rta = 0;
        int aux = historico.size(), count = 0;
        int ind = aux - varTemp - 1;
        if (ind < 0)
            ind = 0;
        for (; ind <= aux - 1; ind++, count++) {
            rta += historico.get(ind).getCompras();
            rta -= historico.get(ind).getVentas();
        }
        return rta / count;
    }


    @Override
    public double getNuevoPrecioCompra(int cantidad) {

        double precioAnt = historico.get(historico.size() - 1).getValor();

        return precioAnt + getDiferencial();
    }

    @Override
    public double getNuevoPrecioVenta(int cantidad) {

        double precioAnt = historico.get(historico.size() - 1).getValor();

        return precioAnt - getDiferencial();
    }

    @Override
    public void notificarComienzoCiclo() {

    }
}
