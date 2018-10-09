# simple-http-client
Very simple http client based on apache's http core and http client utils.

Example of POST request with CERT / SSL config:

    String truststore = new File("was7_cert/truststore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
    String truststorePassword = "changeit";

    String keystore = new File("was7_cert/keystore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
    String keystorePassword = "changeit";

    String uri = "https://wks0393.local:9443/hello/HelloWorldService";

    SslConfig sslConfig = SslConfig.newConfig()
            .withTruststore(new File(truststore), truststorePassword)
            .withKeystore(new File(keystore), keystorePassword)
            .build();

    String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.ex.mz/\"> <soapenv:Header/> <soapenv:Body> <ws:salute> <!--Optional:--> <arg0>martin</arg0> </ws:salute> </soapenv:Body></soapenv:Envelope>";

    SimpleHttpResponse response = SimpleHttpClient.newPost(uri)
            .withSslConfig(sslConfig)
            .withContentType("application/xml")
            .withBody(body)
            .execute();

    System.out.println(response);


Example of POST request with application/json data type:

        SimpleHttpResponse response = SimpleHttpClient.newPost("http://someurl.com")
                .withBasicAuth("username" , "password")
                .withHeader("Accept" , "application/json")
                .withBody( "{ \"id\":1 , \"value\":\"foo\" }" )
                .withContentType("application/json")
                .execute();

        System.out.println(response.getBody());
        
Example of GET request with proxy settings:

    String uri = "https://httpbin.org";
    String proxyUrl = "http://192.168.2.10:8080";

    File truststore = new File("cert/mykeystore.jks");
    String truststorePassword = "changeit";

    SimpleHttpResponse response = SimpleHttpClient.newGet(uri)
            .withProxy(proxyUrl)
            .withSslConfig(SslConfig.newConfig().withTruststore(truststore, truststorePassword).build())
            .execute();

    System.out.println(response);

