package br.com.ac.srdv.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.ac.srdv.controller.LoginController;

public class Sessao {

	// public static Login loginAtivo;

	public static void setLogin(LoginController login) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
				true);
		session.setAttribute("LOGIN", login);
	}

	public static LoginController getLogin() {
		// HttpServletRequest req;
		// HttpServletRequest req = (HttpServletRequest) FacesContext
		// .getCurrentInstance().getExternalContext().getRequest();
		// HttpServletRequest request = (HttpServletRequest) req;
		// HttpSession session = (HttpSession) request.getSession();
		// return (Login) session.getAttribute("LOGIN");
		return (LoginController) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("LOGIN");
	}
}
