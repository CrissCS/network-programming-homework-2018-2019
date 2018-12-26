package com.fmi.mpr.hw.http.server.processors;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;
import com.fmi.mpr.hw.http.server.converters.RequestToResponseConverter;

public class DefaultResponseProcessor implements ResponseProcessor {
  private RequestToResponseConverter requestToResponseConverter;
  private RequestHeaderValueExtractor requestHeaderValueExtractor;

  public DefaultResponseProcessor(RequestToResponseConverter requestToResponseConverter,
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestToResponseConverter = requestToResponseConverter;
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }

  @Override
  public String process(PrintStream output, String request) {
    StringBuilder fullResponse = new StringBuilder();
    try {
      FileInputStream response = requestToResponseConverter.getResponse(request);
      output.println("HTTP/1.1 OK");
      String contentType = requestHeaderValueExtractor.getContentType(request);
      if (contentType != null) {
        output.println(contentType);
      }
      output.println();
      byte[] buffer = new byte[8192];

      int bytesRead = 0;
      while ((bytesRead = response.read(buffer, 0, 8192)) > 0) {
        output.write(buffer, 0, bytesRead);
      }
      output.flush();
    } catch (IOException e) {
      fullResponse.append("HTTP/1.1 404 Not Found\\r\\n");
      output.print(fullResponse.toString());
      output.flush();
    }
    return fullResponse.toString();
  }
}