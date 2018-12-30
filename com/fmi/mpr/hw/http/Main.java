package com.fmi.mpr.hw.http;

import java.io.IOException;
import java.net.ServerSocket;
import com.fmi.mpr.hw.http.server.HttpServer;

public class Main {

  public static void main(String[] args) throws IOException {
    new HttpServer(new ServerSocket(8080), 6).start();
  }
}
