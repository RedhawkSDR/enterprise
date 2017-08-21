package redhawk.rest.endpoints;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.rest.converter.MetricsConverter;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.RedhawkMetrics;
import redhawk.rest.utils.MetricTypes;

@Path("/{nameserver}/domains/{domain}/metrics")
@Api(value = "/{nameserver}/domains/{domain}/metrics")
public class RedhawkMetricsResource extends RedhawkBaseResource{
	private static Logger logger = Logger.getLogger(RedhawkMetricsResource.class.getName());
	
	@ApiParam(value = "url for your name server", required = true)
	@PathParam("nameserver")
	private String nameServer;

	@ApiParam(value = "Name of REDHAWK Domain", required = true)
	@PathParam("domain")
	private String domainName;

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "GET all Metrics for a Domain")
	public RedhawkMetrics metrics() {
		return MetricsConverter.getMetrics(redhawkManager, nameServer, domainName);
	}
	
	@GET
	@Path("/application")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "GET all Application Metrics for a Domain")
	public List<ApplicationMetrics> applicationMetrics(){
		return MetricsConverter.getMetricByType(redhawkManager, nameServer, domainName, MetricTypes.APPLICATION);
	}
	
	@GET
	@Path("/port")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "GET all Application Metrics for a Domain")
	public List<ApplicationMetrics> portMetrics(){
		return MetricsConverter.getMetricByType(redhawkManager, nameServer, domainName, MetricTypes.PORT);
	}
}
