package br.com.ac.srdv.servico;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.FotoDao;

import com.sun.jersey.core.util.Base64;

@Path("/despesa")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DespesaLancamentoService {
	
//	@GET
//	public Response getCargos();
//	
//	public Response getAgenda();
//	
//	public Response getDespesa();
//	
//	public Response adicionarDespesa(@);
	
//	public List<Despesa> getDespesa(){
	
	@POST
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response adicionarDespesa(DespesaLancamento despesa) {
		DespesaLancamentoDao despesaLancamentoDao = new DespesaLancamentoDao();
		try {
			despesaLancamentoDao.salvar(despesa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}
	
	@POST
	@Path("/foto")
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response adicionarDespesa(@PathParam("id") String idDespesa, @FormParam("foto") String fotoBase64){
		FotoDao fotoDao = new FotoDao();
		try {
			fotoDao.salvar(Base64.decode(fotoBase64));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}")
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response getDespesa(@PathParam("id") String idDespesa){
		DespesaLancamentoDao despesaLancamentoDao = new DespesaLancamentoDao();
		DespesaLancamento despesaLancamento = null;
		try {
			despesaLancamento = despesaLancamentoDao.getDespesa(idDespesa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(despesaLancamento).build();
	}
}
