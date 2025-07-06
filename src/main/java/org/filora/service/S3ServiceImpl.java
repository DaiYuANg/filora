package org.filora.service;

import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {
  private final MinioAsyncClient minioClient;

  private final Tika tika = new Tika();

  private final Vertx vertx;

  @Override
  public Uni<ObjectWriteResponse> uploadToMinio(InputStream stream, String filename) {
    vertx.executeBlockingAndForget(() -> {
      try {
        val ct = tika.detect(stream);
        System.err.println(ct);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return null;
    });

    return Uni.createFrom().completionStage(
        Unchecked.supplier(
          () -> {
            log.info("Uploading file {}", filename);
            return minioClient.putObject(PutObjectArgs.builder()
              .bucket("uploads")
              .object(filename)
              .stream(stream, -1, 10485760)
              .contentType("application/octet-stream")
              .build());
          }
        )
      )
      .log()
      ;
  }
}
