package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {
	

	private UsuarioDao usuarioDao;
	private Session session;
	private LeilaoDao leilaoDao;

	
	@Before
	public void antes() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		session.beginTransaction();
	}
	
	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void deveContarLeiloesNaoEncerrados() {
		Usuario usuario = new Usuario("Mauricio", "teste");
		Leilao ativo = new Leilao("geladeira", 1500.0, usuario, false);
		Leilao encerrado = new Leilao("XBOOX", 700.0, usuario, false);
		
		encerrado.encerra();
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		
		Long total = leilaoDao.total();
		
		assertEquals(1L, total, 0.000000);
	}

}
