package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.Despesa;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class DespesaDao {

	Conexao con;
	
	public DespesaDao(){
		con = Conexao.getConexaoAtual();
	}

	public void createTable(){
		String createTable = "CREATE TABLE RDV_Despesa ( "
				+ "id integer primary key, despesa varchar(100) )";

		con = Conexao.getConexaoAtual();

		try {
			con.executar(createTable);
		} catch (Exception e) {

		}
	}

	public Despesa getDespesa(int id) throws Exception {
		Resultado result = con.consultar("select * from RDV_Despesa where id = "
				+ id);

		Despesa item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;
	}

	
	public List<Despesa> getTodasDespesas() throws Exception {
		Resultado result = con
				.consultar("select id, despesa from RDV_Despesa order by despesa");

		List<Despesa> lista = new ArrayList<Despesa>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private Despesa parse(Resultado result) throws Exception,
			SQLException {
		Despesa despesa = new Despesa();
		despesa.setId(Integer.parseInt(result.getString("id")));
		despesa.setDespesa(result.getString("despesa"));
		return despesa;
	}

}
