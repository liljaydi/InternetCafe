package model;

public class StandardComputer extends Computer{
    public StandardComputer(int id, String name, String category) {
        super(id, name, category);
        System.out.println("standard computer created");
    }
}
