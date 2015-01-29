package br.com.ac.srdv.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.DespesaLimite;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class DespesaLimiteDao {

	Conexao con;

	public DespesaLimiteDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		String createTable = "CREATE TABLE RDV_DespesaLimite ( "
				+ "tipo integer , usuario varchar(100), "
				+ " limite DECIMAL(10,2), primary key (tipo,usuario) )";

		con = Conexao.getConexaoAtual();
		try {
			con.executar(createTable);
		} catch (Exception e) {

		}
	}

	public void salvar(DespesaLimite limite) throws Exception {

		DespesaLimite aux = getDespesaLimite(limite.getTipo(),
				limite.getUsuario());

		if (aux == null) {
			con.executar("INSERT INTO RDV_DespesaLimite (tipo,usuario,limite) VALUES ("
					+ limite.getTipo()
					+ ",'"
					+ limite.getUsuario()
					+ "','"
					+ limite.getLimite() + "');");
		} else {
			con.executar("UPDATE RDV_DespesaLimite SET limite = "
					+ limite.getLimite() + " WHERE tipo = " + limite.getTipo()
					+ " and usuario = '" + limite.getUsuario() + "'");
		}
	}

	public void excluir(int id) throws Exception {
		if (id == 0)
			return;

		con.executar("DELETE FROM RDV_Tipo WHERE id = " + id);

	}

	public DespesaLimite getDespesaLimite(int tipo, String usuario)
			throws Exception {

		Resultado result = con
				.consultar("select * from RDV_DespesaLimite WHERE tipo = "
						+ tipo + " and usuario = '" + usuario + "'");

		DespesaLimite item = null;

		if (result.next())
			item = parse(result);
		result.close();

		return item;

	}

	public List<DespesaLimite> getLimites(int tipo) throws Exception {
		Resultado result = con
				.consultar("select * from RDV_DespesaLimite where tipo = "
						+ tipo + " order by usuario");

		List<DespesaLimite> lista = new ArrayList<DespesaLimite>();

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;
	}

	private DespesaLimite parse(Resultado result) throws Exception,
			SQLException {
		DespesaLimite item = new DespesaLimite();
		item.setTipo(result.getInt("tipo"));
		item.setUsuario(result.getString("usuario"));
		item.setLimite(result.getDouble("limite"));
		item.setLimiteOriginal(result.getDouble("limite_original"));
		return item;
	}
}
