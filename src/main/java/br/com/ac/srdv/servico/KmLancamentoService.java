package br.com.ac.srdv.servico;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.ac.srdv.dao.KmLancamentoDao;
import br.com.ac.srdv.modelo.KmLancamento;

@Path("/km")
public class KmLancamentoService {

	@POST
	@Path("/")
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response adicionarKm(KmLancamento lancamento){
		KmLancamentoDao kmLancamentoDao = new KmLancamentoDao();
		try {
			kmLancamentoDao.salvar(lancamento);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}")
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response getKmLancamento(@PathParam("id") String idKmLancamento){
		KmLancamentoDao kmLancamentoDao = new KmLancamentoDao();
		KmLancamento kmLancamento = null;
		try {
			kmLancamento = kmLancamentoDao.getKm(idKmLancamento);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(kmLancamento).build();
	}
//	public List<Despesa> getDespesa() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa,
//			String imei) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public DespesaLancamento adicionarDespesa(String idDespesa, String imei,
//			String fotoBase64) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public DespesaLancamento adicionarDespesa(DespesaLancamento despesa,
//			String imei, String fotoBase64) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public KmLancamento adicionarKm(KmLancamento lancamento, String imei) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
