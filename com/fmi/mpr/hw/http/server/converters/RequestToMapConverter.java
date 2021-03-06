package com.fmi.mpr.hw.http.server.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestToMapConverter {
  public Map<String, String> convert(String request) {

    Map<String, String> requestHeaders = new HashMap<>();
    
    Pattern pattern = Pattern.compile("\\s*^\\s*$\\s*", Pattern.MULTILINE);
    String header = pattern.split(request)[0];
    String[] requestLines = header.split("\n");
    for (String requestLine : requestLines) {
      if (requestLine.equals("\r")) {
        continue;
      }
      String key = requestLine.substring(0, requestLine.indexOf(" "));
      String value = requestLine.substring(requestLine.indexOf(" ") + 1);
      if (key.equals("GET") || key.equals("POST") || key.equals("PUT") || key.equals("DELETE")) {
        requestHeaders.put(key.toLowerCase(), value);
      } else {
        requestHeaders.put(key.substring(0, key.length() - 1).toLowerCase(), value);
      }
    }

    return requestHeaders;
  }
}
