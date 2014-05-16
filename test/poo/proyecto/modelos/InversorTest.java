package poo.proyecto.modelos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import poo.proyecto.mercados.Merval;
import poo.proyecto.modelos.titulos.Accion;

public class InversorTest {

	Inversor inversor;
	AgenteDeBolsa agenteDeBolsa;
	Mercado mercado;

	@Before
	public void setUp() throws Exception {

		agenteDeBolsa = new AgenteDeBolsa("Nombre Agente");
		inversor = new Inversor("Nombre Inversor", 100.0, agenteDeBolsa, 0.5);
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

		Titulo titulo = new Accion("SIMB", 20.0, 2);
		assertEquals(2, titulo.getVolumenDisponible());
		assertEquals(2, titulo.getVolumen());
		assertEquals(0, titulo.getVolumenEnCirculacion());

		titulo.notificarComienzoCiclo();

		mercado.addTitulo(titulo);

		agenteDeBolsa.comprarTitulo(inversor, mercado, titulo, 2);

		assertEquals(60.0, agenteDeBolsa.getCapitalFrom(inversor), 10E-6);
		assertEquals(2L, (long) inversor.getTitulos().get(titulo));
		assertEquals(0, titulo.getVolumenDisponible());
		assertEquals(2, titulo.getVolumenEnCirculacion());

		agenteDeBolsa.venderTitulo(inversor, mercado, titulo, 1);

		assertEquals(80.0, agenteDeBolsa.getCapitalFrom(inversor), 1);
		assertEquals(1L, (long) inversor.getTitulos().get(titulo));
		assertEquals(1, titulo.getVolumenDisponible());
		assertEquals(1, titulo.getVolumenEnCirculacion());

	}
}