package br.com.ac.srdv.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.modelo.KmLancamento;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Funcoes;
import br.com.ac.srdv.util.Resultado;

public class KmLancamentoDao {
	private Conexao con;

	public KmLancamentoDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		con = Conexao.getConexaoAtual();

		String createTable = "CREATE TABLE RDV_KmLancamento ("
				+ "id integer primary key, "
				+ "empresa varchar(100), filial varchar(100), "
				+ "centroCusto varchar(100), "
				+ "usuario varchar(100), cargo varchar(100), "
				+ "km decimal(12,2), indiceKm decimal(12,2), "
				+ "data Date, tipo varchar(100), localizacao varchar(254) )";

		try {
			con.executar(createTable);
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_KmLancamento ADD dataCriacao date");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			con.executar("ALTER TABLE RDV_KmLancamento ADD dataAtualizacao date");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public List<KmLancamento> getDespesasUsuario(String usuario,
			Date dataInicial, Date dataFinal) throws Exception {

		String dataIni = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);
		String dataFim = new SimpleDateFormat("dd-MM-yyyy").format(dataFinal);

		String sql = "select * from RDV_KmLancamento WHERE ";
		sql += " usuario = '" + usuario + "' and ";
		sql += " data between '" + dataIni + "' and '" + dataFim + "'";
		sql += " order by data, localizacao";

		Resultado result = con.consultar(sql);

		List<KmLancamento> lista = new ArrayList<KmLancamento>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;

	}

	public List<KmLancamento> getDespesasUsuario(String usuario, String tipo)
			throws Exception {
		String sql = "select * from RDV_KmLancamento WHERE ";
		sql += "usuario = '" + usuario + "'";
		sql += "tipo = '" + tipo + "'";
		sql += " order by data, localizacao";

		Resultado result = con.consultar(sql);

		List<KmLancamento> lista = new ArrayList<KmLancamento>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;

	}

	public void salvar(KmLancamento despesa) throws Exception {

		if (despesa.getId() != 0) {
			atualizar(despesa);
			return;
		}

		// PEGA O PROXIMO ID

		int id = 0;

		Resultado result = con
				.consultar("select max(id) from RDV_KmLancamento");
		if (result.next())
			id = result.getInt(1);
		result.close();
		id += 1;

		String sql = "insert into RDV_KmLancamento (id,empresa,filial, "
				+ " centroCusto,usuario,nome,cargo,km, indiceKm, data, tipo, localizacao, dataCriacao ) values ( "
				+ id
				+ ",'"
				+ despesa.getEmpresa()
				+ "','"
				+ despesa.getFilial()
				+ "','"
				+ despesa.getCentroCusto()
				+ "','"
				+ despesa.getUsuario()
				+ "','"
				+ despesa.getNome()
				+ "','"
				+ despesa.getCargo()
				+ "','"
				+ Funcoes.formatarDouble(despesa.getKm())
				+ "','"
				+ Funcoes.formatarDouble(despesa.getIndiceKm())
				+ "','"
				+ new SimpleDateFormat("dd-MM-yyyy").format(despesa.getData())
				+ "','"
				+ despesa.getTipo()
				+ "','"
				+ despesa.getLocalizacao()
				+ "','"
				+ new SimpleDateFormat("dd-MM-yyyy").format(new Date())
				+ "')";

		con.executar(sql);
	}

	private void atualizar(KmLancamento despesa) throws Exception {

		String sql = "update RDV_KmLancamento set " + "empresa = '"
				+ despesa.getEmpresa() + "', filial = '" + despesa.getFilial()
				+ "', centroCusto = '" + despesa.getCentroCusto()
				+ "', usuario = '" + despesa.getUsuario() + "',nome = '"
				+ despesa.getNome() + "', cargo = '" + despesa.getCargo()
				+ "', km = '" + Funcoes.formatarDouble(despesa.getKm())
				+ "', indiceKm = '"
				+ Funcoes.formatarDouble(despesa.getIndiceKm()) + "', data = '"
				+ new SimpleDateFormat("dd-MM-yyyy").format(despesa.getData())
				+ "', tipo = '" + despesa.getTipo() + "', dataAtualizacao = '"
				+ new SimpleDateFormat("dd-MM-yyyy").format(new Date())
				+ "', localizacao = '" + despesa.getLocalizacao()
				+ "' where id = " + despesa.getId();

		con.executar(sql);
	}

	public void excluir(KmLancamento despesa) throws Exception {
		String sql = "Delete from RDV_KmLancamento WHERE id = "
				+ despesa.getId();

		con.executar(sql);

	}

	private KmLancamento parse(Resultado result) throws Exception {
		KmLancamento despesa = new KmLancamento();
		despesa.setId(result.getInt("id"));
		despesa.setEmpresa(result.getString("empresa"));
		despesa.setFilial(result.getString("filial"));
		despesa.setCentroCusto(result.getString("centroCusto"));
		despesa.setCargo(result.getString("cargo"));
		despesa.setKm(result.getDouble("km"));
		despesa.setIndiceKm(result.getDouble("indiceKm"));
		despesa.setTipo(result.getString("tipo"));
		despesa.setData(result.getDate("data"));
		despesa.setUsuario(result.getString("usuario"));
		despesa.setNome(result.getString("nome"));
		despesa.setLocalizacao(result.getString("localizacao"));
		return despesa;
	}

	public KmLancamento getKm(String idKmLancamento) {
		// TODO Auto-generated method stub
		return null;
	}

}
