package poo.proyecto.modelos;

import org.junit.Before;
import org.junit.Test;
import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.titulos.Accion;

import static org.junit.Assert.assertEquals;

public class InversorTest {

    Inversor inversor;
    AgenteDeBolsa agenteDeBolsa;
    Mercado mercado;

    @Before
    public void setUp() throws Exception {

        agenteDeBolsa = new AgenteDeBolsa("Nombre Agente");
        inversor = new Inversor("Nombre Inversor", 100.0, agenteDeBolsa);
        mercado = new Merval();

    }

    @Test
    public void testCapitalYAsignacionDeAgente() throws Exception {

        setUp();

        assertEquals(100.0, inversor.getCapital(), 10E-6);

        agenteDeBolsa.agregarInversor(inversor);

        assertEquals(0.0, inversor.getCapital(), 10E-6);

    }


    @Test
    public void testCompraVentaDeTitulos() throws Exception {

        setUp();

        agenteDeBolsa.agregarInversor(inversor);

        Titulo titulo = new Accion("SIMB", 20.0);

        titulo.notificarComienzoCiclo();

        mercado.addTitulo(titulo);

        agenteDeBolsa.comprarTitulo(inversor, mercado, "SIMB", 2);

        assertEquals(60.0, agenteDeBolsa.getCapitalFrom(inversor), 10E-6);
        assertEquals(2L, (long) inversor.getTitulos().get(titulo));

        agenteDeBolsa.venderTitulo(inversor, mercado, "SIMB", 1);

        assertEquals(80.0, agenteDeBolsa.getCapitalFrom(inversor), 1);
        assertEquals(1L, (long) inversor.getTitulos().get(titulo));

    }
}