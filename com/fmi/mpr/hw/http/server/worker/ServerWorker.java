package com.fmi.mpr.hw.http.server.worker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import com.fmi.mpr.hw.http.server.converters.RequestToStringConverter;
import com.fmi.mpr.hw.http.server.extractors.RequestHeaderValueExtractor;
import com.fmi.mpr.hw.http.server.handlers.ResponseHandler;

public class ServerWorker implements Runnable {

  private Socket clientSocket;
  private RequestToStringConverter requestConverter;
  private ResponseHandler responseHandler;

  public ServerWorker(Socket clientSocket, RequestToStringConverter requestConverter,
      ResponseHandler responseHandler) {
    this.clientSocket = clientSocket;
    this.requestConverter = requestConverter;
    this.responseHandler = responseHandler;
  }

  @Override
  public void run() {
    if (clientSocket != null) {
      String request = null;

      try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
          PrintStream out = new PrintStream(clientSocket.getOutputStream())) {
        // Convert the request from InputStream to String
        request = requestConverter.convert(in);

        // Handle and process the response
        responseHandler.handle(out, request);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (clientSocket != null) {
          try {
            clientSocket.close();
            System.out.println("Client socket has been closed. " + clientSocket.getInetAddress());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public Socket getClientSocket() {
    return clientSocket;
  }

  public void setClientSocket(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public RequestToStringConverter getRequestConverter() {
    return requestConverter;
  }

  public void setRequestConverter(RequestToStringConverter requestConverter) {
    this.requestConverter = requestConverter;
  }

  public ResponseHandler getResponseProcessor() {
    return responseHandler;
  }

  public void setResponseProcessor(ResponseHandler responseProcessor) {
    this.responseHandler = responseProcessor;
  }

}
