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
 * The utility class containing query parameter name and values
 *
 * @author      Fikri Aydemir
 * @version     1.0
 * @since       2020-01-12
 */
public class QueryParameterBean implements java.io.Serializable {
    private String paramName;
    private String paramValue;

    public QueryParameterBean() {
    }

    public QueryParameterBean(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
