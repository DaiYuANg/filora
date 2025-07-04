package org.filora;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vavr.Tuple;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.jboss.resteasy.reactive.RestForm;

import java.io.InputStream;

@Path("/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadResource {

  private final S3Service s3Service;

  @SneakyThrows
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @WithTransaction
  public Uni<Response> uploadFile(UploadForm form) {
    val fileStream = FileUtils.openInputStream(form.file().uploadedFile().toFile());
    val filename = form.file().fileName();
    log.info("Uploading file {}", filename);
    val saveMeta = s3Service.saveMetadata(filename, form);
    val upload = s3Service.uploadToMinio(fileStream, filename)
      .replaceWithVoid();

    return Uni.combine()
      .all()
      .unis(saveMeta, upload)
      .asTuple()
      .onItem().transform(t -> Response.ok().build())
      .onFailure().recoverWithItem(err -> Response.serverError().entity(err.getMessage()).build());
  }
}
