package poo.proyecto.simulador;

/**
 * Clase abstracta para implementar una clase que recibira notificaciones
 * en ciertas instancias de un ciclo. No es interfaz pues no todo simulador
 * tiene que implementar todos los hooks.
 */
public abstract class SimuladorHook {

    public void preIteracion() {

    }

    public void postIteracion() {

    }

    public void simulacionDetenida() {

    }

    public void simulacionArrancada() {

    }

    public void prepararSimulacion() {

    }


}
