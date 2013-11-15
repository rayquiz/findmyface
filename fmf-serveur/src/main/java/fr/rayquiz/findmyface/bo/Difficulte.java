package fr.rayquiz.findmyface.bo;

public enum Difficulte {
    SIMPLE(1), MOYEN(2), DIFFICILE(3);

    private int importance;

    private Difficulte(final int importance) {
        this.importance = importance;
    }

    public int getImportance() {
        return importance;
    }
}
