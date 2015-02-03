package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class CicloDao {

	Conexao con;

	public CicloDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		String createTable = "CREATE TABLE RDV_Tipo ( "
				+ "id integer primary key, tipo varchar(100), "
				+ " dataInicial date, dataFinal date )";

		con = Conexao.getConexaoAtual();

		try {
			con.executar(createTable);
		} catch (Exception e) {

		}

		try {
			con.executar("ALTER TABLE RDV_Tipo ADD liberado integer");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void salvar(Ciclo tipo) throws Exception {

		int liberado = tipo.isLiberado() ? 1 : 0;

		String dataI = new SimpleDateFormat("dd-MM-yyyy").format(tipo
				.getDataInicial());
		String dataF = new SimpleDateFormat("dd-MM-yyyy").format(tipo
				.getDataFinal());

		if (tipo.getId() == 0) {
			int codigo = 0;
			Resultado resultado = con.consultar("SELECT MAX(id) FROM RDV_Tipo");
			if (resultado.next())
				codigo = resultado.getInt(1);
			codigo++;
			resultado.close();

			con.executar("INSERT INTO RDV_Tipo (id,tipo,dataInicial,dataFinal,liberado) VALUES ("
					+ codigo
					+ ",'"
					+ tipo.getTipo()
					+ "','"
					+ dataI
					+ "','"
					+ dataF + "'," + liberado + ")");
		} else {
			con.executar("UPDATE RDV_Tipo SET tipo = '" + tipo.getTipo()
					+ "', dataInicial = '" + dataI + "', dataFinal = '" + dataF
					+ "', liberado = " + liberado + " WHERE id = "
					+ tipo.getId());
		}
	}

	public void excluir(int id) throws Exception {
		if (id == 0)
			return;

		con.executar("DELETE FROM RDV_Tipo WHERE id = " + id);

	}

	public List<Ciclo> getTipos(Date dataInicial, Date dataFinal)
			throws Exception {
		String dataI = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);
		String dataF = new SimpleDateFormat("dd-MM-yyyy").format(dataFinal);

		Resultado result = con
				.consultar("select * from RDV_Tipo WHERE dataInicial >= '"
						+ dataI + "' and dataFinal <= '" + dataF
						+ "' order by dataInicial ");

		List<Ciclo> lista = new ArrayList<Ciclo>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;
	}

	public List<Ciclo> getTipos(Date dataInicial) throws Exception {
		String dataI = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);

		Resultado result = con
				.consultar("select * from RDV_Tipo WHERE dataInicial >= '"
						+ dataI + "' order by dataInicial ");

		List<Ciclo> lista = new ArrayList<Ciclo>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;
	}

	public Ciclo getTipo(int id) throws Exception {

		Resultado result = con.consultar("select * from RDV_Tipo WHERE id = "
				+ id);

		Ciclo item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;

	}

	public Ciclo getTipo(Date data) throws Exception {

		String dataF = new SimpleDateFormat("dd-MM-yyyy").format(data);

		Resultado result = con
				.consultar("select * from RDV_Tipo WHERE dataInicial <= '"
						+ dataF + "' and dataFinal >= '" + dataF + "'");

		Ciclo item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;

	}

	public List<Ciclo> getTodosTipos() throws Exception {
		Resultado result = con
				.consultar("select * from RDV_Tipo order by tipo");

		List<Ciclo> lista = new ArrayList<Ciclo>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private Ciclo parse(Resultado result) throws Exception, SQLException {
		Ciclo tipo = new Ciclo();
		tipo.setId(result.getInt("id"));
		tipo.setTipo(result.getString("tipo"));
		tipo.setDataInicial(result.getDate("dataInicial"));
		tipo.setDataFinal(result.getDate("dataFinal"));
		tipo.setLiberado(result.getBoolean("liberado"));
		return tipo;
	}
}
