package com.fmi.mpr.hw.http.server;

import java.util.Map;
import java.util.regex.Pattern;
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
    Map<String, String> headers = requestToMapConverter.convert(request);
    String fileNameWithExtension = headers.get("get").split("\\s+")[0].substring(1).trim();
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

  // TODO Fix it so it doesn't split the body as well.
  public String getContent(String request) {
    Pattern pattern = Pattern.compile("\\s*^\\s*$\\s*", Pattern.MULTILINE);
    String content = pattern.split(request)[1];
    return content;
  }

  public String getContentTypeExtension(String request) {
    String contentType = extract("content-type", request);
    String extension = contentType.split("\\/")[1];
    extension = extension.replaceAll("\\r", "");
    return extension;
  }

  public String getContentTypeHeader(String request) {
    String requestMethod = getRequestMethod(request);
    if (requestMethod.equals("get")) {
      return getContentType(request);
    } else if (requestMethod.equals("post")) {
      return contentTypes.get(getContentTypeExtension(request));
    } else {
      return null;
    }
  }
}
