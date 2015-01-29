package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.Cargo;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class CargoDao {

	Conexao con;

	public CargoDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		String createTable = "CREATE TABLE RDV_Cargo ( "
				+ "id integer primary key, cargo varchar(100) )";

		con = Conexao.getConexaoAtual();

		try {
			con.executar(createTable);
		} catch (Exception e) {

		}
	}

	public Cargo getCargo(int id) throws Exception {
		Resultado result = con.consultar("select * from RDV_Cargo where id = "
				+ id);

		Cargo item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;
	}

	public List<Cargo> getTodosCargos() throws Exception {
		Resultado result = con
				.consultar("select * from RDV_Cargo order by cargo");

		List<Cargo> lista = new ArrayList<Cargo>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private Cargo parse(Resultado result) throws Exception, SQLException {
		Cargo cargo = new Cargo();
		cargo.setId(Integer.parseInt(result.getString("id")));
		cargo.setCargo(result.getString("cargo"));
		return cargo;
	}

}
