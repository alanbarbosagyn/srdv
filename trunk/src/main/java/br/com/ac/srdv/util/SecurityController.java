package br.com.ac.srdv.util;

import java.util.logging.Logger;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
//import javax.servlet.http.HttpSession;

import br.com.ac.srdv.controller.LoginController;

public class SecurityController implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(SecurityController.class.getName());

	public static final String JSF_SECURITY_KEY = "__jsfsecurity__";
	public static final String VIEWID_KEY = "__viewId";

	// private static String[] protectedPaths = null;

	@Override
	public void beforePhase(PhaseEvent event) {

		FacesContext facesContext = event.getFacesContext();
		String currentPage = facesContext.getViewRoot().getViewId();
		System.out.println("PÃ�GINA: " + currentPage);
		boolean isLoginPage = currentPage.equals("/Login.xhtml");
		// HttpSession session = (HttpSession) facesContext.getExternalContext()
		// .getSession(true);
		// Login currentUser = (Login) session.getAttribute("login");

		FacesContext fc = event.getFacesContext();
		LoginController usuarioSessao = (LoginController) fc.getExternalContext().getSessionMap()
				.get("login");

		boolean temUsuario = true;
		if (usuarioSessao == null) {
			temUsuario = false;
			System.out.println("NULL");
		} else if (usuarioSessao.isLogado() == false) {
			temUsuario = false;
			System.out.println("NOT LOGIN");
		}

		if (!isLoginPage && !temUsuario) {
			System.out.println("PAGINA DE LOGIN");
			NavigationHandler nh = facesContext.getApplication()
					.getNavigationHandler();
			nh.handleNavigation(facesContext, null, "Login");

		}
		if (temUsuario)
			if (usuarioSessao.getUsuario().isAdmin() == false) {
				if (currentPage.startsWith("/CadastroUsuario.xhtml")
						|| currentPage.startsWith("/ConsultaUsuario.xhtml")) {

					NavigationHandler nh = facesContext.getApplication()
							.getNavigationHandler();
					nh.handleNavigation(facesContext, null, "Menu");
				}
			}
	}

	@Override
	public void afterPhase(PhaseEvent event) {

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	// public static Login getUsuarioSessao() {
	// FacesContext facesContext = FacesContext.getCurrentInstance();
	// HttpSession session = (HttpSession) facesContext.getExternalContext()
	// .getSession(true);
	// Login currentUser = (Login) session.getAttribute("login");
	//
	// if (currentUser == null)
	// return new Login();
	//
	// return currentUser;
	// }
	//
	// private String[] getProtectedPaths(FacesContext ctx) {
	//
	// if (protectedPaths == null) {
	// logger.config("loading jsfsecurity.protected ...");
	// String s = ctx.getExternalContext().getInitParameter(
	// "jsfsecurity.protected");
	// if (s != null) {
	// protectedPaths = s.split(",");
	// }
	// }
	// return protectedPaths;
	// }
}