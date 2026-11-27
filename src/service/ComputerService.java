package service;

public class ComputerService {
    // Responsibility: the rules of the app. It has no Swing and no file code.
    // It receives the ComputerRepository through its constructor.
    //
    // TODO methods (planned):
    //   - List<Computer> getAllComputers()
    //   - void addComputer(String name, String category)
    //       rules: name not empty, name not duplicate (ignore case), category valid
    //   - void updateComputer(int id, String name, String category)
    //       same rules, but ignore the computer's own current name in the duplicate check
    //   - void deleteComputer(int id)
    //       rule (later, once Sessions exist): refuse if the computer has an active session
    //
    // Reporting errors: throw an exception (e.g., IllegalArgumentException with a readable
    // message) and let the controller/UI show it in a dialog.
}