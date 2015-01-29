package br.com.ac.srdv.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class UsuarioDao {

	private Conexao con;

	public UsuarioDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {

		con = Conexao.getConexaoAtual();

		String sql = "CREATE TABLE RDV_Usuario (" + "id integer primary key, "
				+ "login varchar(50), " + "nome varchar(254), "
				+ "senha varchar(100), " + "empresa integer, "
				+ "filial integer, " + "cargo integer, " + "admin integer"
				+ ")";
		try {
			con.executar(sql);
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Usuario ADD admin integer");
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Usuario ADD ativo integer");
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Usuario ADD centroCusto varchar(50)");
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Usuario ADD kmMaximo decimal(12,2)");
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_Usuario ADD telefone varchar(20)");
		} catch (Exception e) {
		}
	}

	public Usuario getUsuario(String login) throws Exception {
		String sql = "SELECT * FROM RDV_Usuario Where login = '" + login + "'";

		Resultado result = con.consultar(sql);

		Usuario item = null;

		if (result.next())
			item = parse(result);

		result.close();
		return item;

	}

	public Usuario getUsuario(String login, String senha) throws Exception {
		String sql = "SELECT * FROM RDV_Usuario Where login = '" + login
				+ "' and senha = '" + senha + "'";
		Resultado result = con.consultar(sql);

		Usuario item = null;

		if (result.next())
			item = parse(result);

		result.close();
		return item;

	}

	public void salvar(Usuario usuario) throws Exception {

		if (usuario.getId() > 0) {
			alterar(usuario);
			return;
		}

		// PEGA O PROXIMO ID

		int id = 0;

		Resultado result = con.consultar("select max(id) from RDV_Usuario");
		if (result.next())
			id = result.getInt(1);
		result.close();

		id += 1;

		int admin;
		if (usuario.isAdmin())
			admin = 1;
		else
			admin = 0;

		int ativo;
		if (usuario.isAtivo())
			ativo = 1;
		else
			ativo = 0;

		String sql = "insert into RDV_Usuario ";
		sql += "(id,login,nome,senha,empresa,filial,cargo,admin, ativo, centroCusto,telefone,cartaocredito) VALUES (";
		sql += id + ",'";
		sql += usuario.getLogin() + "','";
		sql += usuario.getNome() + "','";
		sql += usuario.getSenha() + "','";
		sql += usuario.getEmpresa() + "','";
		sql += usuario.getFilial() + "','";
		sql += usuario.getCargo() + "',";
		sql += admin + ",";
		sql += ativo + ",'";
		sql += usuario.getCentroCusto() + "','";
		sql += usuario.getTelefone() + "','";
		sql += usuario.getCartaoCredito() + "'";
		sql += ")";

		con.executar(sql);

	}

	private void alterar(Usuario usuario) throws Exception {

		int admin;
		if (usuario.isAdmin())
			admin = 1;
		else
			admin = 0;

		int ativo;
		if (usuario.isAtivo())
			ativo = 1;
		else
			ativo = 0;

		String sql = "update RDV_Usuario SET ";
		sql += " login = '" + usuario.getLogin() + "'";
		sql += " ,nome = '" + usuario.getNome() + "'";
		sql += " ,senha = '" + usuario.getSenha() + "'";
		sql += " ,empresa = " + usuario.getEmpresa();
		sql += " ,filial = " + usuario.getFilial();
		sql += " ,cargo = " + usuario.getCargo();
		sql += " ,admin = " + admin;
		sql += " ,ativo = " + ativo;
		sql += " ,centroCusto = '" + usuario.getCentroCusto() + "'";
		sql += " ,telefone = '" + usuario.getTelefone() + "'";
		sql += " ,cartaoCredito = '" + usuario.getCartaoCredito() + "'";
		sql += " where id = " + usuario.getId();

		con.executar(sql);

	}

	public List<Usuario> getTodosUsuariosAtivos() throws Exception {

		String sql = "SELECT * from RDV_Usuario where ativo = 1 order by nome";

		List<Usuario> lista = new ArrayList<Usuario>();

		Resultado result = con.consultar(sql);

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;

	}

	public List<Usuario> getTodosUsuariosAtivosInativos() throws Exception {

		String sql = "SELECT * from RDV_Usuario order by nome";

		List<Usuario> lista = new ArrayList<Usuario>();

		Resultado result = con.consultar(sql);

		while (result.next()) {
			lista.add(parse(result));
		}
		result.close();
		return lista;

	}

	public void excluir(Usuario usuario) throws Exception {
		String sql = "delete from RDV_Usuario WHERE id = " + usuario.getId();

		con.executar(sql);

	}

	private Usuario parse(Resultado result) throws Exception {
		Usuario user = new Usuario();
		user.setId(result.getInt("id"));
		user.setLogin(result.getString("login"));
		user.setNome(result.getString("nome"));
		user.setSenha(result.getString("senha"));
		user.setEmpresa(result.getInt("empresa"));
		user.setFilial(result.getInt("filial"));
		user.setCargo(result.getInt("cargo"));
		user.setAdmin(result.getBoolean("admin"));
		user.setAtivo(result.getBoolean("ativo"));
		user.setCentroCusto(result.getString("centroCusto"));
		user.setTelefone(result.getString("telefone"));
		user.setCartaoCredito(result.getString("cartaoCredito"));
		return user;
	}
}
