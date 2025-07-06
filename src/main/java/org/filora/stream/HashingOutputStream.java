package org.filora.stream;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

@RequiredArgsConstructor
public class HashingOutputStream extends OutputStream {
  private final OutputStream delegate;
  private final MessageDigest digest;
  @Override
  public void write(int b) throws IOException {
    digest.update((byte) b);
    delegate.write(b);
  }

  @Override
  public void write(byte @NotNull [] b, int off, int len) throws IOException {
    digest.update(b, off, len);
    delegate.write(b, off, len);
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }

  public byte[] getHash() {
    return digest.digest();
  }
}
