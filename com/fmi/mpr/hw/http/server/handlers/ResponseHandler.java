package com.fmi.mpr.hw.http.server.handlers;

import java.io.PrintStream;

public interface ResponseHandler {
  String handle(PrintStream output, String request);
}
