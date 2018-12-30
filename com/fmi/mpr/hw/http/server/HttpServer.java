package com.fmi.mpr.hw.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fmi.mpr.hw.http.server.converters.RequestToStringConverter;
import com.fmi.mpr.hw.http.server.handlers.DefaultResponseHandler;
import com.fmi.mpr.hw.http.server.handlers.GetRequestHandler;
import com.fmi.mpr.hw.http.server.handlers.HttpRequestHandler;
import com.fmi.mpr.hw.http.server.handlers.PostRequestHandler;
import com.fmi.mpr.hw.http.server.handlers.ResponseHandler;
import com.fmi.mpr.hw.http.server.builder.DefaultResponseBuilder;
import com.fmi.mpr.hw.http.server.builder.ResponseBuilder;
import com.fmi.mpr.hw.http.server.converters.RequestToMapConverter;
import com.fmi.mpr.hw.http.server.converters.RequestToResponseConverter;
import com.fmi.mpr.hw.http.server.worker.ServerWorker;

public class HttpServer {
  private ServerSocket serverSocket;
  private boolean isRunning;
  private ExecutorService executorService;
  private final int THREAD_NUMBER;

  public HttpServer(ServerSocket serverSocket, int threadNumber) {
    this.serverSocket = serverSocket;
    this.THREAD_NUMBER = threadNumber;
    executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
  }

  public void start() {

    if (!isRunning) {
      this.isRunning = true;
      System.out.println("Server startup on port " + serverSocket.getLocalPort());
      run();
    }
  }

  private void run() {
    while (isRunning) {
      try {
        listen();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    executorService.shutdown();
  }

  private void listen() {
    Socket clientSocket = null;

    try {
      clientSocket = serverSocket.accept();
      System.out.println("Accepted connection from client : " + clientSocket.getInetAddress());

      // ContentTypes
      Map<String, String> contentTypes = new HashMap<>();
      // Text
      contentTypes.put("plain", "Content-type: text/plain");
      contentTypes.put("txt", "Content-type: text/plain");
      contentTypes.put("css", "Content-type: text/css");
      contentTypes.put("html", "Content-type: text/html");
      // Images
      contentTypes.put("gif", "Content-type: image/gif");
      contentTypes.put("png", "Content-type: image/png");
      contentTypes.put("jpeg", "Content-type: image/jpeg");
      // Videos
      contentTypes.put("mp4", "Content-type: video/mp4");

      // Request Handlers declaration
      Map<String, HttpRequestHandler> requestHandlers = new HashMap<>();

      RequestToStringConverter requestConverter = new RequestToStringConverter();
      RequestToMapConverter requestToMapConverter = new RequestToMapConverter();

      // Value Extractor
      RequestHeaderValueExtractor requestHeaderValueExtractor = new RequestHeaderValueExtractor();
      requestHeaderValueExtractor.setContentTypes(contentTypes);
      requestHeaderValueExtractor.setRequestToMapConverter(requestToMapConverter);

      RequestToResponseConverter requestToResponseConverter = new RequestToResponseConverter();
      requestToResponseConverter.setRequestHandlers(requestHandlers);
      requestToResponseConverter.setRequestHeaderValueExtractor(requestHeaderValueExtractor);

      ResponseBuilder responseBuilder =
          new DefaultResponseBuilder(requestToResponseConverter, requestHeaderValueExtractor);
      ResponseHandler responseProcessor = new DefaultResponseHandler(responseBuilder);

      // HttpRequestHandlers
      GetRequestHandler getRequestHandler = new GetRequestHandler();
      getRequestHandler.setRequestHeaderValueExtractor(requestHeaderValueExtractor);

      PostRequestHandler postRequestHandler = new PostRequestHandler();
      postRequestHandler.setRequestHeaderValueExtractor(requestHeaderValueExtractor);

      // Add request handlers
      requestHandlers.put("get", getRequestHandler);
      requestHandlers.put("post", postRequestHandler);

      executorService.execute(new ServerWorker(clientSocket, requestConverter, responseProcessor));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // public static void main(String[] args) throws IOException {
  // new HttpServer(new ServerSocket(8080), 6).start();
  // }
}
