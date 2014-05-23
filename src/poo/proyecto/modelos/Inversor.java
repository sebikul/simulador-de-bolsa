package poo.proyecto.modelos;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.TituloNoExisteException;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa a un inversor dentro de la simulacion.
 */
public class Inversor implements Serializable, HistoricStore {

    /**
     * Almacena el nombre del inversor.
     */
    private final String nombre;

    /**
     * Almacena un mapa entra un titulo que posee el inversor y la cantidad que
     * posee.
     */
    private final HashMap<Titulo, Integer> cartera = new HashMap<Titulo, Integer>();

    /**
     * Almacena una instancia de consulta de los valores historicos
     * patrimoniales del inversor.
     */
    private final HistoricData historico;
    /**
     * Almacena el riesgo personal del inversor.
     */
    private final double riesgo;
    /**
     * Almacena el capital del inversor.
     */
    private double capital;
    /**
     * Almacena el agente de bolsa del inversor.
     */
    private AgenteDeBolsa agente;

    /**
     * Almacena el capital inicial del inversor.
     */
    private double capitalInicial;

    /**
     * Construye a un nuevo inversor
     *
     * @param nombre  Nombre del inversor (no debe ser unico)
     * @param capital Capital inicial del inversor.
     * @param agente  Instancia del agente de bolsa que administrara su cartera.
     */
    public Inversor(String nombre, double capital, AgenteDeBolsa agente,
                    double riesgo) throws Exception {

        if (riesgo <= 0 || riesgo >= 1) {
            throw new Exception();
        }

        this.capital = capitalInicial = capital;
        this.nombre = nombre;
        this.riesgo = riesgo;
        this.agente = agente;

        historico = new HistoricData(capitalInicial);
    }

    /**
     * Devuelve la cartera del inversor.
     *
     * @return Cartera del inversor.
     */
    public final HashMap<Titulo, Integer> getTitulos() {
        return cartera;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        Inversor inversor = (Inversor) o;
//
//        if (Double.compare(inversor.capitalInicial, capitalInicial) != 0)
//            return false;
//        if (!agente.equals(inversor.agente))
//            return false;
//        if (!nombre.equals(inversor.nombre))
//            return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        result = nombre.hashCode();
//        result = 31 * result + agente.hashCode();
//        temp = Double.doubleToLongBits(capitalInicial);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }

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
     * Devuelve el patrimonio calculado del inversor. Calculado como: capital +
     * agente.inversores[this].capital + sum[titulo.valor for titulo in titulos]
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
        return this.nombre + " (" + getPatrimonio() + ")";
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
    public final void notificarVenta(Titulo titulo, int cantidad)
            throws TituloNoExisteException, CapitalInsuficienteException {

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
     * Determina de forma aleatoria, basandose en el riesgo personal, si el
     * inversor desea invertir.
     * historico
     *
     * @return True si el inversor desea realizar una compra/venta.
     */
    private boolean deseaInvertir(Titulo titulo) {
        return (Math.random() < riesgo);
    }

    /**
     * Decide entre los titulos disponibles en el mercado cuales desea comprar.
     *
     * @param mercado Mercado en el cual se esta operando.
     * @return Mapa de titulos que se desean comprar con su cantidad.
     */
    public Map<Titulo, Integer> getOrdenesDeCompra(Mercado mercado) {

        HashMap<Titulo, Integer> elecciones = new HashMap<Titulo, Integer>();

        for (Titulo titulo : mercado.getTitulos().values()) {
            if (deseaInvertir(titulo)) {

                int cantidad = ((int) (capital / titulo.getValor() * riesgo)) + 1;

                assert cantidad >= 1;

                elecciones.put(titulo, cantidad);
            }
        }

        return Collections.unmodifiableMap(elecciones);
    }

    /**
     * Decide entre los titulos disponibles en su cartera cuales desea vender.
     *
     * @return Mapa de titulos que se desean vender con su cantidad.
     */
    public Map<Titulo, Integer> getOrdenesDeVenta() {

        HashMap<Titulo, Integer> elecciones = new HashMap<Titulo, Integer>();

        for (Map.Entry<Titulo, Integer> entrada : cartera.entrySet()) {

            if (entrada.getValue() > 0 && deseaInvertir(entrada.getKey())) {

                Titulo titulo = entrada.getKey();

                int cantidad = ((int) (capital / titulo.getValor() * riesgo)) + 1;

                int cuantoTengo = cartera.get(titulo);

                if (cantidad > cuantoTengo) {
                    cantidad = cuantoTengo;
                }

                assert cantidad >= 1;

                elecciones.put(titulo, cantidad);
            }
        }

        return Collections.unmodifiableMap(elecciones);
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
     * Notifica al inversor que su agente de bolsa realizo la transferencia de
     * capital por el monto *c*, y se lo descuenta.
     *
     * @param c Monto transferido a la cuenta en su agente de bolsa.
     * @throws CapitalInsuficienteException Si el capital del inversor no alcanza para cubrir la
     *                                      transferencia.
     */
    public final void notificarTransferenciaDeCapital(double c)
            throws CapitalInsuficienteException {

        if (c > capital) {
            throw new CapitalInsuficienteException();
        }

        capital -= c;

    }

    /**
     * Devuelve el capital que le sera transferido al agente de bolsa en caso de
     * necesitarlo.
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

    /**
     * Notifica al inversor que finalizo el ciclo.
     */
    public final void notificarFinCiclo() {

        historico.agregar(getPatrimonio());

    }

    /**
     * Devuelve un objeto de consulta de valores historicos.
     *
     * @return Instancia de HistoricData que almacena los valores historicos del
     * inversor.
     */
    public final HistoricData getHistorico() {
        return historico;
    }

}