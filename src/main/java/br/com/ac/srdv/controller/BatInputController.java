package br.com.ac.srdv.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.KmLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.KmLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.OperacoesArquivos;

public class BatInputController {

	private List<Ciclo> tipos;
	private Ciclo tipoSelecionado;
	private int tipoId;
	private Date dataInicial;
	private Date dataFinal;

	public String Consultar() {
		try {

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -370);
			Date dIni = c.getTime();

			tipos = new CicloDao().getTipos(dIni);
			tipoId = new CicloDao().getTipo(new Date()).getId();

		} catch (Exception e) {
			e.printStackTrace();
		}
		dataFinal = new Date();
		return "BatInput";
	}

	public BatInputController() {
		Consultar();
	}

	public String Gerar() {

		// if (dataInicial == null || dataFinal == null) {
		// return "ConsultaDespesas";
		// }

		for (Ciclo t : tipos) {
			if (t.getId() == tipoId) {
				System.out.println("TIPO SELECIONADO: " + t.getTipo());
				tipoSelecionado = t;
				break;
			}
		}

		dataInicial = tipoSelecionado.getDataInicial();
		dataFinal = tipoSelecionado.getDataFinal();

		List<DespesaLancamento> despesas;
		List<KmLancamento> kms;

		List<Usuario> usuarios;
		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIO;", e1.getMessage());
			e1.printStackTrace();
			return "";
		}

		new File("C:\\TEMP").mkdir();
		String arq = "C:\\TEMP\\" + new Date().getTime() + ".xls";

		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		int row;
		int col;
		row = 0;
		col = 0;

		try {
			workbook = Workbook.createWorkbook(new File(arq));
			sheet = workbook.createSheet("BAT INPUT", 0);

			// SheetSettings settings = sheet.getSettings();
			// settings.setHorizontalFreeze(4);
			// settings.setVerticalFreeze(2);

			Label lb = new Label(col, row, "DATA");
			CellView cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "EMPRESA");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "FILIAL");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "CONTA");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "D/C");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "CC");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "VALOR");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "USUARIO");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "DOC");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "HP");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "HISTORICO");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			lb = new Label(++col, row, "FORNECEDOR");
			cv = sheet.getColumnView(col);
			cv.setAutosize(true);
			sheet.addCell(lb);
			row++;
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.Erro("ERRO AO CRIAR ARQUIVO", e.getMessage());
		}

		DespesaLancamentoDao desLanDao = new DespesaLancamentoDao();
		KmLancamentoDao kmLanDao = new KmLancamentoDao();

		for (Usuario usuario : usuarios) {
			try {
				despesas = desLanDao.getDespesasUsuario(usuario.getLogin(),
						dataInicial, dataFinal);
				kms = kmLanDao.getDespesasUsuario(usuario.getLogin(),
						dataInicial, dataFinal);

				double despesaDinheiro = 0;
				double despesaCartao = 0;
				double despesaTotal = 0;
				double despesaKM = 0;
				double despesaKMCartao = 0;
				double kmReceber = 0;
				double kmTotal = 0;
				double diferencaKM = 0;
				double valorReceber = 0;

				for (DespesaLancamento d : despesas) {
					despesaTotal += d.getDespesaValor();
					if (d.getFormaPagamento().equals("DINHEIRO"))
						despesaDinheiro += d.getDespesaValor();
					if (d.getFormaPagamento().equals("CARTAO"))
						despesaCartao += d.getDespesaValor();
					if (d.getDespesa().equals("COMBUSTIVEL"))
						despesaKM += d.getDespesaValor();
					if (d.getDespesa().equals("COMBUSTIVEL")
							&& d.getFormaPagamento().equals("CARTAO"))
						despesaKMCartao += d.getDespesaValor();
				}
				for (KmLancamento km : kms) {
					kmTotal += km.getKm();
					kmReceber += km.getIndiceKm() * km.getKm();
				}
				diferencaKM = kmReceber - despesaKM;
				valorReceber = despesaDinheiro + diferencaKM;

				Calendar cal = Calendar.getInstance();
				cal.setTime(dataFinal);
				int dia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				int mes = (cal.get(Calendar.MONDAY) + 1);
				int ano = cal.get(Calendar.YEAR);
				Date data = (new SimpleDateFormat("dd/MM/yyyy")).parse(dia
						+ "/" + mes + "/" + ano);

				String empresa = "0001";
				String filial = "0001";
				String doc = (new SimpleDateFormat("MMyyyy")).format(data);
				String hp = "0004";
				// GRAVA A PRIMEIRA LINHA
				col = 0;
				Label lb = new Label(col, row, new SimpleDateFormat(
						"dd/MM/yyyy").format(data));
				sheet.addCell(lb);
				lb = new Label(++col, row, "0001");
				sheet.addCell(lb);
				lb = new Label(++col, row, "0001");
				sheet.addCell(lb);
				lb = new Label(++col, row, "213010100018");
				sheet.addCell(lb);
				lb = new Label(++col, row, "C");
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getCentroCusto());
				sheet.addCell(lb);
				WritableCell nc = new jxl.write.Number(++col, row, valorReceber);
				sheet.addCell(nc);
				lb = new Label(++col, row, "32566");
				sheet.addCell(lb);
				lb = new Label(++col, row, doc);
				sheet.addCell(lb);
				lb = new Label(++col, row, "0004");
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getNome());
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getNome());
				sheet.addCell(lb);
				row++;
				// GRAVA A SEGUDA LINHA
				col = 0;
				lb = new Label(col, row,
						new SimpleDateFormat("dd/MM/yyyy").format(data));
				sheet.addCell(lb);
				lb = new Label(++col, row, "0001");
				sheet.addCell(lb);
				lb = new Label(++col, row, "0001");
				sheet.addCell(lb);
				lb = new Label(++col, row, "331010301058");
				sheet.addCell(lb);
				lb = new Label(++col, row, "D");
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getCentroCusto());
				sheet.addCell(lb);
				nc = new jxl.write.Number(++col, row, valorReceber);
				sheet.addCell(nc);
				lb = new Label(++col, row, "32566");
				sheet.addCell(lb);
				lb = new Label(++col, row, doc);
				sheet.addCell(lb);
				lb = new Label(++col, row, "1004");
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getNome());
				sheet.addCell(lb);
				lb = new Label(++col, row, usuario.getNome());
				sheet.addCell(lb);
				row++;
			} catch (Exception e) {
				Mensagem.Erro("ERRO AO CARREGAR DESPESAS", e.getMessage());
				e.printStackTrace();
				return "";
			}
		}

		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		OperacoesArquivos.downloadFile(arq, "", "txt",
				FacesContext.getCurrentInstance());

		return "";
	}

	public List<Ciclo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Ciclo> tipos) {
		this.tipos = tipos;
	}

	public Ciclo getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(Ciclo tipoSelecionado) {
		this.tipoSelecionado = tipoSelecionado;
	}

	public int getTipoId() {
		return tipoId;
	}

	public void setTipoId(int tipoId) {
		this.tipoId = tipoId;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
