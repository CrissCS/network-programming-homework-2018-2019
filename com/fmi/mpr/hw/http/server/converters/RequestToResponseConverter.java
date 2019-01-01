package com.fmi.mpr.hw.http.server.converters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.fmi.mpr.hw.http.server.extractors.RequestHeaderValueExtractor;
import com.fmi.mpr.hw.http.server.handlers.HttpRequestHandler;

public class RequestToResponseConverter {
  private RequestHeaderValueExtractor requestHeaderValueExtractor;
  private Map<String, HttpRequestHandler> requestHandlers;

  public InputStream getResponse(String request) throws IOException {
    String methodName = requestHeaderValueExtractor.getRequestMethod(request);
    return requestHandlers.get(methodName.toLowerCase()).handle(request);
  }

  public Map<String, HttpRequestHandler> getRequestHandlers() {
    return requestHandlers;
  }

  public void setRequestHandlers(Map<String, HttpRequestHandler> requestHandlers) {
    this.requestHandlers = requestHandlers;
  }

  public RequestHeaderValueExtractor getRequestHeaderValueExtractor() {
    return requestHeaderValueExtractor;
  }

  public void setRequestHeaderValueExtractor(
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }
}
