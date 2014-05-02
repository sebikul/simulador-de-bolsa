package poo.proyecto.modelos;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AgenteDeBolsaTest {

    AgenteDeBolsa agenteDeBolsa;


    @Before
    public void setUp() throws Exception {

        agenteDeBolsa = new AgenteDeBolsa("Nombre Agente");

    }

    @Test
    public void testGetCapitalFrom() throws Exception {

        setUp();

        Inversor inversor = new Inversor("Nombre Inversor", 300.0, agenteDeBolsa);

        agenteDeBolsa.agregarInversor(inversor);

        assertEquals(300.0, agenteDeBolsa.getCapitalFrom(inversor), 10E-6);
    }

    @Test
    public void testAgregarInversor() throws Exception {

        setUp();

        Inversor inversor = new Inversor("Nombre Inversor", 300.0, agenteDeBolsa);

        agenteDeBolsa.agregarInversor(inversor);

        assertTrue(agenteDeBolsa.getClientes().keySet().contains(inversor));

    }

    @Test
    public void testNotificarIteracion() throws Exception {

    }

    @Test
    public void testComprarTitulo() throws Exception {

    }

    @Test
    public void testVenderTitulo() throws Exception {

    }
}