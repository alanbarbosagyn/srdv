package br.com.ac.srdv.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.ac.srdv.dao.CargoDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Cargo;
import br.com.ac.srdv.modelo.Empresa;
import br.com.ac.srdv.modelo.Filial;
import br.com.ac.srdv.modelo.Usuario;

public class CadastroUsuarioController {

	private List<Cargo> cargos;
	private List<Empresa> empresas;
	private List<Filial> filiais;
	private Usuario usuario;

	public CadastroUsuarioController() {
		novoUsuario();
	}

	public String novoUsuario() {

		usuario = new Usuario();

		CargoDao carDao = new CargoDao();
		EmpresaDao empDao = new EmpresaDao();
		FilialDao filDao = new FilialDao();

		try {
			cargos = carDao.getTodosCargos();
			empresas = empDao.getTodosEmpresas();
			filiais = filDao.getTodosFilials();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERRO AO ACESSAR O BANCO DE DADOS", e
											.getMessage()));
		}
		return "CadastroUsuario";
	}

	public String salvar() {
		if (tudoOK() == false)
			return "";

		usuario.setNome(usuario.getNome().toUpperCase());

		UsuarioDao useDao = new UsuarioDao();
		try {
			useDao.salvar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"ERRO AO SALVAR USUARIO", e.getMessage()));
		}

		return novoUsuario();
	}

	public boolean tudoOK() {
		boolean retorno = true;
		if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"LOGIN N�O DIGITADO", "DIGITE O LOGIN!"));
			retorno = false;
		}

		if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"NOME N�O DIGITADO", "DIGITE O NOME!"));
			retorno = false;
		}
		if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"SENHA N�O DIGITADA", "DIGITE A SENHA!"));
			retorno = false;
		}
		if (usuario.getCargo() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CARGO N�O DIGITADO", "DIGITE O CARGO!"));
			retorno = false;
		}
		if (usuario.getEmpresa() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"EMPRESA N�O DIGITADA", "DIGITE A EMPRESA!"));
			retorno = false;
		}
		if (usuario.getFilial() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"FILIAL N�O DIGITADA", "DIGITE A FILIAL!"));
			retorno = false;
		}
		if (usuario.getCentroCusto() == null
				|| usuario.getCentroCusto().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"CENTRO DE CUSTO N�O DIGITADO",
							"DIGITE O CENTRO DE CUSTO!"));
			retorno = false;
		}

		return retorno;
	}

	public String editar() {
		// novo();
		return "CadastroUsuario";
	}

	public List<Cargo> getCargos() {
		return cargos;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public List<Filial> getFiliais() {
		return filiais;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public void setFiliais(List<Filial> filiais) {
		this.filiais = filiais;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
