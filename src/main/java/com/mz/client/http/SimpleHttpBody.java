package com.mz.client.http;

public class SimpleHttpBody {
    private String body;

    /**
     * Creates a new Http body instance.
     * 
     * @param body Body content.
     */
    public SimpleHttpBody(String body) {
        this.body = body;
    }

    /**
     * Checks if body has content.
     * 
     * @return True if body has content.
     */
    public boolean isPresent() {
        return body != null;
    }

    /**
     * Gets body content.
     * 
     * @return body content, null if there's no content.
     */
    public String value() {
        return body;
    }

    /**
     * Alias for {@link SimpleHttpBody#value()}.
     * 
     * @return body content, null if there's no content.
     */
    public String get() {
        return value();
    }

    @Override
    public String toString() {
        return "SimpleHttpBody{" + "body='" + body + '\'' + '}';
    }
}
