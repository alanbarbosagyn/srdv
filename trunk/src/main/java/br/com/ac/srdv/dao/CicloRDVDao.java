package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.modelo.CicloRDV;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class CicloRDVDao {

	Conexao con;

	public CicloRDVDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		String createTable = "CREATE TABLE RDV_CicloRDV ( "
				+ "usuario VARCHAR(200), tipo VARCHAR(50), status VARCHAR(100), data DATE, obs VARCHAR(500), primary key (usuario,tipo,status)) ";

		con = Conexao.getConexaoAtual();
		try {
			con.executar(createTable);
		} catch (Exception e) {

		}
	}

	public void salvar(CicloRDV cicloRDV) throws Exception {

		// EXECUTA UM DELETE
		excluir(cicloRDV);

		String sql;
		sql = "INSERT INTO RDV_CICLORDV (USUARIO,TIPO,STATUS,DATA,OBS) VALUES (";
		sql += "'" + cicloRDV.getUsuario() + "',";
		sql += "'" + cicloRDV.getTipo() + "',";
		sql += "'" + cicloRDV.getStatus() + "',";
		sql += "'" + new SimpleDateFormat("dd-MM-yyyy").format(cicloRDV.getData()) + "',";
		sql += "'" + cicloRDV.getObservacao() + "')";
		con.executar(sql);

	}

	public void excluir(CicloRDV cicloRDV) throws Exception {

		String sql = "DELETE FROM RDV_CICLORDV WHERE USUARIO = '"
				+ cicloRDV.getUsuario() + "' AND TIPO = '" + cicloRDV.getTipo()
				+ "' AND STATUS = '" + cicloRDV.getStatus() + "'";
		con.executar(sql);

	}

	public List<CicloRDV> getCicloRDVs(String usuario, String tipo)
			throws Exception {

		Resultado result = con
				.consultar("select * from RDV_CicloRDV WHERE usuario = '"
						+ usuario + "' and tipo <= '" + tipo
						+ "' order by data ");

		List<CicloRDV> lista = new ArrayList<CicloRDV>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;
	}

	public List<CicloRDV> getCicloRDVs(Date dataInicial) throws Exception {
		String dataI = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);

		Resultado result = con
				.consultar("select * from RDV_CicloRDV WHERE dataInicial >= '"
						+ dataI + "' order by dataInicial ");

		List<CicloRDV> lista = new ArrayList<CicloRDV>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;
	}

	public CicloRDV getCicloRDV(String usuario, String tipo) throws Exception {

		Resultado result = con
				.consultar("select * from RDV_CicloRDV WHERE usuario = '"
						+ usuario + "' and tipo = '" + tipo + "'");

		CicloRDV item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;

	}

	public CicloRDV getCicloRDV(Date data) throws Exception {

		String dataF = new SimpleDateFormat("dd-MM-yyyy").format(data);

		Resultado result = con
				.consultar("select * from RDV_CicloRDV WHERE dataInicial <= '"
						+ dataF + "' and dataFinal >= '" + dataF + "'");

		CicloRDV item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;

	}

	public List<CicloRDV> getTodosCicloRDVs() throws Exception {
		Resultado result = con
				.consultar("select * from RDV_CicloRDV order by CicloRDV");

		List<CicloRDV> lista = new ArrayList<CicloRDV>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private CicloRDV parse(Resultado result) throws Exception, SQLException {
		CicloRDV it = new CicloRDV();

		it.setUsuario(result.getString("usuario"));
		it.setTipo(result.getString("tipo"));
		it.setStatus(result.getString("status"));
		it.setData(result.getDate("data"));
		it.setObservacao(result.getString("obs"));

		return it;
	}
}
