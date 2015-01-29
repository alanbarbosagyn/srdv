package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import br.com.ac.srdv.dao.AgendaDao;
import br.com.ac.srdv.dao.TipoDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Agenda;
import br.com.ac.srdv.modelo.Tipo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class ControleAgendaController {

	private List<Usuario> usuarios;
	private Usuario usuario;
	private List<Tipo> tipos;
	private int tipoId;
	private List<Agenda> listaAgenda;
	private Agenda agenda;

	public ControleAgendaController() {
		novo();
	}

	public String salvar() {

		if (agenda.getData() == null)
			return "";

		try {
			new AgendaDao().salvar(agenda);
			Mensagem.Info("RDV SALVO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO SALVAR RDV!", e.getMessage());
		}

		novo();

		return "";
	}

	public String novo() {

		usuario = Sessao.getLogin().getUsuario();
		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -370);
			Date dIni = c.getTime();

			tipos = new TipoDao().getTipos(dIni);
			tipoId = new TipoDao().getTipo(new Date()).getId();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
			usuario = Sessao.getLogin().getUsuario();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIO!", e.getMessage());
		}

		agenda = new Agenda();
		consultar();
		return "ControleAgenda";
	}

	public String consultar() {

		Tipo t = null;
		try {
			t = new TipoDao().getTipo(tipoId);
		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CONSULTAR TIPOS", e1.getMessage());
			e1.printStackTrace();
			return "";
		}

		Date dataInicial, dataFinal;
		dataInicial = t.getDataInicial();
		dataFinal = t.getDataFinal();

		try {
			List<Agenda> ageAux = new AgendaDao().getAgendas(
					usuario.getLogin(), dataInicial, dataFinal);

			Calendar dia = Calendar.getInstance();
			dia.setTime(dataInicial);

			listaAgenda = new ArrayList<Agenda>();

			while (dia.getTimeInMillis() <= dataFinal.getTime()) {
				boolean encontrado = false;
				for (Agenda age : ageAux) {
					if (age.getData().equals(dia.getTime())) {
						listaAgenda.add(age);
						encontrado = true;
						break;
					}
				}
				if (encontrado == false) {
					Agenda age = new Agenda();
					age.setData(dia.getTime());
					age.setUsuario(usuario.getLogin());
					listaAgenda.add(age);
				}

				dia.add(Calendar.DATE, 1);
			}

		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CONSULTAR AGENDA", e.getMessage());
		}
		return "";
	}

	public void onRowEdit(RowEditEvent event) {

		// SALVA NO BANCO DE DADOS O REGISTRO ALTERADO
		Agenda a = (Agenda) event.getObject();
		a.setUsuario(usuario.getLogin());
		try {
			new AgendaDao().salvar(a);
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO SALVAR ALTERA��O NA AGENDA!", e.getMessage());
			e.printStackTrace();
			return;
		}

		Mensagem.Info("AGENDA ALTERADA!", "");

	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled",
				((Agenda) event.getObject()).getData() + "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String excluir() {
		if (agenda.getId() == 0)
			return "";

		try {
			new TipoDao().excluir(agenda.getId());
			Mensagem.Info("AGENDA EXCLU�DO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO EXCLUIR RDV!", e.getMessage());
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

	public List<Tipo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Tipo> tipos) {
		this.tipos = tipos;
	}

	public int getTipoId() {
		return tipoId;
	}

	public void setTipoId(int tipoId) {
		this.tipoId = tipoId;
	}

	public List<Agenda> getListaAgenda() {
		return listaAgenda;
	}

	public void setListaAgenda(List<Agenda> listaAgenda) {
		this.listaAgenda = listaAgenda;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

}
