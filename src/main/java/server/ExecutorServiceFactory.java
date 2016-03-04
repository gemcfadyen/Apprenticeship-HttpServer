package server;

import java.util.concurrent.ExecutorService;

public interface ExecutorServiceFactory {
    ExecutorService create();
}
