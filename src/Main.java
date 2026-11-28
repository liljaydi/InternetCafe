import repository.ComputerRepository;
import service.ComputerService;
import ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Build the backend from the bottom up, passing each layer to the next
        ComputerRepository repository = new ComputerRepository();
        ComputerService computerService = new ComputerService(repository);

        // TODO (later): when you add Sessions, create SessionRepository / SessionService here
        // the same way, and give ComputerService access to the session service so it can
        // refuse to delete a computer that's in use.
        // TODO (later, only if needed): add a controller class if one UI action has to
        // coordinate several services (e.g., "Start session").

        new MainFrame(computerService);
    }
}