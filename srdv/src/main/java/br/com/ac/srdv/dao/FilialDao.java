package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.Filial;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class FilialDao {

	Conexao con;

	public FilialDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		String createTable = "CREATE TABLE RDV_Filial ( "
				+ "id integer primary key, filial varchar(100) )";

		con = Conexao.getConexaoAtual();

		try {
			con.executar(createTable);
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Filial ADD indiceKm decimal(12,2)");
		} catch (Exception e) {
		}
	}

	public Filial getFilial(int id) throws Exception {
		Resultado result = con.consultar("select * from RDV_Filial where id = "
				+ id);

		Filial item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;
	}

	public List<Filial> getTodosFilials() throws Exception {
		Resultado result = con
				.consultar("select * from RDV_Filial order by filial");

		List<Filial> lista = new ArrayList<Filial>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private Filial parse(Resultado result) throws Exception,
			SQLException {
		Filial filial = new Filial();
		filial.setId(Integer.parseInt(result.getString("id")));
		filial.setFilial(result.getString("filial"));
		filial.setIndiceKm(result.getDouble("indiceKm"));
		return filial;
	}

}
