package com.fmi.mpr.hw.http.server.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import com.fmi.mpr.hw.http.server.builder.ResponseBuilder;

public class DefaultResponseProcessor implements ResponseProcessor {
  private ResponseBuilder responseBuilder;

  public DefaultResponseProcessor(ResponseBuilder responseBuilder) {
    this.responseBuilder = responseBuilder;
  }

  @Override
  public String process(PrintStream output, String request) {
    StringBuilder fullResponse = new StringBuilder();
    InputStream response = responseBuilder.build(request);
    try {
      byte[] buffer = new byte[8192];

      int bytesRead = 0;
      while ((bytesRead = response.read(buffer, 0, 8192)) > 0) {
        output.write(buffer, 0, bytesRead);
        fullResponse.append(new String(buffer, 0, bytesRead));
      }
      output.flush();
    } catch (IOException e) {
      fullResponse.setLength(0);
      fullResponse.append("HTTP/1.1 404 Not Found\\r\\n");
      output.print(fullResponse.toString());
      output.flush();
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
        }
      }
    }
    System.err.println(fullResponse.toString());
    return fullResponse.toString();
  }
}
