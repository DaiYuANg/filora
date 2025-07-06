package org.filora.endpoint;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.file.AsyncFile;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.filora.model.UploadForm;
import org.filora.service.MetaService;
import org.filora.service.S3Service;
import org.filora.util.FS;

import static io.smallrye.mutiny.Uni.combine;

@Path("/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadResource {

  private final S3Service s3Service;

  private final MetaService metaService;

  private final Vertx vertx;

  @SneakyThrows
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Uni<Response> uploadFile(UploadForm form) {
    val filename = form.file().fileName() + System.currentTimeMillis();
    log.atDebug().log("Uploading file {}", filename);
    val saveMeta = metaService.saveMetadata(filename, form);
    val upload = FS
      .openFile(vertx, form.file().uploadedFile())
      .map((AsyncFile asyncFile) -> FS.asyncFileToInputStream(asyncFile, "SHA-256"))
      .invoke(s -> {
        log.atInfo().log("Hash:{}", s.hexHash());
      })
      .log()
      .flatMap(streamWithHash -> s3Service.uploadToMinio(streamWithHash.inputStream(), filename))
      .replaceWithVoid();
    return combine()
      .all()
      .unis(saveMeta, upload)
      .asTuple()
      .onItem().transform(t -> Response.ok().build())
      .onFailure().recoverWithItem(err -> Response.serverError().entity(err.getMessage()).build());
  }
}
