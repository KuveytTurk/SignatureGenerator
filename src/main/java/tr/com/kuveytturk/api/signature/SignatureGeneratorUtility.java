/*
 * Copyright (c) 2020
 * KUVEYT TÜRK PARTICIPATION BANK INC.
 *
 * Author: Fikri Aydemir
 *
 * Project: API Request SignatureGenerator
 */

package tr.com.kuveytturk.api.signature;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public final class SignatureGeneratorUtility {

    /**
     * Utility method for generating signature for GET requests
     *
     * @param accessToken        The clientId that is provided by Kuveyt Türk API market when an application
     *                           is created.
     * @param privateKeyAsString The RSA private key as a string object
     * @param queryParams        The query parameter names and values as an HashMap object
     * @return The base64 encoded signature by using SHA256/RSA
     */
    public static String generateSignatureForGetRequest(String accessToken,
                                                        String privateKeyAsString,
                                                        List<QueryParameterBean> queryParams) throws SignatureGenerationException {

        String queryString = getQueryParamsString(queryParams);
        String input = accessToken.trim() + queryString;
        String base64Signature = null;

        try {
            PrivateKey privateKey = buildPrivateKeyFromString(privateKeyAsString);
            base64Signature = signSHA256RSA(input, privateKey);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            SignatureGenerationException signatureEx = new SignatureGenerationException(msg, e);
            throw signatureEx;
        }

        return base64Signature;
    }

    /**
     * Utility method for generating signature for GET requests
     *
     * @param accessToken        The clientId that is provided by Kuveyt Türk API market when an application
     *                           is created.
     * @param privateKeyAsString The RSA private key as a string object
     * @return The base64 encoded signature by using SHA256/RSA
     */
    public static String generateSignatureForGetRequest(String accessToken,
                                                        String privateKeyAsString) throws SignatureGenerationException {
        String input = accessToken.trim();
        String base64Signature = null;

        try {
            PrivateKey privateKey = buildPrivateKeyFromString(privateKeyAsString);
            base64Signature = signSHA256RSA(input, privateKey);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            SignatureGenerationException signatureEx = new SignatureGenerationException(msg, e);
            throw signatureEx;
        }

        return base64Signature;
    }

    /**
     * Utility method for generating signature for POST requests
     *
     * @param accessToken        The clientId that is provided by Kuveyt Türk API market when an application
     *                           is created.
     * @param privateKeyAsString The RSA private key as a string object.
     * @param jsonBody           The content of the request body in JSON format as a String object.
     * @return The base64 encoded signature by using SHA256/RSA
     */
    public static String generateSignatureForPostRequest(String accessToken,
                                                         String privateKeyAsString,
                                                         String jsonBody) throws SignatureGenerationException {
        String input = accessToken + jsonBody;
        String base64Signature = null;

        try {
            PrivateKey privateKey = buildPrivateKeyFromString(privateKeyAsString);
            base64Signature = signSHA256RSA(input, privateKey);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            SignatureGenerationException signatureEx = new SignatureGenerationException(msg, e);
            throw signatureEx;
        }
        return base64Signature;
    }

    /**
     * Utility method for generating base64 encoded signature by using SHA256/RSA.
     *
     * @param input      The string object upon which hashing is to be applied.
     * @param privateKey The RSA private key
     * @return The base64 encoded signature by using SHA256/RSA
     */
    private static String signSHA256RSA(String input,
                                        PrivateKey privateKey) throws Exception {
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initSign(privateKey);
        s.update(input.getBytes("UTF-8"));
        byte[] signature = s.sign();
        //Base64.getEncoder().encode("Test".getBytes());
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Utility method for generating the query parameter string
     *
     * @param queryParams The query parameter names and values as an HashMap object
     * @return Query parameters as string (e.g. ?param1=1&param2=2)
     */
    private static String getQueryParamsString(List<QueryParameterBean> queryParams) throws SignatureGenerationException {
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            int count = 0;
            for (QueryParameterBean e : queryParams) {
                count++;
                sb.append(e.getParamName()).append("=").append(e.getParamValue().toString());
                if (count != queryParams.size()) {
                    sb.append("&");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Utility method for converting the private key in text format into the binary format
     *
     * @param publicKeyAsString The RSA private key as a string object.
     * @return Public key as an instance of java.security.PublicKey
     */
    private static PublicKey buildPublicKeyFromString(String publicKeyAsString) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException {
        String publicKeyStrWithoutHeaderFooter = publicKeyAsString.
                replaceAll("-----BEGIN PUBLIC KEY-----\n", "").
                replaceAll("-----END PUBLIC KEY-----\n", "").
                replaceAll("\n", "");
        ;
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStrWithoutHeaderFooter.getBytes("UTF-8"));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    /**
     * Utility method for converting the private key in text format into the binary format
     *
     * @param privateKeyAsString The RSA private key as a string object.
     * @return Private key as an instance of java.security.PrivateKey
     */
    private static PrivateKey buildPrivateKeyFromString(String privateKeyAsString) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException {
        String privateKeyStrWithoutHeaderFooter = privateKeyAsString.
                replaceAll("-----BEGIN PRIVATE KEY-----", "").
                replaceAll("-----END PRIVATE KEY-----", "").
                replaceAll("\n", "");
        byte[] privateKeyBytes =
                Base64.getDecoder().decode(privateKeyStrWithoutHeaderFooter.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(keySpec);
    }
}

