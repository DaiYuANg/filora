package org.filora;

import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.smallrye.mutiny.vertx.MutinyHelper;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class S3Service {
  private final MinioAsyncClient minioClient;

  public Uni<FileMeta> saveMetadata(String filename, @NotNull UploadForm form) {
    val meta = new FileMeta();
    meta.setFilename(filename);
    meta.setDescription(form.desc());
    meta.setStoragePath("/files/" + filename);
    log.info("Saving file {}", meta);
    return meta.persist();
  }

  public Uni<ObjectWriteResponse> uploadToMinio(InputStream stream, String filename) {
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
    ).log();
  }
}
