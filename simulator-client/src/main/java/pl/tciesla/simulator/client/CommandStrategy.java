package pl.tciesla.simulator.client;

/**
 * Interface needed to implement strategy pattern for commands.
 */
public interface CommandStrategy {

    /**
     * Perform specific operations for command.
     */
    void execute();

    /**
     * Returns strategy description.
     * @return strategy description
     */
    String getDescription();
}
