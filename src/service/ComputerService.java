package service;

import model.*;
import repository.ComputerRepository;

import java.util.List;

public class ComputerService {
    private static final int NO_ID = -1;
    private int nextId = 1;
    private final ComputerRepository repository;

    public ComputerService(ComputerRepository repository) {
        this.repository = repository;
    }

    public List<Computer> getAllComputers() {
        return repository.findAll();
    }

    public void addComputer(String name, String category) {
        String cleanName = name.trim();
        validateName(cleanName, NO_ID);
        repository.add(createComputer(nextId++, cleanName, category));
    }

    // helper
    private Computer createComputer(int id, String name, String category) {
        Computer newComputer = null;
        if (category.equals("Standard")) {
            newComputer = new StandardComputer(
                id, name, category, true);
        } else if (category.equals("Gaming")) {
            newComputer = new GamingComputer(
                id, name, category, true);
        } else if (category.equals("Premium")) {
            newComputer = new PremiumComputer(
                id, name, category, true);
        } else {
            throw new IllegalArgumentException("Invalid computer category.");
        }
        return newComputer;
    }

    public void updateComputer(Computer computer, String name, String category) {
        String cleanName = name.trim();
        validateName(cleanName, computer.getId());
        if (!computer.isAvailable()) {
            throw new IllegalStateException(
                "Cannot update a computer while it is in use."
            );
        }
        repository.update(createComputer(computer.getId(), cleanName, category));
    }

    public void deleteComputer(Computer computer) {
        if (!computer.isAvailable()) {
            throw new IllegalStateException(
                "Cannot delete a computer while it is in use."
            );
        }
        // TODO (later): decide what happens to the computer's past session history.
        repository.deleteById(computer.getId());
    }

    // helper
    private void validateName(String name, int ignoreId) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Computer name is required.");
        }
        if (isDuplicateName(name, ignoreId)) {
            throw new IllegalArgumentException("A computer named \"" + name + "\" already exists.");
        }
    }

    // helper
    private boolean isDuplicateName(String name, int ignoreId) {
        for (Computer c : repository.findAll()) {
            if (c.getId() != ignoreId && c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}