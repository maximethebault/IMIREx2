package org.tud.imir.ex2.core.index;

import org.tud.imir.ex2.core.Content;
import org.tud.imir.ex2.core.descriptor.Mpeg7Descriptor;

import java.util.ArrayList;
import java.util.List;

public class IndexWriter extends IndexIO {
    private Mpeg7Descriptor descriptor;

    public IndexWriter(String indexName) {
        super(indexName);
        descriptor = new Mpeg7Descriptor();
    }

    public void addDocument(String documentName, String imagePath, Content content) {
        int[] descriptor = this.descriptor.computeDescriptor(imagePath);
        int[] descriptorWithId = new int[descriptor.length + 1];
        descriptorWithId[0] = index.getDocumentIdOffset();
        System.arraycopy(descriptor, 0, descriptorWithId, 1, descriptor.length);
        if (content == null) {
            for (Content iteratingContent : Content.values()) {
                insertDescriptorInIndex(iteratingContent, descriptorWithId);
            }
        }
        else {
            insertDescriptorInIndex(content, descriptorWithId);
        }
        index.getDocumentIdToName().put(index.getDocumentIdOffset(), documentName);
        index.incrementDocumentIdOffset();
    }

    private void insertDescriptorInIndex(Content content, int[] descriptorWithId) {
        List<int[]> contentDescriptors = index.getDescriptors(content.getId());
        if (contentDescriptors == null) {
            contentDescriptors = new ArrayList<>();
            index.putDescriptors(content.getId(), contentDescriptors);
        }
        contentDescriptors.add(descriptorWithId);
    }

    public int getDocumentNumber() {
        return index.getDocumentIdOffset();
    }

    public void close() {
        saveIndex();
        saveMapper();

        for (int i = 0; i < Content.values().length; i++) {
            saveDescriptors(i);
        }
    }
}
