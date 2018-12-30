package com.fmi.mpr.hw.http.server.handlers;

import java.io.IOException;
import java.io.InputStream;

public interface HttpRequestHandler {
  InputStream handle(String request) throws IOException;
}
