package com.fmi.mpr.hw.http.server.extractors;

import java.util.Map;
import java.util.regex.Pattern;
import com.fmi.mpr.hw.http.server.converters.RequestToMapConverter;

public class RequestHeaderValueExtractor {
  private RequestToMapConverter requestToMapConverter;
  private Map<String, String> contentTypes;

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

  // Headers
  // Get /index.html ....
  // Will return index.html
  public String getFullFileNameFromRequestHeader(String request) {
    Map<String, String> headers = requestToMapConverter.convert(request);
    String fileNameWithExtension = headers.get("get").split("\\s+")[0].substring(1).trim();
    return fileNameWithExtension;
  }

  // Headers
  // Get /index.html ....
  // Will return .html
  public String getFileExtensionFromFileNameInHeader(String request) {
    String fileName = getFullFileNameFromRequestHeader(request);
    String[] fileNameAndExtension = fileName.split("\\.");
    if (fileNameAndExtension.length == 2) {
      return fileNameAndExtension[1];
    } else {
      return null;
    }
  }

  // Will return whole Content-type string from header
  public String getContentTypeFromRequestHeader(String request) {
    String extension = getFileExtensionFromFileNameInHeader(request);
    String contentType = contentTypes.get(extension);
    return contentType;
  }

  // Method splits request header and request body based on the blank line between them.
  // Post / ...
  // Content-Type: ....
  //
  // <html><body> Hello </body></html>
  public String getContent(String request) {
    Pattern pattern = Pattern.compile("\\s*^\\s*$\\s*", Pattern.MULTILINE);
    String[] splitContent = pattern.split(request);
    StringBuilder content = new StringBuilder();
    for (int i = 1; i < splitContent.length; i++) {
      content.append(splitContent[i]);
      if (i != splitContent.length - 1) {
        content.append("\n\n");
      }
    }
    return content.toString();
  }

  // Method returns file extension based on the content-type header
  // Content-Type: text/html
  // method will return html
  public String getFileExtensionFromRequestHeader(String request) {
    String contentType = extract("content-type", request);
    String extension = contentType.split("\\/")[1];
    extension = extension.replaceAll("\\r", "");
    return extension;
  }

  // Method returns file extension based on the content-type header
  // Content-Type: text/plain
  // method will return txt
  public String getFileExtensionForSaving(String request) {
    String contentType = extract("content-type", request);
    String extension = contentType.split("\\/")[1];
    extension = extension.replaceAll("\\r", "");
    if (extension.equals("plain")) {
      return "txt";
    } else {
      return extension;
    }
  }

  //
  public String getContentTypeHeader(String request) {
    String requestMethod = getRequestMethod(request);
    if (requestMethod.equals("get")) {
      return getContentTypeFromRequestHeader(request);
    } else if (requestMethod.equals("post")) {
      return contentTypes.get("plain");
    } else {
      return null;
    }
  }

  public boolean isImage(String request) {
    String contentTypeExtension = getFileExtensionFromRequestHeader(request);
    if (contentTypeExtension.equals("jpg") || contentTypeExtension.equals("jpeg")
        || contentTypeExtension.equals("png") || contentTypeExtension.equals("gif")) {
      return true;
    }
    return false;
  }

  public boolean isText(String request) {
    String contentTypeExtension = getFileExtensionFromRequestHeader(request);
    if (contentTypeExtension.equals("plain") || contentTypeExtension.equals("html")
        || contentTypeExtension.equals("css") || contentTypeExtension.equals("txt")) {
      return true;
    }
    return false;
  }

  public RequestToMapConverter getRequestToMapConverter() {
    return requestToMapConverter;
  }

  public void setRequestToMapConverter(RequestToMapConverter requestToMapConverter) {
    this.requestToMapConverter = requestToMapConverter;
  }

  public Map<String, String> getContentTypes() {
    return contentTypes;
  }

  public void setContentTypes(Map<String, String> contentTypes) {
    this.contentTypes = contentTypes;
  }

}
