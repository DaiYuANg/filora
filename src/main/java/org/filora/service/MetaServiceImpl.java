package org.filora.service;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.filora.entity.FileMeta;
import org.filora.model.UploadForm;
import org.jetbrains.annotations.NotNull;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class MetaServiceImpl implements MetaService{

  @WithTransaction
  @Override
  public Uni<FileMeta> saveMetadata(String filename, @NotNull UploadForm form) {
    val meta = new FileMeta();
    meta.setFilename(filename);
    meta.setDescription(form.desc());
    meta.setStoragePath("/files/" + filename);
    log.info("Saving file {}", meta);
    return meta.persist();
  }
}
