package com.fmi.mpr.hw.http.server.processors;

import java.io.PrintStream;

public interface ResponseProcessor {
  String process(PrintStream output, String request);
}
