package io.keyko.nevermined.models.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.keyko.nevermined.models.AbstractModel;
import io.keyko.nevermined.models.FromJsonToModel;

public class ExecuteService extends AbstractModel implements FromJsonToModel {


    @JsonProperty
    public String agreementId;

    @JsonProperty
    public String workflowId;

    @JsonProperty
    public String consumerAddress;

    @JsonProperty
    public String accessToken;

    public ExecuteService() {}

    public ExecuteService(String agreementId, String workflowId, String consumerAddress, String accessToken) {
        this.accessToken = accessToken;
        this.agreementId = agreementId;
        this.workflowId = workflowId;
        this.consumerAddress = consumerAddress;
    }

    @Override
    public String toString() {
        return "ExecuteService{" +
                " agreementId='" + agreementId + '\'' +
                ", workflowId='" + workflowId + '\'' +
                ", consumerAddress='" + consumerAddress + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

}
