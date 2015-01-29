package br.com.ac.srdv.controller;

import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class LoginController {

	private boolean logado;
	private Usuario usuario;

	public LoginController() {
		usuario = new Usuario();
		Sessao.setLogin(this);
	}

	public String logar() {
		UsuarioDao useDao = new UsuarioDao();
		Usuario aux = null;
		try {
			aux = useDao.getUsuario(usuario.getLogin(), usuario.getSenha());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIO;", e.getMessage());
			return "";
		}

		if (aux != null) {
			if (aux.isAtivo() == false) {
				Mensagem.Erro("SEU CADASTRO EST� DESATIVADO!",
						"ENTRE EM CONTATO COM A ADMINISTRA��O");
				return "Login";
			}
			logado = true;
			usuario = aux;
			return "Menu";
		} else {
			Mensagem.Erro("USU�RIO OU SENHA INCORRETO!", "");
			return "Login";
		}

	}

	public String deslogar() {
		logado = false;
		usuario = new Usuario();

		return "Login";

	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
