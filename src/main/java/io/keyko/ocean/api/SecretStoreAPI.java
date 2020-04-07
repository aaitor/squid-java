package io.keyko.ocean.api;


import io.keyko.ocean.exceptions.EncryptionException;

/**
 * Exposes the Public API related with encryption functionalities
 */
public interface SecretStoreAPI {

    /**
     * Encrypts a document using Secret Store
     *
     * @param documentId the id of the document
     * @param content    the content
     * @param threshold  secret store threshold
     * @return a String with the encrypted content
     * @throws EncryptionException EncryptionException
     */
    public String encrypt(String documentId, String content, int threshold) throws EncryptionException;

    /**
     * Decrypts a document using Secret Store
     *
     * @param documentId       the id of the document
     * @param encryptedContent the encrypted content of the document
     * @return a String with the decrypted content
     * @throws EncryptionException EncryptionException
     */
    public String decrypt(String documentId, String encryptedContent) throws EncryptionException;


}
