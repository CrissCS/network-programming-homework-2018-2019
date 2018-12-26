package com.fmi.mpr.hw.http.server.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;

public class RequestToResponseConverter {
  private RequestHeaderValueExtractor requestHeaderValueExtractor;

  public RequestToResponseConverter(RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }

  public FileInputStream getResponse(String request) throws IOException {
    String methodName = requestHeaderValueExtractor.getRequestMethod(request);
    FileInputStream response = null;
    if (methodName.toLowerCase().equals("get")) {
      response = processGetRequest(request);
    }
    else if (methodName.toLowerCase().equals("post")) {
      //response = processPostRequest(request);
    }
    return response;
  }

  public FileInputStream processGetRequest(String request) throws IOException {
    String fileName = requestHeaderValueExtractor.getFullFileName(request);
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
}