package service;

import model.Computer;
import repository.ComputerRepository;

import java.util.List;

public class ComputerService {

    // Responsibility: the rules of the app. No Swing, no file code.
    // Rule violations are reported by throwing IllegalArgumentException with a readable message.

    private static final int NO_ID = -1;   // "no computer to ignore" (real ids start at 1)

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
        System.out.println("computer passed validation");
        repository.add(cleanName, category);
    }

    public void updateComputer(int id, String name, String category) {
        String cleanName = name.trim();
        validateName(cleanName, id);   // ignore this computer's own current name
        // TODO (optional): validate that `category` is one of the allowed categories.
        // TODO (later, Sessions): refuse to edit a computer that has an active session.
        repository.update(new Computer(id, cleanName, category));
    }

    public void deleteComputer(int id) {
        // TODO (later, Sessions): if the computer has an active session, throw
        //   new IllegalArgumentException("<name> is currently in use.") instead of deleting.
        //   This needs access to the session service/repository.
        // TODO (later): decide what happens to the computer's past session history.
        repository.deleteById(id);
    }

    private void validateName(String name, int ignoreId) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Computer name is required.");
        }
        if (isDuplicateName(name, ignoreId)) {
            throw new IllegalArgumentException("A computer named \"" + name + "\" already exists.");
        }
    }

    private boolean isDuplicateName(String name, int ignoreId) {
        for (Computer c : repository.findAll()) {
            if (c.getId() != ignoreId && c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}