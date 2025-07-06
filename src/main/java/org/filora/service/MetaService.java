package org.filora.service;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import org.filora.entity.FileMeta;
import org.filora.model.UploadForm;
import org.jetbrains.annotations.NotNull;

public interface MetaService {
  @WithTransaction
  Uni<FileMeta> saveMetadata(String filename, @NotNull UploadForm form);
}
