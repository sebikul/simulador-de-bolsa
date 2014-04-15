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

    public Inversor(String nombre, double capital) {

        this.capital = capital;
        this.nombre = nombre;
        this.cartera = new HashMap<String, EntradaCartera>();
    }

    public final Map<String,EntradaCartera> getTitulos() {
        return Collections.unmodifiableMap(cartera);
    }

    public void comprarTitulo(Mercado mercado, String simbolo, int cantidad) throws TituloNoExisteException, CapitalInsuficienteException {


        if (mercado == null || (simbolo == null || (simbolo.isEmpty())) || cantidad < 1) {
            throw new IllegalArgumentException();
        }

        Titulo titulo = mercado.getFromSimbolo(simbolo);
        double precio = titulo.getValor() * cantidad;


        if (capital < precio) {
            throw new CapitalInsuficienteException();
        }

        capital -= precio;

        EntradaCartera entrada = cartera.get(simbolo);

        if (entrada == null) {
            cartera.put(simbolo, new EntradaCartera(cantidad, titulo));
        } else {
            entrada.setAmount(entrada.getAmount() + cantidad);
        }

        //FIXME debug
        System.out.println(nombre + " ha comprado " + cantidad + "*" + simbolo + " por $" + precio + "\t| Capital restante: $" + capital);


    }

    public void venderTitulo(Mercado mercado, String simbolo, int cantidad) throws CapitalInsuficienteException, TituloNoExisteException {

        if (mercado == null || (simbolo == null || (simbolo.isEmpty())) || cantidad < 1) {
            throw new IllegalArgumentException();
        }

        Titulo titulo = mercado.getFromSimbolo(simbolo);
        double precio = titulo.getValor() * cantidad;

        EntradaCartera entrada = cartera.get(simbolo);

        if (cantidad <= entrada.getAmount()) {
            entrada.setAmount(entrada.getAmount() - cantidad);
        } else {
            throw new CapitalInsuficienteException();
        }

        if (entrada.getAmount() == 0) {
            cartera.remove(simbolo);
        }

        capital += precio;

        //FIXME debug
        System.out.println(nombre + " ha vendido " + cantidad + "*" + simbolo + " por $" + precio + "\t| Capital restante: $" + capital);



    }

    public final double getCapital() {
        return capital;
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
        return "Nombre:\t" + this.nombre + "\n" +
                "Capital:\t$" + this.capital + "\n" +
                "Titulos:\t" + cartera.toString();
    }

    @Override
    public final String toString() {
        return this.nombre;
    }
}
