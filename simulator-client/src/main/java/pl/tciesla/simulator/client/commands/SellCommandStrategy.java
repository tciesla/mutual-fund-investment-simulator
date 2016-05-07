package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.ClientSingleton;
import pl.tciesla.simulator.client.CommandStrategy;
import pl.tciesla.simulator.client.Defaults;
import pl.tciesla.simulator.commons.constant.ShareType;

import javax.ws.rs.core.MediaType;
import java.util.Scanner;

/**
 * Strategy enables sell mutual fund shares operation.
 */
public class SellCommandStrategy implements CommandStrategy {

    private String fundId;
    private String shareType;
    private String shareAmount;

    @Override
    public void execute() {
        getNeededDataFromUser();
        makeSellRequest();
    }

    private void getNeededDataFromUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter mutual fund id [default: 1]: ");
        String fundId = scanner.nextLine().trim();
        this.fundId = fundId.isEmpty() ? "1" : fundId;

        System.out.print("Enter share type [A or B, default: A]: ");
        String shareType = scanner.nextLine().trim().toUpperCase();
        if (!shareType.equals("A") && !shareType.equals("B")) {
            this.shareType = ShareType.A.toString();
        } else {
            this.shareType = shareType;
        }

        System.out.print("Enter share amount [default: 1]: ");
        String shareAmount = scanner.nextLine().trim();
        this.shareAmount = shareAmount.isEmpty() ? "1" : shareAmount;
    }

    private void makeSellRequest() {
        String request = "http://localhost:8080/simulator/customer/"
                + Defaults.getUsername() + "/sell/" + fundId + "/" + shareType + "/" + shareAmount;
        String response = ClientSingleton.getClient().resource(request)
                .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(response);
    }

    @Override
    public String getDescription() {
        return "sell mutual funds shares";
    }
}
