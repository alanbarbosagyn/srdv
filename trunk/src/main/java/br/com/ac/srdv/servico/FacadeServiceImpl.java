
package br.com.ac.srdv.servico;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class FacadeServiceImpl implements FacadeServico {
	
	@Context
    private static HttpHeaders httpHeaders;
	
    public Response getIt() {
        return Response.noContent().entity("Não há dados!").build();
    }

	@Override
	public Response getCargos() {
		return Response.ok("Tudo certo!").build();
	}
    
}
