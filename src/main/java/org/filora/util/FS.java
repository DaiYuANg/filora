package org.filora.util;

import io.smallrye.mutiny.Uni;
import io.vavr.control.Try;
import io.vertx.core.file.OpenOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.file.AsyncFile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.filora.model.StreamWithHash;
import org.filora.stream.HashingOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Path;
import java.security.MessageDigest;

import static io.smallrye.mutiny.unchecked.Unchecked.consumer;

@UtilityClass
public class FS {

  private final OpenOptions readOptions = new OpenOptions().setRead(true);

  public Uni<AsyncFile> openFile(@NotNull Vertx vertx, @NotNull Path path) {
    return vertx.fileSystem().open(path.toString(), readOptions);
  }

  @SneakyThrows
  public StreamWithHash asyncFileToInputStream(@NotNull AsyncFile asyncFile, String algorithm) {
    val pipedIn = new PipedInputStream(64 * 1024); // 64KB 缓冲
    val pipedOut = new PipedOutputStream(pipedIn);

    val digest = MessageDigest.getInstance(algorithm); // e.g., SHA-256
    val hashingOut = new HashingOutputStream(pipedOut, digest);

    asyncFile
      .handler(consumer(
          buffer -> hashingOut.write(buffer.getBytes())
        )
      )
      .exceptionHandler(err ->
        Try.run(() -> {
          hashingOut.close();
          pipedIn.close();
        })
      )
      .endHandler(() -> Try.run(hashingOut::close)
      );

    return new StreamWithHash(pipedIn, hashingOut.getHash());
  }

}
