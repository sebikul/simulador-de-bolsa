package poo.proyecto.modelos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AgenteDeBolsaTest {

	AgenteDeBolsa agenteDeBolsa;

	@Before
	public void setUp() throws Exception {

		agenteDeBolsa = new AgenteDeBolsa("Nombre Agente");

	}

	@Test
	public void testGetCapitalFrom() throws Exception {

		setUp();

		Inversor inversor = new Inversor("Nombre Inversor", 300.0,
				agenteDeBolsa, 0.5);

		agenteDeBolsa.agregarInversor(inversor);

		assertEquals(300.0, agenteDeBolsa.getCapitalFrom(inversor), 10E-6);
	}

	@Test
	public void testAgregarInversor() throws Exception {

		setUp();

		Inversor inversor = new Inversor("Nombre Inversor", 300.0,
				agenteDeBolsa, 0.5);

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