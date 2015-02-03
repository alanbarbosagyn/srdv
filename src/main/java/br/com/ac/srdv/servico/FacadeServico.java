package br.com.ac.srdv.servico;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/servico")
public interface FacadeServico {
	
	@GET 
    @Produces("text/plain")
    public Response getIt();
	
	@GET
	public Response getCargos();
	
}
