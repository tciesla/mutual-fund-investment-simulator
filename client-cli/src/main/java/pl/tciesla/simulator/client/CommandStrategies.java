package pl.tciesla.simulator.client;

import pl.tciesla.simulator.client.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores command strategies in map.
 */
public class CommandStrategies {

    private static final Map<String, CommandStrategy> strategies = new HashMap<>();

    static {
        strategies.put("funds", new FundsCommandStrategy());
        strategies.put("valuation", new ValuationCommandStrategy());
        strategies.put("wallet", new WalletCommandStrategy());
        strategies.put("buy", new BuyCommandStrategy());
        strategies.put("sell", new SellCommandStrategy());
        strategies.put("delete", new DeleteCommandStrategy());
        strategies.put("help", new HelpCommandStrategy());
        strategies.put("exit", new ExitCommandStrategy());
    }

    /**
     * Returns command strategy for particular command name.
     * @param command command name
     * @return specific command
     */
    public static CommandStrategy getStrategy(String command) {
        if (command == null || !strategies.containsKey(command)) { return new DummyCommandStrategy(); }
        return strategies.get(command);
    }

    /**
     * Return all strategies for help command.
     * @return map with all command strategies
     */
    public static Map<String, CommandStrategy> getStrategies() {
        return strategies;
    }
}
