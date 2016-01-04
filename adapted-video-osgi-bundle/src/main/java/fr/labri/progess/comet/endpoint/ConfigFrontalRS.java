package fr.labri.progess.comet.endpoint;

import java.net.URI;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import fr.labri.progess.comet.app.Context;
import fr.labri.progess.comet.cron.SchedulerUtils;

@Path("/config/frontal/")
public class ConfigFrontalRS {

	@Inject
	Context context;

	void reloadFrontalConfig() {
		SchedulerUtils.setupScheduler(context.getContent(), context.getConfigs(), context.getFrontalHostName(),
				context.getFrontalPort());
	}

	@Path("port/{value}")
	@PUT
	public Response updateFrontalPort(@PathParam("value") @NotNull Integer port) {

		context.setFrontalPort(port);
		reloadFrontalConfig();
		return Response.accepted().build();
	}

	@Path("hostname/{value}")
	@PUT
	public Response updateFrontalHost(@PathParam("value") @NotNull String host) {

		context.setFrontalHostName(host);
		reloadFrontalConfig();
		return Response.accepted().build();
	}

	@PUT
	public Response updateFrontalUri(@QueryParam("uri") @NotNull URI uri) {

		context.setFrontalHostName(uri.getHost());
		context.setFrontalPort(uri.getPort());
		reloadFrontalConfig();
		return Response.accepted().build();
	}
	
	
	@DELETE
	public Response stopFrontal() {

		context.setFrontalHostName(null);
		context.setFrontalPort(null);
		reloadFrontalConfig();
		return Response.accepted().build();
	}
}
