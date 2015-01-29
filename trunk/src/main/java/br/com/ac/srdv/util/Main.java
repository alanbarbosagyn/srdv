package br.com.ac.srdv.util;

import java.util.TimeZone;

import br.com.ac.srdv.dao.CargoDao;
import br.com.ac.srdv.dao.DespesaDao;
import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.TipoDao;
import br.com.ac.srdv.dao.UsuarioDao;

public class Main {

	public static void main(String argv[]) {
		new CargoDao().createTable();
		new DespesaDao().createTable();
		new DespesaLancamentoDao().createTable();
		new EmpresaDao().createTable();
		new FilialDao().createTable();
		new TipoDao().createTable();
		new UsuarioDao().createTable();

		System.out.println("-------------------------------------");

		TimeZone timeZone = TimeZone.getDefault();

		for (String id : timeZone.getAvailableIDs()) {
			System.out.println(id);
		}

	}

}
