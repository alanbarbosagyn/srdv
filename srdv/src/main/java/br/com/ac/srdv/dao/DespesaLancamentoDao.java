package br.com.ac.srdv.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Funcoes;
import br.com.ac.srdv.util.Resultado;

public class DespesaLancamentoDao {
	private Conexao con;

	public DespesaLancamentoDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		con = Conexao.getConexaoAtual();

		String createTable = "CREATE TABLE RDV_DespesaLancamento ("
				+ "id integer primary key, "
				+ "empresa varchar(100), filial varchar(100), "
				+ "filialDescricao varchar(100), centroCusto varchar(100), "
				+ "usuario varchar(100), cargo varchar(100), "
				+ "despesa varchar(100), formaPagamento varchar(100), "
				+ "despesaValor decimal(12,2), "
				+ "data Date, tipo varchar(100), obs varchar(254) )";

		try {
			con.executar(createTable);
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_DespesaLancamento ADD dataCriacao date");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			con.executar("ALTER TABLE RDV_DespesaLancamento ADD dataAtualizacao date");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public List<DespesaLancamento> getDespesasUsuario(String usuario,
			Date dataInicial, Date dataFinal) throws Exception {

		String dataIni = new SimpleDateFormat("dd-MM-yyyy").format(dataInicial);
		String dataFim = new SimpleDateFormat("dd-MM-yyyy").format(dataFinal);

		String sql = "select * from RDV_DespesaLancamento WHERE ";
		sql += "usuario = '" + usuario + "' and ";
		sql += "data between '" + dataIni + "' and '" + dataFim
				+ "' order by data, despesa";

		Resultado result = con.consultar(sql);

		List<DespesaLancamento> lista = new ArrayList<DespesaLancamento>();

		while (result.next())
			lista.add(parse(result));
		result.close();
		return lista;

	}

	public List<DespesaLancamento> getDespesasUsuario(String usuario,
			String tipo) throws Exception {
		String sql = "select * from RDV_DespesaLancamento WHERE ";
		sql += "usuario = '" + usuario + "'";
		sql += "tipo = '" + tipo + "'";

		Resultado result = con.consultar(sql);

		List<DespesaLancamento> lista = new ArrayList<DespesaLancamento>();

		while (result.next())
			lista.add(parse(result));

		result.close();
		return lista;

	}

	public void salvar(DespesaLancamento despesa) throws Exception {

		if (despesa.getId() != 0) {
			atualizar(despesa);
			return;
		}

		// PEGA O PROXIMO ID

		int id = 0;

		Resultado result = con
				.consultar("select max(id) from RDV_DespesaLancamento");
		if (result.next())
			id = result.getInt(1);
		result.close();
		id += 1;

		String sql = "insert into RDV_DespesaLancamento (id,empresa,filial, "
				+ "filialDescricao, centroCusto,usuario,nome,cargo,despesa, "
				+ "formaPagamento,despesaValor, data, tipo, obs, dataCriacao ) values ( "
				+ id
				+ ",'"
				+ despesa.getEmpresa()
				+ "','"
				+ despesa.getFilial()
				+ "','"
				+ despesa.getFilialDescricao()
				+ "','"
				+ despesa.getCentroCusto()
				+ "','"
				+ despesa.getUsuario()
				+ "','"
				+ despesa.getNome()
				+ "','"
				+ despesa.getCargo()
				+ "','"
				+ despesa.getDespesa()
				+ "','"
				+ despesa.getFormaPagamento()
				+ "','"
				+ Funcoes.formatarDouble(despesa.getDespesaValor())
				+ "','"
				+ new SimpleDateFormat("dd-MM-yyyy").format(despesa.getData())
				+ "','"
				+ despesa.getTipo()
				+ "','"
				+ despesa.getObs()
				+ "','"
				+ new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "')";

		con.executar(sql);
	}

	private void atualizar(DespesaLancamento despesa) throws Exception {

		String sql = "update RDV_DespesaLancamento set " + "empresa = '"
				+ despesa.getEmpresa() + "', filial = '" + despesa.getFilial()
				+ "', filialDescricao = '" + despesa.getFilialDescricao()
				+ "', centroCusto = '" + despesa.getCentroCusto()
				+ "', usuario = '" + despesa.getUsuario() + "', nome = '"
				+ despesa.getNome() + "', cargo = '" + despesa.getCargo()
				+ "', despesa = '" + despesa.getDespesa()
				+ "', formaPagamento = '" + despesa.getFormaPagamento()
				+ "', despesaValor = '"
				+ Funcoes.formatarDouble(despesa.getDespesaValor())
				+ "', data = '"
				+ new SimpleDateFormat("dd-MM-yyyy").format(despesa.getData())
				+ "', tipo = '" + despesa.getTipo() + "', dataAtualizacao = '"
				+ new SimpleDateFormat("dd-MM-yyyy").format(new Date())
				+ "', obs = '" + despesa.getObs() + "' where id = "
				+ despesa.getId();

		con.executar(sql);
	}

	public void excluir(DespesaLancamento despesa) throws Exception {
		String sql = "Delete from RDV_DespesaLancamento WHERE id = "
				+ despesa.getId();

		con.executar(sql);

	}

	private DespesaLancamento parse(Resultado result) throws Exception {
		DespesaLancamento despesa = new DespesaLancamento();
		despesa.setId(result.getInt("id"));
		despesa.setEmpresa(result.getString("empresa"));
		despesa.setFilial(result.getString("filial"));
		despesa.setFilialDescricao(result.getString("filialDescricao"));
		despesa.setCentroCusto(result.getString("centroCusto"));
		despesa.setCargo(result.getString("cargo"));
		despesa.setDespesa(result.getString("despesa"));
		despesa.setFormaPagamento(result.getString("formaPagamento"));
		despesa.setDespesaValor(result.getDouble("despesaValor"));
		despesa.setTipo(result.getString("tipo"));
		despesa.setData(result.getDate("data"));
		despesa.setUsuario(result.getString("usuario"));
		despesa.setNome(result.getString("nome"));
		despesa.setObs(result.getString("obs"));
		return despesa;
	}

}
