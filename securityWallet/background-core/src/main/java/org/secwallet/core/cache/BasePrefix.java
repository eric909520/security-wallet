package org.secwallet.core.cache;

public class BasePrefix implements KeyPrefix{
    private Long expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0L, prefix);
    }

    public BasePrefix(Long expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public Long getExpireSeconds() {
        return this.expireSeconds;
    }

    public int expireSeconds() {
        return 0;
    }

    public String getPrefix() {
        String className = this.getClass().getSimpleName();
        return className + ":" + this.prefix;
    }
}
