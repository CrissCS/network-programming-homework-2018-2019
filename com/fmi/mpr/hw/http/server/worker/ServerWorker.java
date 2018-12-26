package com.fmi.mpr.hw.http.server.worker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;
import com.fmi.mpr.hw.http.server.converters.RequestToStringConverter;
import com.fmi.mpr.hw.http.server.processors.ResponseProcessor;

public class ServerWorker implements Runnable {

  private Socket clientSocket;
  private RequestToStringConverter requestConverter;
  private ResponseProcessor responseProcessor;

  public ServerWorker(Socket clientSocket, RequestToStringConverter requestConverter,
      ResponseProcessor responseProcessor) {
    this.clientSocket = clientSocket;
    this.requestConverter = requestConverter;
    this.responseProcessor = responseProcessor;
  }

  @Override
  public void run() {
    if (clientSocket != null) {
      String request = null;

      try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
          PrintStream out = new PrintStream(clientSocket.getOutputStream())) {
        request = requestConverter.convert(in);
        responseProcessor.process(out, request);
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
}