package com.fmi.mpr.hw.http.server;

import java.util.Map;
import com.fmi.mpr.hw.http.server.converters.RequestToMapConverter;

public class RequestHeaderValueExtractor {
  private RequestToMapConverter requestToMapConverter;
  private Map<String, String> contentTypes;

  public RequestHeaderValueExtractor(RequestToMapConverter requestToMapConverter,
      Map<String, String> contentTypes) {
    this.requestToMapConverter = requestToMapConverter;
    this.contentTypes = contentTypes;
  }

  public String extract(String key, String request) {
    Map<String, String> headers = requestToMapConverter.convert(request);
    String value = headers.get(key.toLowerCase());
    return value;
  }

  public String getRequestMethod(String request) {
    String requestMethod = null;
    if (request.length() == 0 || request != null) {
      Map<String, String> headers = requestToMapConverter.convert(request);
      if (headers.containsKey("get")) {
        requestMethod = "get";
      } else if (headers.containsKey("post")) {
        requestMethod = "post";
      }
    }
    return requestMethod;
  }

  public String getFullFileName(String request) {
    String[] requestLines = request.split("\n");
    String firstLine = requestLines[0];
    String[] firstLineArguments = firstLine.split("\\s+");
    String fileNameWithExtension = firstLineArguments[1].substring(1);
    return fileNameWithExtension;
  }

  public String getFileExtension(String request) {
    String fileName = getFullFileName(request);
    String[] fileNameAndExtension = fileName.split("\\.");
    if (fileNameAndExtension.length == 2) {
      return fileNameAndExtension[1];
    } else {
      return null;
    }
  }

  public String getContentType(String request) {
    String extension = getFileExtension(request);
    String contentType = contentTypes.get(extension);
    return contentType;
  }

}
