
package br.com.ac.srdv.servico;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import br.com.ac.srdv.modelo.Despesa;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.KmLancamento;

public class FacadeServiceImpl implements FacadeServico {
	
	@Context
    private static HttpHeaders httpHeaders;

	@Override
	public List<Despesa> getDespesa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa,
			String imei) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DespesaLancamento adicionarDespesa(String idDespesa, String imei,
			String fotoBase64) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa,
			String imei, String fotoBase64) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KmLancamento adicionarKm(KmLancamento lancamento, String imei) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
