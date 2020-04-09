package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	@Test
	public void deveRetornarLeiloesNaoEncerradosNulo() {
		Usuario usuario = new Usuario("Mauricio", "teste");
		Leilao encerrado = new Leilao("geladeira", 1500.0, usuario, false);
		Leilao encerrado2 = new Leilao("XBOOX", 700.0, usuario, false);
		encerrado.encerra();
		encerrado2.encerra();
		
		leilaoDao.salvar(encerrado);
		leilaoDao.salvar(encerrado2);
		
		Long total = leilaoDao.total();
		
		assertEquals(0L, total, 0.00000);
	}
	
	
	@Test
	public void deveRetornarUmLeilaoComUsado() {
		Usuario usuario = new Usuario("DOIDO", "Teste");
		Leilao usado = new Leilao("gela",110.0,usuario,true);
		Leilao naoUsado = new Leilao("gela",110.0,usuario,false);
		
		leilaoDao.salvar(usado);
		leilaoDao.salvar(naoUsado);
		
		List<Leilao> novos = leilaoDao.novos();

		assertEquals(1, novos.size());
		assertEquals("gela", novos.get(0).getNome());
		
	}
	
	
	public void deveRetornarLeiloesDeUmaSemanaAtras() {
		
		Usuario usuario = new Usuario("teste", "teste");
		Leilao leilao = new Leilao("seila", 100.0, usuario, false);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -10);
		leilao.setDataAbertura(c);
		
		Leilao leilao2 = new Leilao("seila", 100.0, usuario, false);
		leilao2.setDataAbertura(Calendar.getInstance());
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);
		
		List<Leilao> antigos = leilaoDao.antigos();
		assertEquals(1L, antigos.size());
		assertEquals("seila", antigos.get(0).getNome());
	}
	
	public void deveRetornarLeiloesDeSeteDiasAtras() {
		
		Usuario usuario = new Usuario("teste", "teste");
		Leilao leilao = new Leilao("seila", 100.0, usuario, false);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -7);
		leilao.setDataAbertura(c);
		

		leilaoDao.salvar(leilao);

		
		List<Leilao> antigos = leilaoDao.antigos();
		
		assertEquals(1, antigos.size());
		assertEquals("teste", antigos.get(0).getNome());
		
	}

}
