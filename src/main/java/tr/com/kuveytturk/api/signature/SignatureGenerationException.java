/*
 * Copyright (c) 2020
 * KUVEYT TÜRK PARTICIPATION BANK INC.
 *
 * Author: Fikri Aydemir
 *
 * Project: API Request SignatureGenerator
 */

package tr.com.kuveytturk.api.signature;

/**
 * Custom exception class that can be thrown during signature generation
 *
 * @author      Fikri Aydemir
 * @version     1.0
 * @since       2018-04-18
 */
public final class SignatureGenerationException extends Exception {

    /**
     * Constructor
     *
     * @param  message Holds the error message
     * @param  e Holds the original exception object
     */
    public SignatureGenerationException(String message, Exception e){
        super(message,e);
    }

    /**
     * Constructor
     *
     * @param  message Holds the error message
     */
    public SignatureGenerationException(String message){
        super(message);
    }


    @Override
    public String toString() {
        return "GetRequestExecutionException{" +
                " message='" + super.getMessage() + '\'' +
                '}';
    }

}
