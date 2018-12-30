package com.fmi.mpr.hw.http.server.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;

public class GetRequestHandler implements HttpRequestHandler {
  private RequestHeaderValueExtractor requestHeaderValueExtractor;
  
  @Override
  public InputStream handle(String request) throws IOException {
    String fileName = requestHeaderValueExtractor.getFullFileNameFromRequestHeader(request);
    FileInputStream file = getFileByName(fileName);
    return file;
  }

  public FileInputStream getFileByName(String fileName) throws FileNotFoundException {
    File file = new File(
        System.getProperty("user.dir") + File.separator + "resources" + File.separator + fileName);
    String path = file.getAbsolutePath();
    FileInputStream fileInputStream = new FileInputStream(path);
    return fileInputStream;
  }

  public RequestHeaderValueExtractor getRequestHeaderValueExtractor() {
    return requestHeaderValueExtractor;
  }

  public void setRequestHeaderValueExtractor(
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }
}
