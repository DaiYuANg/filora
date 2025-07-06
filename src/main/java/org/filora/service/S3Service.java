package org.filora.service;

import io.minio.ObjectWriteResponse;
import io.smallrye.mutiny.Uni;

import java.io.InputStream;

public interface S3Service {
  Uni<ObjectWriteResponse> uploadToMinio(InputStream stream, String filename);
}
