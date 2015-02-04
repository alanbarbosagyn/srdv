package br.com.ac.srdv.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DespesaLancamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String empresa;
	private String filial;
	private String filialDescricao;
	private String centroCusto;
	private String usuario;
	private String nome;
	private String cargo;
	private String despesa;
	private String formaPagamento;
	private String obs;
	private double despesaValor;
	private Date data;
	private String tipo;
	private Date dataCriacao;
	private Date dataAtualizacao;

	public DespesaLancamento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getFilial() {
		return filial;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getCargo() {
		return cargo;
	}

	public String getDespesa() {
		return despesa;
	}

	public double getDespesaValor() {
		return despesaValor;
	}

	public Date getData() {
		return data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public void setDespesa(String despesa) {
		this.despesa = despesa;
	}

	public void setDespesaValor(double despesaValor) {
		this.despesaValor = despesaValor;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getFilialDescricao() {
		return filialDescricao;
	}

	public void setFilialDescricao(String filialDescricao) {
		this.filialDescricao = filialDescricao;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
