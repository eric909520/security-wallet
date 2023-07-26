package org.secwallet.core.cache;

public interface KeyPrefix {

    int expireSeconds();
    String getPrefix();

}
