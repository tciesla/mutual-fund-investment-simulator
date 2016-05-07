package pl.tciesla.simulator.client;

import org.junit.Test;
import pl.tciesla.simulator.client.commands.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandStrategiesTest {

    @Test
    public void shouldContainsEightStrategies() throws Exception {
        // when
        Map<String, CommandStrategy> strategies = CommandStrategies.getStrategies();
        // then
        assertThat(strategies).hasSize(8);
    }

    @Test
    public void shouldContainHelpStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("help");
        // then
        assertThat(strategy).isInstanceOf(HelpCommandStrategy.class);
    }

    @Test
    public void shouldContainExitStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("exit");
        // then
        assertThat(strategy).isInstanceOf(ExitCommandStrategy.class);
    }

    @Test
    public void shouldContainBuyStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("buy");
        // then
        assertThat(strategy).isInstanceOf(BuyCommandStrategy.class);
    }

    @Test
    public void shouldContainSellStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("sell");
        // then
        assertThat(strategy).isInstanceOf(SellCommandStrategy.class);
    }

    @Test
    public void shouldContainDeleteStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("delete");
        // then
        assertThat(strategy).isInstanceOf(DeleteCommandStrategy.class);
    }


    @Test
    public void shouldContainFundsStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("funds");
        // then
        assertThat(strategy).isInstanceOf(FundsCommandStrategy.class);
    }

    @Test
    public void shouldContainValuationStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("valuation");
        // then
        assertThat(strategy).isInstanceOf(ValuationCommandStrategy.class);
    }

    @Test
    public void shouldContainWalletStrategy() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("wallet");
        // then
        assertThat(strategy).isInstanceOf(WalletCommandStrategy.class);
    }

    @Test
    public void shouldReturnDummyStrategyWhenCommandIsNull() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy(null);
        // then
        assertThat(strategy).isInstanceOf(DummyCommandStrategy.class);
    }

    @Test
    public void shouldReturnDummyStrategyWhenCommandNotExists() throws Exception {
        // when
        CommandStrategy strategy = CommandStrategies.getStrategy("xyz");
        // then
        assertThat(strategy).isInstanceOf(DummyCommandStrategy.class);
    }

}