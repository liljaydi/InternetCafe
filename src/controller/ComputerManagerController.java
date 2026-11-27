package controller;

public class ComputerManagerController {
    // Responsibility: the bridge between ComputerManagerPanel and ComputerService.
    // It receives the ComputerService through its constructor.
    //
    // TODO methods (planned), one per UI action:
    //   - getComputers()                          -> used when the panel loads
    //   - addComputer(name, category)             -> Save in Add mode
    //   - updateComputer(id, name, category)      -> Save in Edit mode
    //   - deleteComputer(id)                      -> row Delete button
    //
    // Each one calls the service, catches validation errors, and gives the panel either
    // the result or an error message to display. No Swing components in here.
}