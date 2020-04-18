package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.builder.LanceBuilder;
import br.com.caelum.pm73.builder.LeilaoBuilder;
import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {

	private UsuarioDao usuarioDao;
	private Session session;
	private LeilaoDao leilaoDao;
	private LeilaoBuilder leilaoBuilder;
	private LanceBuilder lanceBuilder;

	@Before
	public void antes() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		session.beginTransaction();
		leilaoBuilder = new LeilaoBuilder();
		lanceBuilder = new LanceBuilder();
	}

	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void deveContarLeiloesNaoEncerrados() {

		Usuario usuario = new Usuario("Mauricio", "teste");

		Leilao ativo = leilaoBuilder.comDono(usuario).comNome("geladeira").comValor(100.0).encerrado(false).constroi();

		Leilao encerrado = leilaoBuilder.comDono(usuario).comNome("XBOOX").comValor(100.0).encerrado(false).constroi();

		encerrado.encerra();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);

		Long total = leilaoDao.total();

		assertEquals(1L, total, 0.000000);
	}

	@Test
	public void deveRetornarLeiloesNaoEncerradosNulo() {
		Usuario usuario = new Usuario("Mauricio", "teste");

		Leilao encerrado = leilaoBuilder.comDono(usuario).comNome("geladeira").comValor(100.0).encerrado(false)
				.constroi();
		Leilao encerrado2 = leilaoBuilder.comDono(usuario).comNome("XBOOX").comValor(100.0).encerrado(false).constroi();

		encerrado.encerra();
		encerrado2.encerra();
		usuarioDao.salvar(usuario);
		leilaoDao.salvar(encerrado);
		leilaoDao.salvar(encerrado2);

		Long total = leilaoDao.total();

		assertEquals(0L, total, 0.00000);
	}

	@Test
	public void deveRetornarUmLeilaoComUsado() {
		Usuario usuario = new Usuario("DOIDO", "Teste");

		Leilao usado = leilaoBuilder.comDono(usuario).comNome("gela").comValor(100.0).encerrado(true).usado(true)
				.constroi();

		Leilao naoUsado = leilaoBuilder.comDono(usuario).comNome("gela").comValor(100.0).encerrado(false).usado(false)
				.constroi();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(usado);
		leilaoDao.salvar(naoUsado);

		List<Leilao> novos = leilaoDao.novos();

		assertEquals(1, novos.size());
		assertEquals("gela", novos.get(0).getNome());

	}

	public void deveRetornarLeiloesDeUmaSemanaAtras() {

		Usuario usuario = new Usuario("teste", "teste");

		Leilao leilao = leilaoBuilder.comDono(usuario).comNome("seila").comValor(100.0).usado(false).diasAtras(10)
				.constroi();

		Leilao leilao2 = leilaoBuilder.comDono(usuario).comNome("seila").comValor(100.0).usado(false).diasAtras(0)
				.constroi();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);

		List<Leilao> antigos = leilaoDao.antigos();
		assertEquals(1L, antigos.size());
		assertEquals("seila", antigos.get(0).getNome());
	}

	public void deveRetornarLeiloesDeSeteDiasAtras() {

		Usuario usuario = new Usuario("teste", "teste");

		Leilao leilao = leilaoBuilder.comDono(usuario).comNome("seila").comValor(100.0).usado(false).diasAtras(7)
				.constroi();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(leilao);

		List<Leilao> antigos = leilaoDao.antigos();

		assertEquals(1, antigos.size());
		assertEquals("teste", antigos.get(0).getNome());

	}

	@Test
	public void deveTrazerLeiloesNaoEncerradosNoPeriodo() {

		Calendar comecoDoIntervalo = Calendar.getInstance();
		comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);

		Calendar fimDoIntervalo = Calendar.getInstance();

		Usuario usuario = new Usuario("teste", "teste");

		Leilao leilao = new LeilaoBuilder().comDono(usuario).comNome("seila").comValor(100.0).usado(false).diasAtras(13)
				.constroi();

		Leilao leilao2 = new LeilaoBuilder().comDono(usuario).comNome("geladeira").comValor(100.0).usado(false)
				.diasAtras(2).constroi();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);

		List<Leilao> porPeriodo = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);

		assertEquals(1, porPeriodo.size());
		assertEquals("geladeira", porPeriodo.get(0).getNome());

	}

	@Test
	public void deveTrazerLeiloesComDataDentroDoIntervaloNulo() {
		Calendar comecoDoIntervalo = Calendar.getInstance();
		comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);
		Calendar fimDoIntervalo = Calendar.getInstance();

		Usuario usuario = new Usuario("Teste", "teste");

		Leilao leilao1 = new LeilaoBuilder().comDono(usuario).comNome("vasosura").comValor(100.0).usado(false)
				.diasAtras(2).encerrado(true).constroi();

		Leilao leilao2 = new LeilaoBuilder().comDono(usuario).comNome("carro").comValor(100.0).usado(false).diasAtras(3)
				.encerrado(true).constroi();

		usuarioDao.salvar(usuario);
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);

		List<Leilao> porPeriodo = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);
		assertEquals(0, porPeriodo.size());
	}

	@Test
	public void deveRetornarUmaListaDe2Leiloes() {

		Usuario dono = new Usuario("dono do negocio", "dono de tudo");
		Usuario usuario = new Usuario("Teste22", "teste22");

		Leilao leilao1 = new LeilaoBuilder().comDono(dono).comNome("vasosura").comValor(200.0)
				.comLance(new LanceBuilder().comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comValor(400.0).comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comValor(400.0).comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi()).encerrado(false).constroi();

		Leilao leilao2 = new LeilaoBuilder().comDono(dono).comNome("teste").comValor(200.0)
				.comLance(new LanceBuilder().comValor(400.0).comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comValor(400.0).comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi())
				.comLance(new LanceBuilder().comUsuario(usuario).constroi()).encerrado(false).constroi();
		usuarioDao.salvar(dono);
		usuarioDao.salvar(usuario);

		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);

		List<Leilao> disputadosEntre = leilaoDao.disputadosEntre(100, 1000);
		assertEquals(2, disputadosEntre.size());
		assertEquals(200, disputadosEntre.get(0).getLances().get(0).getValor(), 0.0000);

	}

	@Test
	public void listaSomenteOsLeiloesDoUsuario() throws Exception {
		Usuario dono = new Usuario("Mauricio", "m@a.com");
		Usuario comprador = new Usuario("Victor", "v@v.com");
		Usuario comprador2 = new Usuario("Guilherme", "g@g.com");
		Leilao leilao = new LeilaoBuilder().comDono(dono).comValor(50.0)
				.comLance(new LanceBuilder().comUsuario(comprador).constroi())
				.comLance(new LanceBuilder().comUsuario(comprador2).constroi()).constroi();
		Leilao leilao2 = new LeilaoBuilder().comDono(dono).comValor(250.0)
				.comLance(new LanceBuilder().comUsuario(comprador2).constroi()).constroi();
		usuarioDao.salvar(dono);
		usuarioDao.salvar(comprador);
		usuarioDao.salvar(comprador2);
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);

		List<Leilao> leiloes = leilaoDao.listaLeiloesDoUsuario(comprador);
		assertEquals(1, leiloes.size());
		assertEquals(leilao, leiloes.get(0));
	}

	@Test
	public void listaDeLeiloesDeUmUsuarioNaoTemRepeticao() throws Exception {
		Usuario dono = new Usuario("Mauricio", "m@a.com");
		Usuario comprador = new Usuario("Victor", "v@v.com");
		Leilao leilao = new LeilaoBuilder().comDono(dono)
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(100.0).constroi())
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(200.0).constroi()).constroi();
		usuarioDao.salvar(dono);
		usuarioDao.salvar(comprador);
		leilaoDao.salvar(leilao);

		List<Leilao> leiloes = leilaoDao.listaLeiloesDoUsuario(comprador);
		assertEquals(1, leiloes.size());
		assertEquals(leilao, leiloes.get(0));
	}

	@Test
	public void deveRetornarUmValorInicialMedioDeLance() {

		Usuario dono = new Usuario("Mauricio", "m@a.com");
		Usuario comprador = new Usuario("Victor Das Cobras", "v@vs.scom");

		Leilao leilao = new LeilaoBuilder().comDono(dono).comValor(1600)
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(100).constroi())
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(100).constroi()).constroi();
		Leilao leilao2 = new LeilaoBuilder().comDono(dono).comValor(1900)
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(100).constroi())
				.comLance(new LanceBuilder().comUsuario(comprador).comValor(100).constroi()).constroi();

		usuarioDao.salvar(dono);
		usuarioDao.salvar(comprador);
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);

		double valorInicialMedioDoUsuario = leilaoDao.getValorInicialMedioDoUsuario(comprador);

		System.out.println(valorInicialMedioDoUsuario);
		assertEquals(1750, valorInicialMedioDoUsuario, 0.001);

	}

}
