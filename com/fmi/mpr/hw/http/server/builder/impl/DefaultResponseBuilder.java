package com.fmi.mpr.hw.http.server.builder.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Map;
import com.fmi.mpr.hw.http.server.builder.ResponseBuilder;
import com.fmi.mpr.hw.http.server.converters.RequestToResponseConverter;
import com.fmi.mpr.hw.http.server.extractors.RequestHeaderValueExtractor;

public class DefaultResponseBuilder implements ResponseBuilder {

  private RequestToResponseConverter requestToResponseConverter;
  private RequestHeaderValueExtractor requestHeaderValueExtractor;

  public DefaultResponseBuilder(RequestToResponseConverter requestToResponseConverter,
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestToResponseConverter = requestToResponseConverter;
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }

  @Override
  public InputStream build(String request) {
    StringBuilder fullResponse = new StringBuilder();
    InputStream responseBody = null;
    try {
      responseBody = requestToResponseConverter.getResponse(request);
      fullResponse.append("HTTP/1.1 200 OK\r\n");
      String contentTypeHeader = requestHeaderValueExtractor.getContentTypeHeader(request);
      if (contentTypeHeader != null) {
        fullResponse.append(contentTypeHeader + "\r\n");
      }
      fullResponse.append("\r\n");
    } catch (IOException e) {
      fullResponse.setLength(0);
      fullResponse.append("HTTP/1.1 404 Not Found\r\n");
    }
    InputStream headerInputStream = new ByteArrayInputStream(fullResponse.toString().getBytes());
    SequenceInputStream sequenceInputStream =
        new SequenceInputStream(headerInputStream, responseBody);

    return sequenceInputStream;
  }

  public RequestToResponseConverter getRequestToResponseConverter() {
    return requestToResponseConverter;
  }

  public void setRequestToResponseConverter(RequestToResponseConverter requestToResponseConverter) {
    this.requestToResponseConverter = requestToResponseConverter;
  }

  public RequestHeaderValueExtractor getRequestHeaderValueExtractor() {
    return requestHeaderValueExtractor;
  }

  public void setRequestHeaderValueExtractor(
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }

}
