package io.keyko.nevermined.external;

import com.fasterxml.jackson.core.type.TypeReference;
import io.keyko.common.helpers.HttpHelper;
import io.keyko.common.models.HttpResponse;
import io.keyko.nevermined.exceptions.DDOException;
import io.keyko.nevermined.models.AbstractModel;
import io.keyko.nevermined.models.DDO;
import io.keyko.nevermined.models.asset.AssetMetadata;
import io.keyko.nevermined.models.metadata.SearchQuery;
import io.keyko.nevermined.models.metadata.SearchResult;
import org.apache.commons.httpclient.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Service for Metadata Integration
 */
public class MetadataApiService {

    private static final Logger log = LogManager.getLogger(MetadataApiService.class);

    private static final String DDO_URI = "/api/v1/metadata/assets/ddo";
    private String ddoEndpoint;

    /**
     * Builds an instance of MetadataApiService
     *
     * @param url url of metadata api
     * @return MetadataApiService instance
     */
    public static MetadataApiService getInstance(String url) {
        log.debug("Getting Metadata API instance: " + url);
        return new MetadataApiService(url);
    }

    /**
     * Constructor
     *
     * @param url the url of Metadata Api
     */
    private MetadataApiService(String url) {
        String url1 = url.replaceAll("/$", "");
        this.ddoEndpoint = url1 + DDO_URI;
    }

    public String getDdoEndpoint() {
        return ddoEndpoint;
    }

    /**
     * Registers a new DDO in Metadata Api
     *
     * @param ddo the ddo
     * @return the created DDO
     * @throws DDOException DDOException
     */
    public DDO createDDO(DDO ddo) throws DDOException {

        log.debug("Creating DDO: " + ddo.id);

        try {

            HttpResponse response = HttpHelper.httpClientPost(
                    this.ddoEndpoint, new ArrayList<>(), DDO.cleanFileUrls(ddo).toJson());

            if (response.getStatusCode() != 201) {
                throw new DDOException("Unable to create DDO: " + response.toString());
            }

            return DDO.fromJSON(new TypeReference<DDO>() {
            }, response.getBody());

        } catch (Exception e) {
            throw new DDOException("Error building DDO from JSON", e);
        }

    }

    /**
     * Gets a DDO from an URL
     *
     * @param url the url
     * @return the DDO
     * @throws DDOException DDOException
     */
    public DDO getDDO(String url) throws DDOException {

        log.debug("Getting DDO: " + url);
        HttpResponse response;

        try {
            response = HttpHelper.httpClientGet(url);
        } catch (HttpException e) {
            throw new DDOException("Unable to get DDO", e);
        }

        if (response.getStatusCode() != 200) {
            throw new DDOException("Unable to get DDO: " + response.toString());
        }
        try {
            return DDO.fromJSON(new TypeReference<DDO>() {
            }, response.getBody());
        } catch (Exception e) {
            throw new DDOException("Error building DDO from JSON", e);
        }
    }

    /**
     * Gets a DDO from the DID
     *
     * @param id the DID
     * @return the DDO
     * @throws Exception Exception
     */
    public DDO getDDOUsingId(String id) throws Exception {
        return getDDO(this.ddoEndpoint + "/" + id);

    }


    /**
     * Updates the metadata of a DDO
     *
     * @param id  the did
     * @param ddo the DDO
     * @return a flag that indicates if the update operation was executed correctly
     * @throws Exception Exception
     */
    public boolean updateDDO(String id, DDO ddo) throws Exception {
        HttpResponse response = HttpHelper.httpClientPut(
                this.ddoEndpoint + "/" + id, new ArrayList<>(), ddo.toJson());

        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            return true;
        }
        throw new Exception("Unable to update DDO: " + response.toString());
    }

    /**
     * Search all the DDOs that match the text passed as a parameter
     *
     * @param param  the criteria
     * @param offset parameter to paginate
     * @param page   parameter to paginate
     * @return a List of all the DDOs found
     * @throws DDOException DDOException
     */
    public SearchResult searchDDO(String param, int offset, int page) throws DDOException {

        String url = this.ddoEndpoint + "/query?text=" + param + "&page=" + page + "&offset=" + offset;
        HttpResponse response;

        try {
            response = HttpHelper.httpClientGet(url);
        } catch (HttpException e) {
            throw new DDOException("Unable to get DDO: ", e);
        }

        if (response.getStatusCode() != 200) {
            throw new DDOException("Unable to search for DDO's: " + response.toString());
        }

        try {
            return AbstractModel
                    .getMapperInstance()
                    .readValue(response.getBody(), new TypeReference<SearchResult>() {
                    });
        } catch (IOException e) {
            throw new DDOException("Unable to search for DDO's: ", e);
        }

    }

    /**
     * Search all the DDOs that match the query passed as a parameter
     *
     * @param searchQuery the query
     * @return a List of all the DDOs found
     * @throws DDOException DDOException
     */
    public SearchResult searchDDO(SearchQuery searchQuery) throws DDOException {

        HttpResponse response;

        try {
            response = HttpHelper.httpClientPost(
                    this.ddoEndpoint + "/query", new ArrayList<>(), searchQuery.toJson());
        } catch (Exception e) {
            throw new DDOException("Unable to get DDO", e);
        }

        if (response.getStatusCode() != 200) {
            throw new DDOException("Unable to search for DDO's: " + response.toString());
        }

        try {
            return AbstractModel
                    .getMapperInstance()
                    .readValue(response.getBody(), new TypeReference<SearchResult>() {
                    });
        } catch (IOException e) {
            throw new DDOException("Unable to search for DDO's", e);
        }

    }

    /**
     * Retire the asset ddo from Metadata Api.
     *
     * @param id the did
     * @return a flag that indicates if the retire operation was executed correctly
     * @throws DDOException DDOException
     */
    public boolean retireAssetDDO(String id) throws DDOException {
        HttpResponse response;
        try {
            response = HttpHelper.httpClientDelete(this.ddoEndpoint + "/" + id);
        } catch (Exception e) {
            throw new DDOException("Unable to retire DDO with DID: " + id, e);
        }
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            return true;
        } else {
            throw new DDOException("Unable to retire DDO with DID: " + id);
        }
    }

    /**
     * Check that the metadata has a valid formUrl.
     *
     * @param metadata the metadata of the DDO
     * @return a flag that indicates if the metadata is valid
     * @throws DDOException DDOException
     */
    public boolean validateMetadata(AssetMetadata metadata) throws DDOException {
        HttpResponse response;
        try {
            response = HttpHelper.httpClientPost(
                    this.ddoEndpoint + "/validate", new ArrayList<>(), metadata.toJson());
        } catch (Exception e) {
            throw new DDOException("Unable to call the validate endpoint", e);
        }
        return response.getBody().contains("true");
    }
}
