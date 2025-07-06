package org.filora.model;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.apache.commons.codec.binary.Hex;

import java.io.InputStream;
import java.util.HexFormat;

@RecordBuilder
public record StreamWithHash(InputStream inputStream, byte[] hash) {

  public String hexHash() {
    return HexFormat.of().formatHex(hash);
  }
}
