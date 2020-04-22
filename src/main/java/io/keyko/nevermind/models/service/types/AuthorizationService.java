package io.keyko.nevermind.models.service.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.keyko.nevermind.models.service.Service;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class AuthorizationService extends Service {


    @JsonIgnore
    public static final int DEFAULT_INDEX = 2;

    @JsonIgnore
    public static final String DEFAULT_SERVICE = "SecretStore";

    public AuthorizationService() {
        this.index= DEFAULT_INDEX;
        this.type= ServiceTypes.authorization.toString();
    }

    public AuthorizationService(String serviceEndpoint, int index, String service) {
        super(ServiceTypes.authorization, serviceEndpoint, index);
        this.attributes.main.service = service;
    }

    public AuthorizationService(String serviceEndpoint, int index) {
        super(ServiceTypes.authorization, serviceEndpoint, index);
        this.attributes.main.service = DEFAULT_SERVICE;
    }

}