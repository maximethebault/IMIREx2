package org.tud.imir.ex2.core.index;

import org.tud.imir.ex2.core.Content;
import org.tud.imir.ex2.core.descriptor.Mpeg7Descriptor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class IndexReader extends IndexIO {

    public IndexReader(String indexName) {
        super(indexName);
    }

    public Map<Integer, Double> findSimilarByDocId(String imagePath, Content content) {
        Mpeg7Descriptor descriptorGenerator = new Mpeg7Descriptor();
        int[] descriptor = descriptorGenerator.computeDescriptor(imagePath);

        TreeMap<Integer, Double> documentIdToDistance = new TreeMap<>();

        if(content == null) {
            for (Content iteratingContent : Content.values()) {
                for (int[] iteratingDescriptor : index.getDescriptors(iteratingContent.getId())) {
                    documentIdToDistance.put(iteratingDescriptor[0], compare(descriptor, iteratingDescriptor));
                }
            }
        }
        else {
            for (int[] iteratingDescriptor : index.getDescriptors(content.getId())) {
                documentIdToDistance.put(iteratingDescriptor[0], compare(descriptor, iteratingDescriptor));
            }
        }

        return documentIdToDistance.entrySet()
                                   .stream()
                                   .sorted(Map.Entry.comparingByValue())
                                   .collect(Collectors.toMap(
                                           Map.Entry::getKey,
                                           Map.Entry::getValue,
                                           (e1, e2) -> e1,
                                           LinkedHashMap::new)
                                   );
    }

    public Map<String, Double> findSimilarByDocName(String imagePath, Content content) {
        Map<Integer, Double> similar = findSimilarByDocId(imagePath, content);

        return similar.entrySet()
                      .stream()
                      .collect(
                              Collectors.toMap(
                                      e -> index.getDocumentIdToName().get(e.getKey()),
                                      Map.Entry::getValue,
                                      (e1, e2) -> e2,
                                      LinkedHashMap::new
                              )
                      );
    }

    private double compare(int[] descriptor, int[] iteratingDescriptor) {
        double difference = 0;
        for (int i = 0; i < descriptor.length; i++) {
            difference += Math.pow(descriptor[i] - iteratingDescriptor[i + 1], 2);
        }
        return Math.sqrt(difference);
    }

    public Map<Integer, String> mapDocumentIdToName(Set<Integer> documentIds) {
        return documentIds.stream().collect(Collectors.toMap(
                id -> id,
                id -> index.getDocumentIdToName().get(id),
                (value1, value2) -> value2
        ));
    }
}
