package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Ticket;
import org.example.model.TicketList;
import org.example.util.FlightTimeDurationCalculator;
import org.example.util.LocalTimeDeserializer;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public class TicketReaderApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            log.info("Invalid length, length must be bigger then 0");
            System.out.println("Invalid arguments, use: java TicketReaderApplication <path to json>.json");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
        try {
            TicketList ticketList = objectMapper.readValue(new File(args[0]), TicketList.class);
            List<Ticket> allTickets = ticketList.getTickets();
            List<Ticket> validTickets = new ArrayList<>();
            for (Ticket ticket : allTickets) {
                String origin = ticket.getOrigin();
                String destination = ticket.getDestination();

                if ((origin.equals("VVO") && destination.equals("TLV")) ||
                        (origin.equals("TLV") && destination.equals("VVO"))) {
                    Ticket validTicket = Ticket.builder().
                            carrier(ticket.getCarrier())
                            .price(ticket.getPrice())
                            .arrivalTime(ticket.getArrivalTime())
                            .arrivalDate(ticket.getArrivalDate())
                            .departureTime(ticket.getDepartureTime())
                            .departureDate(ticket.getDepartureDate())
                            .origin(ticket.getOrigin())
                            .destination(ticket.getDestination())
                            .build();

                    validTickets.add(validTicket);
                }
            }

            Map<String, Long> minFlightTime = new HashMap<>();
            for (Ticket ticket : validTickets) {
                long flightTime = FlightTimeDurationCalculator.calculateFlightDuration(ticket);
                minFlightTime.merge(ticket.getCarrier(), flightTime, Math::min);
            }

            List<Long> allPrices = new ArrayList<>();
            for (Ticket ticket : validTickets) {
                allPrices.add(ticket.getPrice());
            }
            Collections.sort(allPrices);

            double middlePrice = allPrices.stream().mapToLong(Long::longValue).average().orElse(0);
            double median;
            if (allPrices.size() % 2 == 0) {
                median = (allPrices.get(allPrices.size() / 2) + allPrices.get(allPrices.size() / 2 - 1)) / 2.0;
            } else {
                median = allPrices.get(allPrices.size() / 2);
            }
            double priceDuration = Math.abs(median - middlePrice);

            System.out.println("Minimal flight time VVO-TLV: ");
            minFlightTime.forEach((k, v) ->
                    System.out.printf("Company: %s, Flight time: %d. %n", k, v));
            System.out.println("============================");
            System.out.printf("Duration between median price and middle price: %.2f rubles", priceDuration);


        } catch (Exception e) {
            log.error("Something went wrong");
            throw new RuntimeException("Tickets read was failed", e);
        }
    }
}