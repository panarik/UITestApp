package com.github.panarik.uitestapp.mockOkhttp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class OkHttpJavaBase {

    //server setup
    MockWebServer server = new MockWebServer();
    HttpUrl serverUrl;
    MockResponse response = new MockResponse();

    //client setup
    OkHttpClient client = new OkHttpClient();
    String responseBody;

    @Before
            public void setupMockServer() throws Exception {
        //run server
        server.start();

        //configure server
        serverUrl = server.url("api/test"); //set server path
        server.enqueue(response
                .setBody("test body") //set response body
                .setResponseCode(200) //set response code
        );
    }


    @Test
    public void testOkHttp() throws Exception {

        //run Get Request
        responseBody = getRequest(client, serverUrl);

        //matcher
        Assert.assertEquals("test body", responseBody); //body equals
    }

    @Test
    public void testTimeouts() throws Exception {

        //configure client for set timeout
        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build();

        //run Get Request
        responseBody = getRequest(client, serverUrl);

        //matcher
        Assert.assertEquals("test body", responseBody); //body equals

    }

    @After
    public void stopMockServer() throws Exception {
        server.shutdown();
    }


    /* method with parameters:
       - client - for GET request object
       - serverUrl - for configure server URL of GET request
    */
    public String getRequest(OkHttpClient client, HttpUrl serverUrl) throws Exception {

        //run client request
        Request request = new Request.Builder()
                .url(serverUrl) //configure server URL of GET request
                .build(); //run request

        //run server response
        try (Response response = client.newCall(request).execute()){
            return response.body().string();
        }

    }

}
