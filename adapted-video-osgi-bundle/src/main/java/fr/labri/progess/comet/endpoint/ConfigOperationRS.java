package fr.labri.progess.comet.endpoint;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.labri.progess.comet.app.Context;
import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.ContentWrapper;
import fr.labri.progess.comet.model.FilterConfig;
import fr.labri.progess.comet.model.FilterConfigWrapper;

@Path("op")
public class ConfigOperationRS {

	@Inject
	Context context;

	@Produces(MediaType.APPLICATION_XML)
	@Path("config")
	@GET

	public FilterConfigWrapper getAllConfig() {

		//
		//
		// FilterConfig fc = new FilterConfig();
		// HeaderFilter hf = new HeaderFilter();
		// hf.setHeader("host");
		// hf.setValue("foofl.fez");
		// fc.getHeaderRequestValues().add(hf);
		// fc.getHeaderRequestValues().add(hf);
		// fc.getHeaderResponseValues().add(hf);
		//
		// this.context.getConfigs().add(fc);
		//
		// FilterConfigWrapper obj =
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//
		// try {
		// JAXBContext.newInstance(FilterConfigWrapper.class).createMarshaller().marshal(obj,
		// bos);
		// } catch (JAXBException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println(bos.toString());
		//

		return FilterConfigWrapper.wraps(this.context.getConfigs());

	}

	@Produces(MediaType.APPLICATION_XML)
	@Path("content")
	@GET

	public ContentWrapper getAllContents() {
		return ContentWrapper.wraps(this.context.getContent().values());

	}

	@Consumes(MediaType.APPLICATION_XML)
	@Path("config")
	@PUT
	public Response addFilterConfig(FilterConfigWrapper configs) {

		for (FilterConfig config : configs) {
			context.getConfigs().add(config);
		}
		return Response.accepted().build();
	}

	@Consumes(MediaType.APPLICATION_XML)
	@Path("content")
	@PUT
	public Response addContents(ContentWrapper contents) {

		for (Content content : contents) {
			context.getContent().put(content.getUri(), content);
		}

		return Response.accepted().build();
	}

	@Path("config")
	@DELETE
	public Response delAllConfigs() {
		int count = context.getConfigs().size();
		context.getConfigs().clear();
		return Response.accepted("deleted " + count + " configs").build();
	}

	@Path("/content")
	@DELETE
	public Response getAllContent() {
		int count = context.getContent().size();
		context.getContent().clear();
		return Response.accepted("deleted " + count + " content").build();
	}

}
