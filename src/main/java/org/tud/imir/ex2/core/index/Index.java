package org.tud.imir.ex2.core.index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index implements Serializable {
    private int documentIdOffset;
    /**
     * Integer: id of the Content
     * =>
     * List<int[]>: List of descriptors
     */
    private Map<Integer, List<int[]>> contentToDescriptors;
    private Map<Integer, String> documentIdToName;

    public Index() {
        this.documentIdOffset = 0;
        this.contentToDescriptors = new HashMap<>();
        this.documentIdToName = new HashMap<>();
    }

    public int getDocumentIdOffset() {
        return documentIdOffset;
    }

    public void setDocumentIdOffset(int documentIdOffset) {
        this.documentIdOffset = documentIdOffset;
    }

    public void incrementDocumentIdOffset() {
        this.documentIdOffset++;
    }

    public List<int[]> getDescriptors(int contentId) {
        return contentToDescriptors.get(contentId);
    }

    public void putDescriptors(int contentId, List<int[]> descriptors) {
        contentToDescriptors.put(contentId, descriptors);
    }

    public Map<Integer, String> getDocumentIdToName() {
        return documentIdToName;
    }

    public void setDocumentIdToName(Map<Integer, String> documentIdToName) {
        this.documentIdToName = documentIdToName;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(documentIdOffset);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.documentIdOffset = ois.readInt();
    }

}
