package com.mz.client.http;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "get")
public class SimpleHttpGet extends AbstractMojo {

  @Parameter(property = "url", required = true)
  private String url;

  public void execute() throws MojoExecutionException {
    SimpleHttpResponse response = SimpleHttpClient.newGet(url).execute();
    SimpleHttpBody body = response.getBody();
    if (body.isPresent()) {
      getLog().info("BODY: " + body.get());
    }
    int statusCode = response.getStatusCode();

    getLog().info("Hello, world.");
  }
}