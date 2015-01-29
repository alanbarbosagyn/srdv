	package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.Empresa;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class EmpresaDao {

	Conexao con;
	
	public EmpresaDao(){
		con = Conexao.getConexaoAtual();
	}

	public void createTable(){
		String createTable = "CREATE TABLE RDV_Empresa ( "
				+ "id integer primary key, empresa varchar(100) )";

		con = Conexao.getConexaoAtual();

		try {
			con.executar(createTable);
		} catch (Exception e) {

		}
	}

	public Empresa getEmpresa(int id) throws Exception {
		Resultado result = con.consultar("select * from RDV_Empresa where id = "
				+ id);

		Empresa item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;
	}

	
	public List<Empresa> getTodosEmpresas() throws Exception {
		Resultado result = con
				.consultar("select id, empresa from RDV_Empresa order by empresa");

		List<Empresa> lista = new ArrayList<Empresa>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		
		return lista;
	}

	private Empresa parse(Resultado result) throws Exception,
			SQLException {
		Empresa empresa = new Empresa();
		empresa.setId(Integer.parseInt(result.getString("id")));
		empresa.setEmpresa(result.getString("empresa"));
		return empresa;
	}

}
