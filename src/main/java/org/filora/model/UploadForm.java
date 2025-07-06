package org.filora.model;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@RecordBuilder
public record UploadForm(
  @RestForm("file")
  FileUpload file,
  @RestForm("desc")
  String desc
) {
}
