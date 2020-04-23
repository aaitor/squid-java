package io.keyko.nevermind.manager;

import io.keyko.nevermind.api.config.NevermindConfig;
import io.keyko.nevermind.external.MetadataService;
import io.keyko.nevermind.contracts.*;
import com.oceanprotocol.secretstore.core.EvmDto;
import com.oceanprotocol.secretstore.core.SecretStoreDto;
import io.keyko.common.web3.KeeperService;
import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public abstract class ManagerHelper {

    private static final Logger log = LogManager.getLogger(ManagerHelper.class);

    public enum VmClient { ganache, parity}

    public static KeeperService getKeeper(Config config) throws IOException, CipherException {
        return getKeeper(config, VmClient.ganache);
    }

    public static KeeperService getKeeper(String url, String address, String password, String file, BigInteger gasLimit, BigInteger gasPrice, int attempts, long sleepDuration) throws IOException, CipherException {
        KeeperService keeper= KeeperService.getInstance(url, address, password, file, attempts, sleepDuration);

        keeper.setGasLimit(gasLimit)
                .setGasPrice(gasPrice);

        return keeper;
    }

    public static KeeperService getKeeper(Config config, VmClient client) throws IOException, CipherException {

         return getKeeper(
                config.getString("keeper.url"),
                config.getString("account." + client.toString() + ".address"),
                config.getString("account." + client.toString() + ".password"),
                config.getString("account." + client.toString() + ".file"),
                BigInteger.valueOf(config.getLong("keeper.gasLimit")),
                BigInteger.valueOf(config.getLong("keeper.gasPrice")),
                config.getInt("keeper.tx.attempts"),
                config.getLong("keeper.tx.sleepDuration")
         );


    }


    public static KeeperService getKeeper(Config config, VmClient client, String nAddress) throws IOException, CipherException {
        KeeperService keeper= KeeperService.getInstance(
                config.getString("keeper.url"),
                config.getString("account." + client.toString() + ".address" + nAddress),
                config.getString("account." + client.toString() + ".password" + nAddress),
                config.getString("account." + client.toString() + ".file" + nAddress),
                config.getInt("keeper.tx.attempts"),
                config.getLong("keeper.tx.sleepDuration")
        );

        keeper.setGasLimit(BigInteger.valueOf(config.getLong("keeper.gasLimit")))
                .setGasPrice(BigInteger.valueOf(config.getLong("keeper.gasPrice")));

        return keeper;
    }

    public static MetadataService getMetadataService(Config config) {
        return MetadataService.getInstance(config.getString("metadata.url"));
    }

    public static SecretStoreDto getSecretStoreDto(Config config) {
        return SecretStoreDto.builder(config.getString("secretstore.url"));
    }

    public static EvmDto getEvmDto(Config config, VmClient client) {
        return EvmDto.builder(
                config.getString("keeper.url"),
                config.getString("account." + client.toString() + ".address"),
                config.getString("account." + client.toString() + ".password")
        );
    }

    public static SecretStoreManager getSecretStoreController(Config config, VmClient client) {
        return SecretStoreManager.getInstance(getSecretStoreDto(config),getEvmDto(config, client));
    }

    public static SecretStoreManager getSecretStoreController(Config config, EvmDto evmDto) {
        return SecretStoreManager.getInstance(getSecretStoreDto(config), evmDto);
    }
/*
    public static boolean prepareEscrowTemplate(NevermindAPI nevermindAPI, String accessSecretStoreConditionAddress, String lockRewardConditionAddress, String escrowRewardConditionAddress, String owner, String templateName) throws EthereumException, InterruptedException {

        BigInteger numberTemplates= nevermindAPI.getTemplatesAPI().getListSize();
        log.debug("Number of existing templates: " + numberTemplates.toString());

        try {
            log.debug("Registering actor type");
            nevermindAPI.getTemplatesAPI().registerActorType("consumer");
            nevermindAPI.getTemplatesAPI().registerActorType("provider");
        } catch (EthereumException ex)  {}

        byte[] _id = CryptoHelper.keccak256(templateName);
        String templateId = EthereumHelper.remove0x(EncodingHelper.toHexString(_id));

        TemplateSEA template= nevermindAPI.getTemplatesAPI().getTemplate(templateId);

        if (template.state.compareTo(TemplateSEA.TemplateState.Uninitialized.getStatus()) == 0) {
            log.debug("Proposing template: " + templateId);

            byte[] consumerTypeId = nevermindAPI.getTemplatesAPI().getActorTypeId("consumer");
            byte[] providerTypeId = nevermindAPI.getTemplatesAPI().getActorTypeId("provider");

            nevermindAPI.getTemplatesAPI().propose(
                    _id,
                    Arrays.asList(accessSecretStoreConditionAddress, lockRewardConditionAddress, escrowRewardConditionAddress),
                    Arrays.asList(providerTypeId, consumerTypeId),
                    templateName);

        }

        for (int counter= 0; counter<10; counter++) {
            log.debug("Waiting for the template proposal ...");
            template= nevermindAPI.getTemplatesAPI().getTemplate(templateName);
            if (template.state.compareTo(TemplateSEA.TemplateState.Proposed.getStatus()) == 0) {
                log.debug("Template " + templateId + " in Proposed state");
                break;
            }
            ManagerHelper.class.wait(1000L);
        }

        final boolean isApproved = nevermindAPI.getTemplatesAPI().isApproved(templateName);

        if (!isApproved) {
            log.debug("Approving template: " + templateId);
            nevermindAPI.getTemplatesAPI().approve(templateName);
        }

        return true;
    }*/

    /**
     * Returns a Properties object with the entries necessary to run the integration tests
     * @param config
     * @return
     */
    public static Properties getDefaultProperties(Config config, String numAddress)    {
        Properties properties = new Properties();
        properties.put(NevermindConfig.KEEPER_URL, config.getString("keeper.url"));
        properties.put(NevermindConfig.KEEPER_GAS_LIMIT, config.getString("keeper.gasLimit"));
        properties.put(NevermindConfig.KEEPER_GAS_PRICE, config.getString("keeper.gasPrice"));
        properties.put(NevermindConfig.KEEPER_TX_ATTEMPTS, config.getString("keeper.tx.attempts"));
        properties.put(NevermindConfig.KEEPER_TX_SLEEPDURATION, config.getString("keeper.tx.sleepDuration"));
        properties.put(NevermindConfig.METADATA_URL, config.getString("metadata.url"));
        properties.put(NevermindConfig.SECRETSTORE_URL, config.getString("secretstore.url"));
        properties.put(NevermindConfig.CONSUME_BASE_PATH, config.getString("consume.basePath"));
        properties.put(NevermindConfig.MAIN_ACCOUNT_ADDRESS, config.getString("account.parity.address" + numAddress));
        properties.put(NevermindConfig.MAIN_ACCOUNT_PASSWORD, config.getString("account.parity.password" + numAddress));
        properties.put(NevermindConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, config.getString("account.parity.file" + numAddress));
        properties.put(NevermindConfig.DID_REGISTRY_ADDRESS, config.getString("contract.DIDRegistry.address"));
        properties.put(NevermindConfig.AGREEMENT_STORE_MANAGER_ADDRESS, config.getString("contract.AgreementStoreManager.address"));
        properties.put(NevermindConfig.CONDITION_STORE_MANAGER_ADDRESS, config.getString("contract.ConditionStoreManager.address"));
        properties.put(NevermindConfig.LOCKREWARD_CONDITIONS_ADDRESS, config.getString("contract.LockRewardCondition.address"));
        properties.put(NevermindConfig.ESCROWREWARD_CONDITIONS_ADDRESS, config.getString("contract.EscrowReward.address"));
        properties.put(NevermindConfig.ACCESS_SS_CONDITIONS_ADDRESS, config.getString("contract.AccessSecretStoreCondition.address"));
        properties.put(NevermindConfig.TEMPLATE_STORE_MANAGER_ADDRESS, config.getString("contract.TemplateStoreManager.address"));
        properties.put(NevermindConfig.TOKEN_ADDRESS, config.getString("contract.OceanToken.address"));
        properties.put(NevermindConfig.DISPENSER_ADDRESS, config.getString("contract.Dispenser.address"));
        properties.put(NevermindConfig.PROVIDER_ADDRESS, config.getString("provider.address"));
        properties.put(NevermindConfig.COMPUTE_EXECUTION_CONDITION_ADDRESS, config.getString("contract.ComputeExecutionCondition.address"));
        return properties;
    }

    public static OceanToken loadOceanTokenContract(KeeperService keeper, String address) {
        return OceanToken.load(
                address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider());
    }


    public static Dispenser loadDispenserContract(KeeperService keeper, String address) {
        return Dispenser.load(
                address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }


    public static DIDRegistry loadDIDRegistryContract(KeeperService keeper, String address) {

        return DIDRegistry.load(
                address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }


    public static EscrowAccessSecretStoreTemplate loadEscrowAccessSecretStoreTemplate(KeeperService keeper, String address) throws Exception, IOException, CipherException {
        return EscrowAccessSecretStoreTemplate.load(
                address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider());
    }

    public static EscrowReward loadEscrowRewardContract(KeeperService keeper, String address) {
        return EscrowReward.load(
                address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }

    public static LockRewardCondition loadLockRewardCondition(KeeperService keeper, String address) {
        return LockRewardCondition.load(address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }

    public static AccessSecretStoreCondition loadAccessSecretStoreConditionContract(KeeperService keeper, String address) {
        return AccessSecretStoreCondition.load(address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
                );
    }

    public static TemplateStoreManager loadTemplateStoreManager(KeeperService keeper, String address) {
        return TemplateStoreManager.load(address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }

    public static AgreementStoreManager loadAgreementStoreManager(KeeperService keeper, String address) {
        return AgreementStoreManager.load(address,
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }

    public static ConditionStoreManager loadConditionStoreManager(KeeperService keeper, String address) {
        return ConditionStoreManager.load(address,
                keeper.getWeb3(),
//                keeper.getCredentials(),
                keeper.getTxManager(),
                keeper.getContractGasProvider()
        );
    }


    public static TemplateStoreManager deployTemplateStoreManager(KeeperService keeper) throws Exception {
        log.debug("Deploying TemplateStoreManager with address: " + keeper.getCredentials().getAddress());
        return TemplateStoreManager.deploy(
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider())
                .send();
    }

    public static EscrowAccessSecretStoreTemplate deployEscrowAccessSecretStoreTemplate(KeeperService keeper) throws Exception {
        log.debug("Deploying EscrowAccessSecretStoreTemplate with address: " + keeper.getCredentials().getAddress());
        return EscrowAccessSecretStoreTemplate.deploy(
                keeper.getWeb3(),
                keeper.getTxManager(),
                keeper.getContractGasProvider())
                .send();
    }
}
