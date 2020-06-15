/*
 * Copyright (c) 2020
 * KUVEYT TÜRK PARTICIPATION BANK INC.
 *
 * Author: Fikri Aydemir
 *
 * Project: API Request SignatureGenerator
 */

package tr.com.kuveytturk.api.signature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The utility class containing the list of query parameters
 *
 * @author      Fikri Aydemir
 * @version     1.0
 * @since       2020-01-12
 */
public class QueryParameterListBean {
    private TreeMap<String, Object> backMap;

    public QueryParameterListBean() {
        this.backMap = new TreeMap<>();
    }

    public QueryParameterListBean(String apiEndpointUrl) throws SignatureGenerationException {
        this();

        if(!apiEndpointUrl.contains("?")){
            return;
        }

        String[] urlTokens = apiEndpointUrl.split("\\?");
        if(urlTokens.length == 2){
            String parameterSequence = urlTokens[1];
            String[] parameterPairs = parameterSequence.split("&");

            if(parameterPairs.length > 0){
                for(String paramPair : parameterPairs){
                    String[] paramTokens = paramPair.split("=");
                    if(paramTokens.length == 2){
                        String paramName = paramTokens[0];
                        String paramValue = paramTokens[1];
                        QueryParameterBean parameterBean = new QueryParameterBean(paramName, paramValue);
                        this.add(parameterBean);
                    } else {
                        String errMsg = "Invalid parameter format has been detected in the query parameter: " + paramPair;
                        SignatureGenerationException exp = new SignatureGenerationException(errMsg);
                        throw exp;
                    }
                }
            } else {
                String errMsg = "Query parameters must be provided after the question mark!";
                SignatureGenerationException exp = new SignatureGenerationException(errMsg);
                throw exp;
            }
        } else {
            String errMsg = "Endpoint url is in invalid format. It must contain zero or only one question mark!";
            SignatureGenerationException exp = new SignatureGenerationException(errMsg);
            throw exp;
        }

    }

    public QueryParameterListBean add(QueryParameterBean bean) {
        if (bean != null) {
            backMap.put(bean.getParamName(), bean.getParamValue());
        }
        return this;
    }

    public QueryParameterListBean remove(String key) {
        backMap.remove(key);
        return this;
    }

    public QueryParameterListBean clear() {
        backMap.clear();
        return this;
    }

    public boolean isEmpty(){
        return backMap.isEmpty();
    }

    public Map<String, Object> toMap(){
        return backMap;
    }

    public List<QueryParameterBean> toList(){
        List<QueryParameterBean> theList = new ArrayList<>();
        backMap.forEach( (k,v)-> {
            theList.add(new QueryParameterBean(k, String.valueOf(v)));
        });
        return theList;
    }

    @Override
    public String toString()  {
        List<QueryParameterBean> queryParams = this.toList();

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

}
