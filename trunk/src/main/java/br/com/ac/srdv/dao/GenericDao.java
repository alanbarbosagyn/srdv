package br.com.ac.srdv.dao;

import org.apache.log4j.Logger;

import br.com.ac.srdv.util.Conexao;

public abstract class GenericDao {
	protected String createTable;
	protected Conexao con = Conexao.getConexaoAtual();
	
	public void createTable() {
		con = Conexao.getConexaoAtual();
		try {
			con.executar(createTable);
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("Erro ao criar tabela no banco de dados!", e);
		}
	}

	public void excluir(String string) throws Exception {
		con.executar(string);
	}
}
