package org.filora.lifecycle;

import io.minio.MinioClient;
import io.quarkus.arc.All;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.filora.stream.LoggingOutputStream;

import java.util.List;

@ApplicationScoped
@Slf4j
public class MinioLifecycle {

  private final List<MinioClient> clients;

  MinioLifecycle(@All List<MinioClient> clients) {
    this.clients = clients;
  }

  void onStart(@Observes StartupEvent ev) {
    clients.forEach(client -> {
      client.traceOn(new LoggingOutputStream());
      client.enableDualStackEndpoint();
      client.enableVirtualStyleEndpoint();
    });
  }

  void onStop(@Observes ShutdownEvent ev) {
    log.atDebug().log("The application is stopping...");
  }
}
