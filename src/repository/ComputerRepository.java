package repository;

import model.Computer;

import java.util.ArrayList;
import java.util.List;

public class ComputerRepository {

    // Responsibility: store and load computers. No rules, no Swing.
    private final List<Computer> computers = new ArrayList<>();
    private int nextId = 1;

    public ComputerRepository() {
        // TODO (optional, file storage): load data/computers.txt into `computers` here.
        //   - create the file/folder if missing
        //   - parse each line ("id,name,category") into a Computer
        //   - set nextId to (highest id found) + 1 so ids are never reused
    }

    public List<Computer> findAll() {
        return new ArrayList<>(computers);   // copy, so callers can't modify the stored list
    }

    public Computer findById(int id) {
        for (Computer c : computers) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    /** Creates the computer with the next id, stores it, and returns it. */
    public Computer add(String name, String category) {
        Computer computer = new Computer(nextId++, name, category);
        computers.add(computer);
        System.out.println("computer added in the system");
        // TODO (optional, file storage): call saveToFile() here.
        return computer;
    }

    /** Replaces the stored computer that has the same id. */
    public void update(Computer computer) {
        for (int i = 0; i < computers.size(); i++) {
            if (computers.get(i).getId() == computer.getId()) {
                computers.set(i, computer);
                // TODO (optional, file storage): call saveToFile() here.
                return;
            }
        }
    }

    public void deleteById(int id) {
        computers.removeIf(c -> c.getId() == id);
        // TODO (optional, file storage): call saveToFile() here.
    }

    // TODO (optional, file storage): private void saveToFile()
    //   - rewrite data/computers.txt with one line per computer
    //   - this and the constructor are the only places that should touch java.io
    //   - decide how to report IOException (e.g., wrap in a RuntimeException so the
    //     controller can return a message to the UI)
}