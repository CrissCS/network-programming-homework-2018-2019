package com.fmi.mpr.hw.http.server.converters;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;

public class RequestToResponseConverter {
  private RequestHeaderValueExtractor requestHeaderValueExtractor;

  public RequestToResponseConverter(RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }

  public InputStream getResponse(String request) throws IOException {
    String methodName = requestHeaderValueExtractor.getRequestMethod(request);
    InputStream response = null;
    if (methodName.toLowerCase().equals("get")) {
      response = processGetRequest(request);
    } else if (methodName.toLowerCase().equals("post")) {
      response = processPostRequest(request);
    }
    return response;
  }

  private InputStream processPostRequest(String request) throws IOException {
    String content = requestHeaderValueExtractor.getContent(request);
    String fileExtension = requestHeaderValueExtractor.getContentTypeExtension(request);
    File resourceFolder = new File(System.getProperty("user.dir") + File.separator + "resources");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm_ss_SSS");
    File file = new File(resourceFolder + File.separator + simpleDateFormat.format(new Date()) + "."
        + fileExtension);
    file.createNewFile();
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
    bufferedWriter.write(content);
    bufferedWriter.close();
    String fileNameResponseBody = "file-name: " + file.getName();
    InputStream fileNameInputStream = new ByteArrayInputStream(fileNameResponseBody.getBytes());
    FileInputStream fis = new FileInputStream(file.getPath());
    SequenceInputStream sequenceInputStream = new SequenceInputStream(fileNameInputStream, fis);
    return sequenceInputStream;
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
