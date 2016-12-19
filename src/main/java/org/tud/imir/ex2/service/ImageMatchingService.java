package org.tud.imir.ex2.service;

import org.tud.imir.ex2.core.Content;
import org.tud.imir.ex2.core.index.IndexReader;
import org.tud.imir.ex2.core.index.IndexWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ImageMatchingService {

    public boolean isIndexFilled(String indexName) {
        IndexWriter indexWriter = new IndexWriter(indexName);
        return indexWriter.getDocumentNumber() > 0;
    }

    public void indexResources(String indexName, String resourcePath, ProgressCallback progressCallback) {
        long startTime = System.nanoTime();
        try {
            IndexWriter indexWriter = new IndexWriter(indexName);
            List<Path> imageList = Files.walk(Paths.get(resourcePath))
                                        .filter((path) -> {
                                            try {
                                                return Files.isRegularFile(path) && path.toString().endsWith(".jpg") && Files.size(path) > 2048;
                                            }
                                            catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            return false;
                                        }).collect(Collectors.toList());
            final long totalCount = imageList.size();
            AtomicLong progressCount = new AtomicLong(0);

            imageList.forEach(jpgFilePath -> {
                if (progressCallback != null && progressCount.incrementAndGet() % 10 == 0) {
                    progressCallback.onProgress(progressCount.get(), totalCount);
                }
                try {
                    String basename = Utils.stripExtension(jpgFilePath.getFileName().toString());
                    Path xmlFilePath = jpgFilePath.resolveSibling(basename + ".xml");
                    String xml = new String(Files.readAllBytes(xmlFilePath));
                    Content content = null;
                    try {
                        String contentStr = Utils.getBetweenStrings(xml, "<Content>", "</Content>");
                        content = Content.getEnum(contentStr);
                    }
                    catch (Exception ignored) {
                    }
                    indexWriter.addDocument(basename, jpgFilePath.toString(), content);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
            indexWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Indexing took " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
    }

    public List<String> findSimilarImages(String jpgFilePath, int limit) throws IOException {
        long startTime = System.nanoTime();

        Path path = Paths.get(jpgFilePath);
        if (!Files.isRegularFile(path) || (!path.toString().toLowerCase().endsWith(".jpg") && !path.toString().toLowerCase().endsWith(".jpeg")) || Files.size(path) <= 2048) {
            throw new IllegalArgumentException("Incorrect JPEG file as input");
        }
        String basename = Utils.stripExtension(path.getFileName().toString());
        Path xmlFilePath = path.resolveSibling(basename + ".xml");
        String xml = new String(Files.readAllBytes(xmlFilePath));
        Content content = null;
        try {
            String contentStr = Utils.getBetweenStrings(xml, "<Content>", "</Content>");
            content = Content.getEnum(contentStr);
        }
        catch (Exception ignored) {
        }
        ConfigurationHolder configurationHolder = ConfigurationHolder.getInstance();
        IndexReader indexReader = new IndexReader(configurationHolder.getIndexName());
        Map<String, Double> similar = indexReader.findSimilarByDocName(jpgFilePath, content);

        System.out.println("Query took " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");

        return similar.entrySet()
                      .stream()
                      .limit(limit)
                      .map((entry) -> path.getParent() + "/" + entry.getKey() + ".jpg")
                      .collect(
                              Collectors.toCollection(LinkedList::new)
                      );
    }
}
