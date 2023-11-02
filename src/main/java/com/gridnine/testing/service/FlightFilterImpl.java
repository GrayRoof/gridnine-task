package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightFilterImpl implements FlightFilter {
    private final List<Flight> initialFlights;

    public FlightFilterImpl(List<Flight> initialFlights) {
        this.initialFlights = initialFlights;
    }

    @Override
    public List<Flight> getResult() {
        return initialFlights;
    }

    @Override
    public FlightFilter departureBefore(LocalDateTime dateTime) {
        List<Flight> filtered = initialFlights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getDepartureDate().isBefore(dateTime)))
                .collect(Collectors.toList());
        return new FlightFilterImpl(filtered);
    }

    @Override
    public FlightFilter arrivalBeforeDeparture() {
        List<Flight> filtered = initialFlights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
                .collect(Collectors.toList());
        return new FlightFilterImpl(filtered);
    }

    @Override
    public FlightFilter totalTransferAtLeast(long hours) {
        List<Flight> filtered = initialFlights.stream()
                .filter(flight -> getTotalTransferTime(flight.getSegments()) > hours)
                .collect(Collectors.toList());
        return new FlightFilterImpl(filtered);
    }

    private long getTotalTransferTime(List<Segment> segments) {
        Duration totalDuration = Duration.ZERO;
        Duration currentDuration;
        for (int i = 1; i < segments.size(); i++) {
            currentDuration = Duration.between(
                    segments.get(i - 1).getArrivalDate(),
                    segments.get(i).getDepartureDate());
            if (!currentDuration.isNegative()) {
                totalDuration = totalDuration.plus(currentDuration);
            }
        }
        return totalDuration.toHours();
    }
}
