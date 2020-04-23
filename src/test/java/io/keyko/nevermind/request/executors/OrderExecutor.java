package io.keyko.nevermind.request.executors;

import io.keyko.nevermind.api.NevermindAPI;
import io.keyko.nevermind.exceptions.DDOException;
import io.keyko.nevermind.models.DDO;
import io.keyko.nevermind.models.asset.OrderResult;
import io.reactivex.Flowable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderExecutor extends BaseOceanExecutor{

    private static final Logger log = LogManager.getLogger(OrderExecutor.class);


    private static DDO ddo;

    @Override
    public void setUp() {

        super.setUp();

        try {
            log.info("Creating DDO...");
            ddo = nevermindAPIPublisher.getAssetsAPI().create(metadataBase, providerConfig);
            log.info("DDO Created");
        } catch (DDOException e) {
            log.error(e.getMessage());
        }

    }


    @Override
    public Boolean executeRequest() throws Exception {

        if (ddo == null || ddo.getDid() == null)
            throw new Exception("DDO is not created");

        log.info("Executing Request");

        NevermindAPI nevermindAPI = getNextOceanApi();
        log.info("Using NevermindAPI Instance with the main Address: " + nevermindAPI.getMainAccount().address);

        Flowable<OrderResult> result = nevermindAPI.getAssetsAPI().order(ddo.getDid(), 1);
        OrderResult orderResult = result.blockingFirst();

        log.info("Result of the order flow. AccessGranted: " + orderResult.isAccessGranted() + ". ServiceAgreementId: " + orderResult.getServiceAgreementId());

        if (orderResult.isAccessGranted() && orderResult.getServiceAgreementId()!= null)
            return true;

        return false;

    }
}
