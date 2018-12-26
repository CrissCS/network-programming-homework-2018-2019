package com.fmi.mpr.hw.http.server.converters;

import java.io.IOException;
import java.io.InputStream;

public class RequestToStringConverter {
  public String convert(InputStream input) throws IOException {
    if (input != null) {
      StringBuilder request = new StringBuilder();

      byte[] buffer = new byte[1024];
      int bytesRead = 0;

      while ((bytesRead = input.read(buffer, 0, 1024)) > 0) {
        request.append(new String(buffer, 0, bytesRead));

        if (bytesRead < 1024) {
          break;
        }
      }
      String requestString = request.toString();
      if (requestString.length() == 0) {
        throw new IOException("Could not read input.");
      }
      return requestString;
    }
    throw new IOException("Could not read input. Input stream can not be null.");
  }
}
