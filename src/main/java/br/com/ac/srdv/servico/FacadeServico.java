package br.com.ac.srdv.servico;

import java.util.List;

import javax.ws.rs.Path;

import br.com.ac.srdv.modelo.Despesa;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.KmLancamento;

@Path("/servico")
public interface FacadeServico {
	
//	@GET
//	public Response getCargos();
//	
//	public Response getAgenda();
//	
//	public Response getDespesa();
//	
//	public Response adicionarDespesa(@);
	
	public List<Despesa> getDespesa();
		
	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa, String imei);
	public DespesaLancamento adicionarDespesa(String idDespesa, String imei, String fotoBase64);
	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa, String imei, String fotoBase64);
	public KmLancamento adicionarKm(KmLancamento lancamento, String imei);
}
