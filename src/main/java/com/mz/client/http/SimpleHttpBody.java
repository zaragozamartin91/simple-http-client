package com.mz.client.http;

public class SimpleHttpBody {
    private String body;

    public SimpleHttpBody(String body) {
        this.body = body;
    }

    public boolean isPresent() {
        return body != null;
    }

    public String value() {
        return body;
    }

    @Override public String toString() {
        return "SimpleHttpBody{" +
                "body='" + body + '\'' +
                '}';
    }
}
