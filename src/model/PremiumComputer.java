package model;

public class PremiumComputer extends Computer{
    public PremiumComputer(int id, String name, String category) {
        super(id, name, category);
        System.out.println("premium computer created");
    }
}
