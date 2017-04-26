package pl.tciesla.mutual.fund.simulator.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/funds")
@Produces(MediaType.APPLICATION_XML)
public class MutualFundResource {

    private static final Logger logger = LoggerFactory.getLogger(MutualFundResource.class);

    @EJB
    private MutualFundRepository mutualFundRepository;

    @GET
    public Response getMutualFunds() {
        logger.info("request get mutual funds");
        List<MutualFund> mutualFunds = mutualFundRepository.findAll();
        return Response.ok(new GenericEntity<List<MutualFund>>(mutualFunds){}).build();
    }

}
