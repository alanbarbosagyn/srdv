package br.com.ac.srdv.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Conexao {

	private static Conexao con;
	protected Connection connection;
	protected long transacoes = 0;
	protected boolean rollback = false;
	protected List<Resultado> resultados;
	protected String historico;
	protected boolean ocorreuerro = false;

	public static String FORMAT_DATA_BANCO = "dd-MM-yyyy";

	// Statement statement = null;
	// Resultado Resultado = null;

	private Conexao() {
		connection = conectar();
		// conectarMySql();
	}

	public static Conexao getConexaoAtual() {
		if (con == null) {
			con = new Conexao();
		}

		return con;
	}

	public Connection getConnection() {
		if (con == null) {
			con = new Conexao();
		}
		return connection;
	}

	public static Connection conectar() {

		// ABRE O ARQUIVO DE CONFIGURA��O
		// PARA IDENTIFICAR O IP, USU�RIO E SENHA

		String ip = null;
		String usuario = null;
		String senha = null;
		String sid = null;
		String porta = null;

		try {
			Scanner in = new Scanner(new File("C:\\config_visitacao.ini"));
			String linha;
			while (in.hasNextLine()) {
				linha = in.nextLine();
				String s[] = linha.split("=");
				if (s[0].equals("ip"))
					ip = s[1];
				else if (s[0].equals("usuario"))
					usuario = s[1];
				else if (s[0].equals("senha"))
					senha = s[1];
				else if (s[0].equals("sid"))
					sid = s[1];
				else if (s[0].equals("porta"))
					porta = s[1];
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		if (ip == null || usuario == null || senha == null || sid == null
				|| porta == null) {
			System.out
					.println("N�O FOI POSS�VEL INICIAR CONEX�O COM O BANCO DE DADOS, ALGUNS CAMPOS EST�O FALTANDO NO ARQUIVO DE CONFIGURA��O 'C:\\config_visitacao.ini'");
			return null;
		}

		try {
			// Load the Oracle JDBC driver
			// System.out.println("INICIANDO A CONEX�O COM O BANCO\n");
			Class.forName("oracle.jdbc.OracleDriver");
			// System.out.println("Oracle JDBC driver loaded ok.");

			Connection connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@10.5.101.110:1521:BDMIG", "dbreport",
					"dbreport");
			// System.out.println("Connected with @10.5.100.240:1521:DADOUNDG");
			// connection.close();
			return connection;
		} catch (Exception e) {
			System.err.println("Exception AO CRIAR CONEXAO: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public int executar(String sql) throws Exception {
		Statement statement = null;
		int Resultado = 0;

		System.out.println(sql);

		try {
			historico += "\nExecutar: " + sql;

			statement = getConnection().createStatement();
			Resultado = statement.executeUpdate(sql);

		} catch (Exception exception) {
			ocorreuerro = true;
			exception.printStackTrace();
			throw new Exception("Exception ao executar (" + sql + "), "
					+ exception.getMessage(), exception);

		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return Resultado;
	}

	public int executarNoEx(String sql) {
		Statement statement = null;
		int Resultado = 0;

		System.out.println(sql);
		try {
			historico += "\nExecutar: " + sql;

			statement = getConnection().createStatement();
			Resultado = statement.executeUpdate(sql);

		} catch (Exception exception) {
			ocorreuerro = true;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}

		return Resultado;
	}

	public Resultado consultar(String sql) throws Exception {
		Statement statement = null;
		ResultSet resultset = null;

		System.out.println(sql);
		try {
			historico += "\nConsultar: " + sql;

			// statement = getConnection().createStatement();
			statement = getConnection().prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			resultset = statement.executeQuery(sql);

			if (resultados == null) {
				resultados = new ArrayList<Resultado>();
			}

			Resultado resultado = new Resultado(this, statement, resultset);
			resultados.add(resultado);

			return resultado;

		} catch (Exception exception) {
			ocorreuerro = true;
			if (resultset != null) {
				resultset.close();
			}

			if (statement != null) {
				statement.close();
			}

			throw new Exception("Exception ao executar (" + sql + "), "
					+ exception.getMessage(), exception);
		}
	}

	public void iniciar() throws Exception {
		Connection conn = getConnection();

		try {
			if (conn.getAutoCommit()) {
				historico += "\nBegin Transaction";

				conn.setAutoCommit(false);
			}
		} catch (Exception exception) {
			ocorreuerro = true;
			throw new Exception("Exception ao iniciar transa��o.", exception);
		}

		transacoes += 1;
	}

	public void concluir() throws Exception {
		if (!rollback) {
			if (transacoes > 0) {
				transacoes -= 1;

				if (transacoes == 0) {
					Connection conn = getConnection();
					try {
						historico += "\nCommit";

						conn.commit();
						conn.setAutoCommit(true);

					} catch (Exception exception) {
						ocorreuerro = true;
						throw new Exception(
								"Exception ao finalizar transa��o.", exception);
					}
				}
			} else {
				throw new Exception("N�o h� transa��es ativas.");
			}
		} else {
			throw new Exception(
					"A transa��o atual est� em situa��o de rollback.");
		}
	}

	public void cancelar() throws Exception {
		if (transacoes > 0) {
			transacoes -= 1;

			if (transacoes == 0) {
				Connection conn = getConnection();

				try {
					historico += "\nCommit";

					conn.rollback();
					conn.setAutoCommit(true);

					rollback = false;
				} catch (Exception exception) {
					ocorreuerro = true;
					throw new Exception("Exception ao finalizar transa��o.",
							exception);
				}
			} else {
				rollback = true;
			}
		} else {
			throw new Exception("N�o h� transa��es ativas.");
		}
	}
	
	public void close() {
		historico += "\nFechar";
		if (resultados != null) {
			while (!resultados.isEmpty()) {
				try {
					resultados.get(0).close();
				} catch (Exception exception) {
					ocorreuerro = true;
				}
			}
			resultados = null;
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (Exception exception) {
				ocorreuerro = true;
			}
			connection = null;

			if (ocorreuerro) {
				System.out.println(historico);
			}
		}
	}
	public void close(Resultado resultado) throws Exception {
		if (resultados.contains(resultado)) {
			resultados.remove(resultado);
			resultado.close();
		}
	}
}
