package br.com.ac.srdv.controller;

import java.util.ArrayList;
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

public class ConsultaUsuarioController {
	private List<Usuario> usuarios;
	private Usuario usuario;

	private List<Empresa> listaEmpresa;
	private List<Filial> listaFilial;
	private List<Cargo> listaCargo;

	private boolean ativos;

	public ConsultaUsuarioController() {
		ativos = true;
		usuarios = new ArrayList<Usuario>();
	}

	public String consultar() {

		UsuarioDao useDao = new UsuarioDao();
		try {

			if (ativos == true)
				usuarios = useDao.getTodosUsuariosAtivos();
			else
				usuarios = useDao.getTodosUsuariosAtivosInativos();

			listaEmpresa = new EmpresaDao().getTodosEmpresas();
			listaFilial = new FilialDao().getTodosFilials();
			listaCargo = new CargoDao().getTodosCargos();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERRO AO ACESSAR O BANCO DE DADOS", e
											.getMessage()));
		}

		return "ConsultaUsuario";
	}

	public String excluir() {
		UsuarioDao useDao = new UsuarioDao();
		try {
			useDao.excluir(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"ERRO AO EXCLUIR USUARIO", e.getMessage()));
		}
		return "";
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Empresa> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<Empresa> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public List<Filial> getListaFilial() {
		return listaFilial;
	}

	public void setListaFilial(List<Filial> listaFilial) {
		this.listaFilial = listaFilial;
	}

	public List<Cargo> getListaCargo() {
		return listaCargo;
	}

	public void setListaCargo(List<Cargo> listaCargo) {
		this.listaCargo = listaCargo;
	}

	public boolean isAtivos() {
		return ativos;
	}

	public void setAtivos(boolean ativos) {
		this.ativos = ativos;
	}

}