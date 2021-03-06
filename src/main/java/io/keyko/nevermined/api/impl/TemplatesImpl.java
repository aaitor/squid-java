package io.keyko.nevermined.api.impl;

import io.keyko.nevermined.api.TemplatesAPI;
import io.keyko.nevermined.exceptions.EthereumException;
import io.keyko.nevermined.manager.TemplatesManager;
import io.keyko.nevermined.models.service.template.TemplateSEA;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;


public class TemplatesImpl implements TemplatesAPI {

    private TemplatesManager templatesManager;

    /**
     * Constructor
     *
     * @param templatesManager the templatesManager
     */
    public TemplatesImpl(TemplatesManager templatesManager) {

        this.templatesManager = templatesManager;
    }

    @Override
    public TransactionReceipt propose(String templateAddress) throws EthereumException {
        return templatesManager.proposeTemplate(templateAddress);
    }

    @Override
    public TransactionReceipt approve(String templateAddress) throws EthereumException {
        return templatesManager.approveTemplate(templateAddress);
    }

    @Override
    public TransactionReceipt revoke(String templateAddress) throws EthereumException {
        return templatesManager.revokeTemplate(templateAddress);
    }

    @Override
    public boolean isApproved(String templateAddress) throws EthereumException {
        return templatesManager.isTemplateApproved(templateAddress);
    }

    @Override
    public BigInteger getListSize() throws EthereumException {
        return templatesManager.getTemplateListSize();
    }

    @Override
    public TemplateSEA getTemplate(String templateAddress) throws EthereumException {
        return templatesManager.getTemplate(templateAddress);
    }

}
