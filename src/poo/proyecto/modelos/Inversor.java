package poo.proyecto.modelos;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.TituloNoExisteException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa a un inversor dentro de la simulacion.
 */
public class Inversor {

    private final String nombre;

    private final HashMap<Titulo, Integer> cartera = new HashMap<Titulo, Integer>();
    private double capital;
    private double riesgo = 0.5;
    private AgenteDeBolsa agente;
    private double capitalInicial;

    /**
     * Construye a un nuevo inversor
     *
     * @param nombre  Nombre del inversor (no debe ser unico)
     * @param capital Capital inicial del inversor.
     * @param agente  Instancia del agente de bolsa que administrara su cartera.
     */
    public Inversor(String nombre, double capital, AgenteDeBolsa agente) {

        this.capital = capitalInicial = capital;
        this.nombre = nombre;
        this.agente = agente;
    }

    /**
     * Devuelve la cartera del inversor.
     *
     * @return Cartera del inversor.
     */
    public final Map<Titulo, Integer> getTitulos() {
        return Collections.unmodifiableMap(cartera);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inversor inversor = (Inversor) o;

        if (Double.compare(inversor.capitalInicial, capitalInicial) != 0) return false;
        if (!agente.equals(inversor.agente)) return false;
        if (!nombre.equals(inversor.nombre)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nombre.hashCode();
        result = 31 * result + agente.hashCode();
        temp = Double.doubleToLongBits(capitalInicial);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Devuelve el capital actual del inversor.
     *
     * @return Capital del inversor.
     */
    public final double getCapital() {
        return capital;
    }

    /**
     * Setea el capital del inversor.
     *
     * @param capital Nuevo capital del inversor.
     */
    public final void setCapital(double capital) {
        this.capital = capital;
    }

    /**
     * Devuelve el patrimonio calculado del inversor.
     * Calculado como: capital + agente.inversores[this].capital + sum[titulo.valor for titulo in titulos]
     *
     * @return Patrimonio del inversor.
     */
    public final double getPatrimonio() {
        double ret = capital + agente.getCapitalFrom(this);

        for (Map.Entry<Titulo, Integer> entrada : cartera.entrySet()) {
            ret += entrada.getValue() * entrada.getKey().getValor();
        }

        return ret;
    }

    /**
     * Devuelve el riesgo del inversor.
     *
     * @return Riesgo del inversor.
     */
    public final double getRiesgo() {
        return riesgo;
    }

    /**
     * Setea el riesgo del inversor.
     *
     * @param riesgo Riesgo del inversor. En caso de ser invalido se ignora.
     *               0 < riesgo < 1
     */
    public final void setRiesgo(double riesgo) {
        if (riesgo <= 0 || riesgo >= 1) {
            return;
        }
        this.riesgo = riesgo;
    }

    /**
     * Devuelve el nombre del inversor.
     *
     * @return Nombre del inversor.
     */
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
     * Modifica la entrada en la cartera para coincidir con la cantidad
     * adquirida.
     *
     * @param titulo   Titulo comprado.
     * @param cantidad Cantidad comprada del titulo
     */
    public final void notificarCompra(Titulo titulo, int cantidad) {

        if (cartera.containsKey(titulo)) {

            int cantidadVieja = cartera.get(titulo);

            cartera.put(titulo, cantidadVieja + cantidad);


        } else {
            cartera.put(titulo, cantidad);
        }

    }

    /**
     * Notifica al inversor de la venta de un titulo efectuada por el agente.
     * Modifica la entrada en la cartera para coincidir con la cantidad vendida.
     *
     * @param titulo   Titulo vendido.
     * @param cantidad Candidad vendida del titulo.
     * @throws TituloNoExisteException
     */
    public final void notificarVenta(Titulo titulo, int cantidad) throws TituloNoExisteException, CapitalInsuficienteException {

        if (!cartera.containsKey(titulo)) {
            throw new TituloNoExisteException();

        }

        int cantidadVieja = cartera.get(titulo);

        if (cantidad > cantidadVieja) {
            throw new CapitalInsuficienteException();
        }

        cartera.put(titulo, cantidadVieja - cantidad);


    }

    /**
     * Determina de forma aleatoria, basandose en el riesgo personal,
     * si el inversor desea invertir.
     *
     * @return True si el inversor desea realizar una compra/venta.
     */
    public boolean deseaInvertir() {
        return (Math.random() < riesgo);
    }

    /**
     * Devuelve el agente de bolsa del inversor.
     *
     * @return Agente de bolsa del inversor.
     */
    public final AgenteDeBolsa getAgente() {
        return agente;
    }

    /**
     * Notifica al inversor que su agente de bolsa realizo la
     * transferencia de capital por el monto *c*, y se lo descuenta.
     *
     * @param c Monto transferido a la cuenta en su agente de bolsa.
     * @throws CapitalInsuficienteException Si el capital del inversor no alcanza
     *                                      para cubrir la transferencia.
     */
    public final void notificarTransferenciaDeCapital(double c)
            throws CapitalInsuficienteException {

        if (c > capital) {
            throw new CapitalInsuficienteException();
        }

        capital -= c;

    }

    /**
     * Devuelve el capital que le sera transferido al agente de bolsa en caso de necesitarlo.
     * TODO: Basado en el riesgo
     *
     * @return Capital a transferir.
     */
    public double getCapitalParaCuenta() {

        return capital;

    }

    /**
     * Devuelve el capital inicial del inversor.
     *
     * @return Capital inicial del inversor.
     */
    public double getCapitalInicial() {
        return capitalInicial;
    }

}
