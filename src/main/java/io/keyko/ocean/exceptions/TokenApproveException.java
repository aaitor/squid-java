/*
 * Copyright 2018 Ocean Protocol Foundation
 * SPDX-License-Identifier: Apache-2.0
 */

package io.keyko.ocean.exceptions;

public class TokenApproveException extends OceanException {

    public TokenApproveException(String message, Throwable e) {
        super(message, e);
    }

    public TokenApproveException(String message) {
        super(message);
    }
}
