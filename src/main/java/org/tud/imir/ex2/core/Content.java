package org.tud.imir.ex2.core;

public enum Content {
    ENTIRE(0, "Entire"),
    BRANCH(1, "Branch"),
    FLOWER(2, "Flower"),
    FRUIT(3, "Fruit"),
    LEAF(4, "Leaf"),
    LEAF_SCAN(5, "LeafScan"),
    STEM(6, "Stem");

    private int id;
    private String stringIdentifier;

    Content(int id, String stringIdentifier) {
        this.id = id;
        this.stringIdentifier = stringIdentifier;
    }

    public int getId() {
        return id;
    }

    public String getStringIdentifier() {
        return stringIdentifier;
    }

    public static Content getEnum(String value) {
        for (Content v : values())
            if (v.getStringIdentifier().equalsIgnoreCase(value)) {
                return v;
            }
        throw new IllegalArgumentException();
    }

}
