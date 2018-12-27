package com.fmi.mpr.hw.http.server.builder;

import java.io.InputStream;

public interface ResponseBuilder {
  InputStream build(String request);
}
