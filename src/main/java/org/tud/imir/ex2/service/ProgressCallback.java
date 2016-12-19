package org.tud.imir.ex2.service;

public interface ProgressCallback {
    void onProgress(long workDone, long total);
}
