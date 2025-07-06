package org.filora.stream;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class LoggingOutputStream extends OutputStream {
  private static final int DEFAULT_BUFFER_SIZE = 2048;
  private final StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);

  @Override
  public void write(int b) throws IOException {
    if (b == '\n') {
      flush();
    } else {
      buffer.append((char) b);
    }
  }

  @Override
  public void flush() throws IOException {
    if (!buffer.isEmpty()) {
      log.debug("[MinIO Trace] {}", buffer.toString());
      buffer.setLength(0);
    }
  }

  @Override
  public void close() throws IOException {
    flush();
  }
}
