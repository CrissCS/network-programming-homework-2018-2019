package com.fmi.mpr.hw.http.server.handlers;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import com.fmi.mpr.hw.http.server.RequestHeaderValueExtractor;
import sun.misc.BASE64Decoder;

public class PostRequestHandler implements HttpRequestHandler {
  private RequestHeaderValueExtractor requestHeaderValueExtractor;

  @Override
  public InputStream handle(String request) throws IOException {
    String content = requestHeaderValueExtractor.getContent(request);
    File savedFile = saveToFile(request, content);
    String fileNameResponseBody = "file-name: " + savedFile.getName();
    InputStream fileNameInputStream = new ByteArrayInputStream(fileNameResponseBody.getBytes());
    // FileInputStream fis = new FileInputStream(savedFile.getPath());
    // SequenceInputStream sequenceInputStream = new SequenceInputStream(fileNameInputStream, fis);
    return fileNameInputStream;
  }

  public File saveToFile(String request, String content) throws IOException {
    ByteArrayInputStream byteArrayInputStream = null;
    BufferedWriter bufferedWriter = null;
    File savedFile = null;

    try {
      String fileExtension = requestHeaderValueExtractor.getFileExtensionForSaving(request);
      File resourceFolder = new File(System.getProperty("user.dir") + File.separator + "resources");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm_ss_SSS");

      if (!requestHeaderValueExtractor.isText(request)) {
        // Images in base64 format will have the following formatted text
        // data:image/png;base64,iVBORw0K.....
        // the image data is after the comma
        String[] body = content.split(",");
        String imageString = body[1];

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] imageByte = decoder.decodeBuffer(imageString);

        byteArrayInputStream = new ByteArrayInputStream(imageByte);
        BufferedImage image = ImageIO.read(byteArrayInputStream);

        savedFile = new File(resourceFolder + File.separator + simpleDateFormat.format(new Date())
            + "." + fileExtension);

        ImageIO.write(image, fileExtension, savedFile);
      } else {
        savedFile = new File(resourceFolder + File.separator + simpleDateFormat.format(new Date())
            + "." + fileExtension);
        savedFile.createNewFile();
        bufferedWriter = new BufferedWriter(new FileWriter(savedFile));
        bufferedWriter.write(content);
      }
      return savedFile;
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        if (byteArrayInputStream != null) {
          byteArrayInputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        if (bufferedWriter != null) {
          bufferedWriter.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public RequestHeaderValueExtractor getRequestHeaderValueExtractor() {
    return requestHeaderValueExtractor;
  }

  public void setRequestHeaderValueExtractor(
      RequestHeaderValueExtractor requestHeaderValueExtractor) {
    this.requestHeaderValueExtractor = requestHeaderValueExtractor;
  }
}
