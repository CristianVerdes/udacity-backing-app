package com.example.myretrofit.jsonparsers;

/**
 * Created by cristian.verdes on 05.03.2018.
 */

public abstract class JsonParser {
    String queryResponse;

    public void setQueryResponse(Object queryResponse) {
        this.queryResponse = (String) queryResponse;
    }
}
