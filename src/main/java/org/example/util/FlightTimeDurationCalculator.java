package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
public class FlightTimeDurationCalculator {
    public static long calculateFlightDuration(Ticket ticket) {
        Map<String, ZoneId> TIME_ZONES = Map.of(
                "VVO", ZoneId.of("Asia/Vladivostok"),
                "TLV", ZoneId.of("Asia/Jerusalem")
        );
        ZoneId originZone = TIME_ZONES.get(ticket.getOrigin());
        ZoneId destinationZone = TIME_ZONES.get(ticket.getDestination());

        if (originZone == null || destinationZone == null) {
            log.error("Invalid airport ZoneID");
            throw new IllegalArgumentException("Invalid airport ZoneID");
        }

        ZonedDateTime departureTime = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime())
                .atZone(originZone);
        ZonedDateTime arrivalTime = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime())
                .atZone(destinationZone);

        return ChronoUnit.MINUTES.between(departureTime, arrivalTime);
    }
}