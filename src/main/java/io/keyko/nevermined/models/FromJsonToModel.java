package io.keyko.nevermined.models;

import java.io.IOException;

public interface FromJsonToModel {

    static AbstractModel convertToModel(String json) throws IOException {
        throw new UnsupportedOperationException();
    }

    ;

}
