package poo.proyecto.modelos;

import poo.proyecto.exceptions.CapitalInsuficienteException;
import poo.proyecto.exceptions.TituloNoExisteException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa a una agente de bolsa dentro de la simulacion.
 */
public class AgenteDeBolsa implements Serializable {

    /**
     * Almacena el nombre del agente de bolsa.
     */
    private final String nombre;
    /**
     * Representa el riesgo personal que induce a las decisiones de los
     * inversores que representa.
     */
    private final double riesgoPersonal = 0.5;
    /**
     * Almacena un mapa que vincula a un inversor con su capital deltro de la
     * cueta que posee con el agente de bolsa.
     */
    private final HashMap<Inversor, Double> capitalClientes = new HashMap<Inversor, Double>();

    /**
     * Construye un agente de bolsa.
     *
     * @param nombre Nombre del agente de bolsa
     */
    public AgenteDeBolsa(String nombre) {
        this.nombre = nombre;

    }

    /**
     * Devuelve un mapa entre los inversores y su capital en la cuenta que
     * poseen con el inversor.
     *
     * @return Mapa entre los inversores y el capital que poseen en la cuenta
     * con el inversor.
     */
    public final HashMap<Inversor, Double> getClientes() {
        return capitalClientes;
    }

    /**
     * Devuelve el capital de un inversor.
     *
     * @param inversor Inversor del cual se quiere obtener el capital
     * @return Capital del inversor.
     */
    public double getCapitalFrom(Inversor inversor) {

        if (capitalClientes.containsKey(inversor)) {
            return capitalClientes.get(inversor);
        }

        return 0.0;

    }

    /**
     * Agrega un inversor para que el agente de bolsa administre su cartera.
     *
     * @param inversor Inversor a agregar,
     */
    public final void agregarInversor(Inversor inversor) {

        double cap = inversor.getCapitalParaCuenta();

        capitalClientes.put(inversor, cap);

        try {
            inversor.notificarTransferenciaDeCapital(cap);
        } catch (CapitalInsuficienteException e) {
            e.printStackTrace();
        }
    }


    /**
     * Notifica al agente de bolsa que comenzo una nueva iteracion.
     *
     * @param mercado Mercado sobre el cual se esta operando.
     */
    public void notificarIteracion(Mercado mercado) {

        for (Inversor inversor : capitalClientes.keySet()) {

            // if(inversor.getPatrimonio())

            Map<Titulo, Integer> elecciones = inversor
                    .getOrdenesDeCompra(mercado);

            for (Map.Entry<Titulo, Integer> entrada : elecciones.entrySet()) {

                try {

                    Titulo titulo = entrada.getKey();

                    int cantidad = entrada.getValue();

                    if (cantidad > titulo.getVolumenDisponible()) {
                        cantidad = titulo.getVolumenDisponible();

                        if (cantidad == 0) {
                            continue;
                        }
                    }

                    comprarTitulo(inversor, mercado, titulo, cantidad);

                } catch (CapitalInsuficienteException e) {

                }

            }

        }

        for (Inversor inversor : capitalClientes.keySet()) {

            Map<Titulo, Integer> elecciones = inversor.getOrdenesDeVenta();

            for (Map.Entry<Titulo, Integer> entrada : elecciones.entrySet()) {

                try {
                    venderTitulo(inversor, mercado, entrada.getKey(),
                            entrada.getValue());
                } catch (TituloNoExisteException e) {
                    e.printStackTrace();
                } catch (CapitalInsuficienteException e) {

                }

            }

        }

    }

    /**
     * Ejecuta la compra de un titulo.
     *
     * @param inversor Inversor ejecutando la compra.
     * @param mercado  Mercado sobre el cual se esta operando.
     * @param titulo   Titulo que se esta comprando.
     * @param cantidad Cantidad que se desea comprar del titulo.
     * @throws TituloNoExisteException      Si el titulo no existe.
     * @throws CapitalInsuficienteException Si el inversor no cuenta con el capital para realizar la
     *                                      compra
     */
    private void comprarTitulo(Inversor inversor, Mercado mercado,
                               Titulo titulo, int cantidad) throws CapitalInsuficienteException {

        if (mercado == null || titulo == null || cantidad < 1) {
            throw new IllegalArgumentException();
        }

        double precio = titulo.getValor() * cantidad;

        double capitalViejo = capitalClientes.get(inversor);

        if (!capitalClientes.containsKey(inversor) || capitalViejo < precio) {
            throw new CapitalInsuficienteException();
        }

        capitalClientes.put(inversor, capitalViejo - precio);

        inversor.notificarCompra(titulo, cantidad);
        titulo.notificarCompra(cantidad);

    }

    /**
     * Ejecuta la venta de un titulo.
     *
     * @param inversor Inversor ejecutando la compra.
     * @param mercado  Mercado sobre el cual se esta operando.
     * @param titulo   Titulo que se esta comprando.
     * @param cantidad Cantidad que se desea comprar del titulo.
     * @throws TituloNoExisteException      Si el titulo no existe.
     * @throws CapitalInsuficienteException Si el inversor no cuenta con el capital para realizar la
     *                                      compra
     */
    private void venderTitulo(Inversor inversor, Mercado mercado, Titulo titulo,
                              int cantidad) throws CapitalInsuficienteException,
            TituloNoExisteException {

        if (mercado == null || titulo == null || cantidad < 1) {
            throw new IllegalArgumentException();
        }

        if (!capitalClientes.containsKey(inversor)
                || cantidad > inversor.getTitulos().get(titulo)) {

            throw new CapitalInsuficienteException();
        }

        double precio = titulo.getValor() * cantidad;

        double capitalViejo = capitalClientes.get(inversor);

        capitalClientes.put(inversor, capitalViejo + precio);

        inversor.notificarVenta(titulo, cantidad);
        titulo.notificarVenta(cantidad);

    }

    public double getRiesgoPersonal() {
        return riesgoPersonal;
    }

    /**
     * Notifica al agente que finalizo el ciclo.
     */
    public final void notificarFinCiclo() {

        for (Inversor inversor : capitalClientes.keySet()) {
            inversor.notificarFinCiclo();
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AgenteDeBolsa that = (AgenteDeBolsa) o;

        return nombre.equals(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

}