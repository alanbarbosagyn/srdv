package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.modelo.Agenda;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class AgendaDao extends GenericDao {
	
	public AgendaDao() {
		this.createTable = "CREATE TABLE RDV_Agenda ( "
				+ "id integer primary key, usuario varchar(200), "
				+ " data date, local varchar(500), acaoPrevista varchar(500),acaoRealizada varchar(500) )"; 
	}

	public void salvar(Agenda agenda) throws Exception {

		String data = new SimpleDateFormat("dd-MM-yyyy").format(agenda
				.getData());

		if (agenda.getId() == 0) {
			int codigo = 0;
			Resultado resultado = con
					.consultar("SELECT MAX(id) FROM RDV_Agenda");
			if (resultado.next())
				codigo = resultado.getInt(1);
			codigo++;
			resultado.close();

			con.executar("INSERT INTO RDV_Agenda (id,usuario,data,local,acaoPrevista,acaoRealizada) VALUES ("
					+ codigo
					+ ",'"
					+ agenda.getUsuario()
					+ "','"
					+ data
					+ "','"
					+ agenda.getLocal()
					+ "','"
					+ agenda.getAcaoPrevista()
					+ "','"
					+ agenda.getAcaoRealizada() + "')");
		} else {
			con.executar("UPDATE RDV_Agenda SET local = '" + agenda.getLocal()
					+ "', data = '" + data + "', acaoPrevista = '"
					+ agenda.getAcaoPrevista() + "', acaoRealizada = '"
					+ agenda.getAcaoRealizada() + "' where id = "
					+ agenda.getId());
		}
	}

	public void excluir(int id) throws Exception {
		super.excluir("DELETE FROM RDV_Agenda WHERE id = " + id);
	}

	public List<Agenda> getAgendas(String usuario, Date dataInicial,
			Date dataFinal) throws Exception {
		String dataI = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);
		String dataF = new SimpleDateFormat("dd-MM-yyyy").format(dataFinal);

		Resultado result = con
				.consultar("select * from RDV_Agenda WHERE usuario = '"
						+ usuario + "' and data >= '" + dataI
						+ "' and data <= '" + dataF + "' order by data ");

		List<Agenda> lista = new ArrayList<Agenda>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;
	}

	public List<Agenda> getTodosAgendas() throws Exception {
		Resultado result = con
				.consultar("select * from RDV_Agenda order by agenda");

		List<Agenda> lista = new ArrayList<Agenda>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private Agenda parse(Resultado result) throws Exception, SQLException {
		Agenda agenda = new Agenda();
		agenda.setId(result.getInt("id"));
		agenda.setUsuario(result.getString("usuario"));
		agenda.setData(result.getDate("data"));
		agenda.setLocal(result.getString("local"));
		agenda.setAcaoPrevista(result.getString("acaoPrevista"));
		agenda.setAcaoRealizada(result.getString("acaoRealizada"));
		return agenda;
	}
}
