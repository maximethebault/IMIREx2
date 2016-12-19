package org.tud.imir.ex2.core.index;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.tud.imir.ex2.core.Content;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class IndexIO {
    protected Index index;
    protected Path indexPath;

    public IndexIO(String indexName) {
        File rootFile = new File(".");
        indexPath = rootFile.toPath().resolve("index").resolve(indexName);
        if (!indexPath.toFile().isDirectory() && !indexPath.toFile().mkdirs()) {
            throw new RuntimeException("Could not create index directory");
        }

        try {
            loadIndex();
            loadMapper();
            for (int i = 0; i < Content.values().length; i++) {
                loadDescriptors(i);
            }
        }
        catch (FileNotFoundException e) {
            index = new Index();
            saveIndex();
        }
    }

    public void loadIndex() throws FileNotFoundException {
        File propertyFile = indexPath.resolve("properties").toFile();
        if (!propertyFile.isFile()) {
            throw new FileNotFoundException("Property file was not found");
        }
        try {
            FileInputStream f = new FileInputStream(propertyFile);
            BufferedInputStream bis = new BufferedInputStream(f);
            FSTObjectInput s = new FSTObjectInput(bis);
            index = (Index) s.readObject();
            s.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Property file could not be read (access path: " + indexPath.toString() + ")");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Input object couldn't be casted (access path: " + indexPath.toString() + ")");
        }
    }

    public void saveIndex() {
        File propertyFile = indexPath.resolve("properties").toFile();
        try {
            FileOutputStream f = new FileOutputStream(propertyFile);
            BufferedOutputStream bos = new BufferedOutputStream(f);
            FSTObjectOutput s = new FSTObjectOutput(bos);
            s.writeObject(index);
            s.close();
        }
        catch (IOException exception) {
            System.err.println("Property file could not be written (access path: " + indexPath.toString() + ")");
        }
    }

    public void loadMapper() throws FileNotFoundException {
        File propertyFile = indexPath.resolve("mapper").toFile();
        if (!propertyFile.isFile()) {
            throw new FileNotFoundException("Mapper file was not found");
        }
        try {
            FileInputStream f = new FileInputStream(propertyFile);
            BufferedInputStream bis = new BufferedInputStream(f);
            FSTObjectInput s = new FSTObjectInput(bis);
            Map<Integer, String> mapper = (Map<Integer, String>) s.readObject();
            index.setDocumentIdToName(mapper);
            s.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Property file could not be read (access path: " + indexPath.toString() + ")");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Input object couldn't be casted (access path: " + indexPath.toString() + ")");
        }
    }

    public void saveMapper() {
        File mapperFile = indexPath.resolve("mapper").toFile();
        try {
            FileOutputStream f = new FileOutputStream(mapperFile);
            BufferedOutputStream bos = new BufferedOutputStream(f);
            FSTObjectOutput s = new FSTObjectOutput(bos);
            s.writeObject(index.getDocumentIdToName());
            s.close();
        }
        catch (IOException exception) {
            System.err.println("Mapper file could not be written (access path: " + indexPath.toString() + ")");
        }
    }


    private List<int[]> loadDescriptorsFromFile(int contentId) throws FileNotFoundException {
        File dictionnaryFile = indexPath.resolve("descriptors_" + contentId).toFile();
        if (!dictionnaryFile.isFile()) {
            throw new FileNotFoundException("Descriptor file was not found");
        }
        try {
            FileInputStream f = new FileInputStream(dictionnaryFile);
            BufferedInputStream bis = new BufferedInputStream(f);
            FSTObjectInput s = new FSTObjectInput(bis);
            List<int[]> dictionnary = (List<int[]>) s.readObject();
            s.close();
            return dictionnary;
        }
        catch (IOException exception) {
            System.err.println("Descriptor file could not be read (access path: " + indexPath.toString() + ")");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Input object couldn't be casted (access path: " + indexPath.toString() + ")");
        }
        return null;
    }

    public void loadDescriptors(int contentId) throws FileNotFoundException {
        List<int[]> descriptors = loadDescriptorsFromFile(contentId);
        index.putDescriptors(contentId, descriptors);
    }

    public void loadDescriptors(Content content) throws FileNotFoundException {
        loadDescriptors(content.getId());
    }

    public void saveDescriptorsToFile(int contentId, List<int[]> descriptors) {
        File dictionnaryFile = indexPath.resolve("descriptors_" + contentId).toFile();
        try {
            FileOutputStream f = new FileOutputStream(dictionnaryFile);
            BufferedOutputStream bos = new BufferedOutputStream(f);
            FSTObjectOutput s = new FSTObjectOutput(bos);
            s.writeObject(descriptors);
            s.close();
        }
        catch (IOException exception) {
            System.err.println("Descriptor file could not be written (access path: " + indexPath.toString() + ")");
        }
    }

    public void saveDescriptors(int contentId) {
        saveDescriptorsToFile(contentId, index.getDescriptors(contentId));
    }

    public void saveDescriptors(Content content) {
        saveDescriptors(content.getId());
    }
}
