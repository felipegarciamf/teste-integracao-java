package br.com.caelum.pm73.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoBuilder {

	private Usuario dono;
	private double valor;
	private String nome;
	private boolean usado;
	private Calendar dataAbertura;
	private boolean encerrado;
	private List<Lance> lance;


	public LeilaoBuilder() {
		this.dono = new Usuario("Joao da Silva", "joao@silva.com.br");
		this.valor = 1500.0;
		this.nome = "XBox";
		this.usado = false;
		this.dataAbertura = Calendar.getInstance();
		this.encerrado = false;
		this.lance = new ArrayList<Lance>();
	}

	public LeilaoBuilder comDono(Usuario dono) {
		this.dono = dono;
		return this;
	}

	public LeilaoBuilder comValor(double valor) {
		this.valor = valor;
		return this;
	}

	public LeilaoBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}

	public LeilaoBuilder encerrado(boolean encerrado) {
		this.encerrado = encerrado;
		return this;
	}

	public LeilaoBuilder usado(boolean usado) {
		this.usado = usado;
		return this;
	}

	public LeilaoBuilder diasAtras(int dias) {
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DAY_OF_MONTH, -dias);
		this.dataAbertura = data;
		return this;
	}
	
	public LeilaoBuilder comLance(Lance lance) {
		this.lance.add(lance);
		return this;
	}
	


	public Leilao constroi() {
		Leilao leilao = new Leilao(this.nome, this.valor, this.dono, this.usado);
		for (Lance lances : lance) {
			leilao.adicionaLance(lances);
		}
		leilao.setDataAbertura(dataAbertura);
		if (encerrado)
			leilao.encerra();
		return leilao;
	}

}
