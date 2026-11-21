package ticketbooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileIO {

    public static ArrayList<Event> loadEvents(String filename) {
        ArrayList<Event> events = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename),StandardCharsets.UTF_8)) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 5) {
                    throw new IllegalArgumentException("Expected 5 event fields: " + line);
                }

                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                EventCategory category = EventCategory.valueOf(fields[2].trim());
                int capacity = Integer.parseInt(fields[3].trim());
                double ticketPrice = Double.parseDouble(fields[4].trim());
                Event event = new Event(id, name, category, capacity, ticketPrice);

                events.add(event);
            }
        } catch (IOException |IllegalArgumentException e) {
            throw new IllegalStateException("Could not load events from " + filename, e);
        }

        return events;
    }

    public static ArrayList<Customer> loadCustomers(String filename) {
        ArrayList<Customer> customers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 4) {
                    throw new IllegalArgumentException("Expected 4 customer fields: " + line);
                }

                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                double balance = Double.parseDouble(fields[2].trim());
                MembershipType membership = MembershipType.valueOf(fields[3].trim());

                Customer customer;

                if (membership == MembershipType.PREMIUM) {
                    customer = new PremiumCustomer(id, name, balance);
                } else {
                    customer = new Customer(id, name, balance);
                }

                customers.add(customer);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException("Could not load customers from " + filename, e);
        }
        return customers;
    }

}
