package se.rn.decerno.colourmemory.colourmemory.model;

public class Card {
    private final String id;
    private final Color color;
    private boolean matched;

    public Card(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}

