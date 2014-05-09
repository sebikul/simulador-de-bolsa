package poo.proyecto.modelos;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.TituloNoExisteException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Inversor {

    private final String nombre;
    private final HashMap<String, EntradaCartera> cartera;
    private double capital;
    private double riesgo = 0.5;
    private AgenteDeBolsa agente;
    private double capitalInicial;

    public Inversor(String nombre, double capital, AgenteDeBolsa agente) {

        this.capital = capitalInicial = capital;
        this.nombre = nombre;
        this.cartera = new HashMap<String, EntradaCartera>();
        this.agente = agente;
    }

    public final Map<String, EntradaCartera> getTitulos() {
        return Collections.unmodifiableMap(cartera);
    }

    public final double getCapital() {
        return capital;
    }

    public final void setCapital(double capital) {
        this.capital = capital;
    }

    public final double getPatrimonio() {
        double ret = capital + agente.getCapitalFrom(this);

        for (EntradaCartera entrada : cartera.values()) {
            ret += entrada.getAmount() * entrada.getTitulo().getValor();
        }

        return ret;
    }

    public final double getRiesgo() {
        return riesgo;
    }

    public final void setRiesgo(double riesgo) {
        if (riesgo <= 0 || riesgo >= 1) {
            return;
        }
        this.riesgo = riesgo;
    }

    public final String getNombre() {
        return nombre;
    }

    public String printDebugInfo() {
        return "Nombre:\t" + this.nombre + "\n" + "Capital:\t$"
                + agente.getCapitalFrom(this) + "\n" + "Titulos:\t"
                + cartera.toString();
    }

    @Override
    public final String toString() {
        return this.nombre;
    }

    /**
     * Notifica al inversor de la compra de un titulo efectuada por el agente.
     * <p/>
     * Modifica la entrada en la cartera para coincidir con la cantidad
     * adquirida.
     *
     * @param titulo
     * @param cantidad
     */
    public final void notificarCompra(Titulo titulo, int cantidad) {

        if (cartera.containsKey(titulo.getSimbolo())) {

            EntradaCartera entrada = cartera.get(titulo.getSimbolo());

            entrada.setAmount(entrada.getAmount() + cantidad);
        } else {
            cartera.put(titulo.getSimbolo(), new EntradaCartera(cantidad,
                    titulo));
        }

    }

    /**
     * Notifica al inversor de la venta de un titulo efectuada por el agente.
     * <p/>
     * Modifica la entrada en la cartera para coincidir con la cantidad vendida.
     *
     * @param titulo
     * @param cantidad
     * @throws TituloNoExisteException
     */
    public final void notificarVenta(Titulo titulo, int cantidad)
            throws TituloNoExisteException {

        if (cartera.containsKey(titulo.getSimbolo())) {

            EntradaCartera entrada = cartera.get(titulo.getSimbolo());

            entrada.setAmount(entrada.getAmount() - cantidad);
        } else {
            throw new TituloNoExisteException();
        }

    }

    public boolean deseaInvertir() {
        return (Math.random() < riesgo);
    }

    public final AgenteDeBolsa getAgente() {
        return agente;
    }

    public final void notificarTransferenciaDeCapital(double c)
            throws CapitalInsuficienteException {

        if (c > capital) {
            throw new CapitalInsuficienteException();
        }

        capital -= c;

    }

    /**
     * TODO: Basado en el riesgo
     *
     * @return
     */
    public double getCapitalParaCuenta() {

        return capital;

    }

    public double getCapitalInicial() {
        return capitalInicial;
    }

}
