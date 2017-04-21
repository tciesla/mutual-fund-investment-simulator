package pl.tciesla.simulator.server.service;

import pl.tciesla.simulator.server.dao.MutualFundDao;
import pl.tciesla.simulator.server.domain.MutualFunds;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST web services to access funds resources.
 */
@Path("/funds")
@Stateless
public class FundsServiceBean {

    @EJB private MutualFundDao mutualFundDao;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public MutualFunds getFunds() {
        MutualFunds mutualFunds = new MutualFunds();
        mutualFunds.setMutualFunds(mutualFundDao.fetchAll());
        return mutualFunds;
    }
}
