package model;

public class Computer {
    private final int id;
    private final String name;
    private final String category;
    // NOTE: no status here. Status belongs to the Session module.

    public Computer(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }

    // The class is immutable on purpose: to "edit" a computer, the service creates a new
    // Computer with the same id and the repository replaces the old one.

    // TODO (only if you do file storage): decide where the file format lives.
    //   Suggestion: leave it to the repository (e.g., "1,PC-001,Gaming"), not here.
    // TODO (optional): replace the String category with a Category enum.
}