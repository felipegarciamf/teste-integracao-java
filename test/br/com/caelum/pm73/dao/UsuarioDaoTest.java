package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {

	private Session session;
	private UsuarioDao usuarioDao;

	@Before
	public void antes() {
		session = Mockito.mock(Session.class);
		usuarioDao = new UsuarioDao(session);
	}

	@Test
	public void deveEncontrarPeloNomeEmailMocado() {
		Query query = Mockito.mock(Query.class);
		UsuarioDao usuarioDao = new UsuarioDao(session);
		Usuario usuario = new Usuario("Joao da Silva", "joaodasilva@teste.com");
		String sql = "from Usuario u where u.nome = :nome and u.email = :email";
		Mockito.when(session.createQuery(sql)).thenReturn(query);
		Mockito.when(query.setParameter("nome", "Joao da Silva")).thenReturn(query);
		Mockito.when(query.setParameter("email", "joaodasilva@teste.com")).thenReturn(query);
		Mockito.when(query.uniqueResult()).thenReturn(usuario);
		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Joao da Silva", "joaodasilva@teste.com");
		assertEquals(usuario.getNome(), usuarioDoBanco.getNome());
		assertEquals(usuario.getEmail(), usuarioDoBanco.getEmail());
	}

	@Test
	public void deveEncontrarPeloNomeEmailMocado2() {

		Usuario novoUsuario = new Usuario("Joao da Silva", "joaodasilva@teste.com");
		usuarioDao.salvar(novoUsuario);

		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joaodasilva@teste.com");

		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joaodasilva@teste.com", usuario.getEmail());

	}

	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {
		Usuario usuarioBanco = usuarioDao.porNomeEEmail("teste", "nao vai vir");
		assertNull(usuarioBanco);
	}

	@After
	public void depois() {
		session.close();
	}
	


}
