# simple-http-client
Very simple http client based on apache's http core and http client utils.

Example of GET request with proxy settings and CERT / SSL config:

        String uri = "https://httpbin.org";
        String proxyUrl = "http://192.168.2.10:8080";

        SimpleHttpResponse response = SimpleHttpClient.newGet(uri)
                .withProxy(proxyUrl)
                .withSslConfig(SslConfig.create(new File("cert/mykeystore.jks"), "changeit"))
                .execute();

        System.out.println(response);

