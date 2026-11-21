# Event Ticket Booking System

![CI](https://github.com/ozgeAAygul/EventTicketBookingSystem/actions/workflows/ci.yml/badge.svg)

A Java console application for booking event tickets, applying purchase discounts, cancelling reservations, and reporting on active bookings. Built by Özge Aygül as an individual learning project, originally inspired by a group assignment.

## Features

- Load event and customer records from UTF-8 CSV files.
- Support regular and premium customers through inheritance and polymorphism.
- Check customer and event IDs, ticket quantity, remaining seats, and balance before completing a purchase.
- Apply a configurable group discount to purchases of four or more tickets, followed by an additional 5% premium discount.
- Cancel a reservation with a full refund and retain its history.
- Report the most popular event, highest-revenue event, top customer, and active tickets by category.
- Covered by JUnit 5 unit tests.

## Run

Install a Java Development Kit (JDK) 8 or later. Running the tests requires Maven. The application uses standard Java libraries and has no external dependencies.

From the project root, where the two CSV files are located, run these commands in a terminal. They work in Windows Command Prompt and typical macOS/Linux shells.

```sh
javac -encoding UTF-8 -d out src/ticketbooking/*.java
java -cp out ticketbooking.TicketBookingApp
```

In IntelliJ IDEA, open the project, select a JDK, mark `src` as Sources Root if necessary, and run `TicketBookingApp.main`. Set the run configuration's working directory to the project root so relative CSV paths resolve correctly.

The application runs a predefined demonstration automatically. It lists records, performs purchases and cancellations, prints four reports, and displays updated balances and available seats.

## Tests

The project includes JUnit 5 tests for the booking logic: purchases, discounts, cancellations, validation errors, and CSV loading. They live in the `test/` folder.

Run them from the project root with Maven:

```sh
mvn test
```

In IntelliJ IDEA, open `BookingSystemTest` and click the green run icon next to the class name. The tests also run automatically on every push through GitHub Actions.

## Demo

With the supplied sample CSV files, the demonstration does the following.

| Operation | Outcome |
| --- | --- |
| Ali buys one Rock Festival ticket | Pays 500.00 |
| Ayse buys four Rock Festival tickets | Pays 1805.00 after both discounts |
| Ali requests zero tickets | Fails with `Invalid Quantity` |
| Ali cancels his purchase | Receives a 500.00 refund |
| Ali attempts the same cancellation again | Fails with `Already Cancelled` |

The active reports then show Rock Festival with four tickets and 1805.00 in revenue, Ayse as the top customer, and four CONCERT tickets. Rock Festival has eight seats remaining.

## Discount configuration

The application creates its system with the following settings.

```java
BookingSystem system = new BookingSystem(events, customers, 0.05, true);
```

`0.05` sets the group discount to 5%. `true` enables it for a single purchase of at least four tickets. Setting the flag to `false` disables the group discount. Premium customers still receive their 5% benefit.

For a premium purchase of four tickets priced at 500.00 each, the calculation is `2000.00 × 0.95 × 0.95 = 1805.00`. The group discount is 100.00 and the premium discount is 95.00.

## Data files

Keep the CSV files in the project root. Each starts with a header row. The current reader supports plain comma-separated fields, with no embedded commas or quoted multiline fields.

```csv
event_id,event_name,category,capacity,ticket_price
```

```csv
customer_id,customer_name,balance,membership_type
```

Event categories are `CONCERT`, `CONFERENCE`, and `SPORTS`. Membership types are `REGULAR` and `PREMIUM`.

## Design

Read the [class diagram and responsibility guide](docs/class-diagram.md) for all classes, enums, and their relationships.

| File or folder | Purpose |
| --- | --- |
| `src/ticketbooking/` | Application source code |
| `TicketBookingApp.java` | Main entry point and automatic demonstration |
| `ModelCheck.java` | Earlier manual development checks, runnable separately |
| `events.csv`, `customers.csv` | Sample starting records |
| `docs/class-diagram.md` | Class relationships and responsibility guide |
| `.gitignore` | IDE settings and compiled output exclusions |

## Behaviour and scope

Available seats are total capacity minus tickets in ACTIVE bookings. Reports exclude cancelled bookings and use the smaller record ID to break ties. The code uses the enum spelling `CANCELED` for cancelled reservations.

Failed bookings create no reservation and leave balances and booking IDs unchanged. Failed cancellations issue no refund. Refunds use the original final payment, including the discounts applied at purchase time.

This version keeps bookings and updated balances in memory. Every run reloads the original CSV data. Monetary values use `double`, with two decimal places for display. The model classes expose mutable objects and lists, so callers should perform purchase and cancellation operations through `BookingSystem` to keep balance and seat changes coordinated. CSV records are expected to contain valid, unique IDs and finite, nonnegative starting values.
