package model;

public class GamingComputer extends Computer{
    public GamingComputer(int id, String name, String category) {
        super(id, name, category);
        System.out.println("gaming computer created");
    }
}
