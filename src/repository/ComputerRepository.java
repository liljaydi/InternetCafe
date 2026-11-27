package repository;

public class ComputerRepository {
    // Responsibility: store and load computers. It has no rules and no Swing.
    //
    // STEP 1: keep a private List<Computer> in memory
    //
    // TODO methods (planned):
    //   - List<Computer> findAll()
    //   - Computer findById(int id)
    //   - void add(Computer computer)         // also generates the next id
    //   - void update(Computer computer)
    //   - void deleteById(int id)
    //
    // STEP 2 (OPTIONAL, decide later): file persistence
    //   - File: data/computers.txt, one line per computer, e.g. "1,PC-001,Gaming"
    //   - load() in the constructor: read the file into the list (create it if missing)
    //   - save(): rewrite the file after every add/update/delete
    //   - The only class that touches java.io. Nothing else changes when you add this.
}