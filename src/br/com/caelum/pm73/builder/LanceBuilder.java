package br.com.caelum.pm73.builder;

import java.util.Calendar;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Usuario;

public class LanceBuilder {

	private double valor;
	private Calendar data;
	private Usuario usuario;

	public LanceBuilder() {
		this.valor = 200.0;
		this.data = Calendar.getInstance();
		this.usuario = new Usuario("Silas", "teste@teste.com");
	}

	public LanceBuilder comValor(double valor) {
		this.valor = valor;
		return this;
	}

	public LanceBuilder comData(Calendar data) {
		this.data = data;
		return this;
	}

	public LanceBuilder comUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}


	public Lance constroi() {
		return new Lance(data, usuario, valor);
	}
}
