package com.gridnine.testing.filterService;

import com.gridnine.testing.filterService.exceptions.FlightFilterException;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightFilterImplTest {

    @Test
    void shouldThrowExceptionWhenTryCreateFilterWithNull() {
        assertThrows(FlightFilterException.class, () -> new FlightFilterImpl(null));
    }

    @Test
    void shouldThrowExceptionWhenTryFilterByDepartureBeforeWithNullSegments() {
        assertThrows(FlightFilterException.class, () -> {
            Flight flight = new Flight(null);
            List<Flight> flights = List.of(flight);
            FlightFilter filter = new FlightFilterImpl(flights);
            filter.departureBefore(LocalDateTime.now());
        });
    }

    @Test
    void shouldThrowExceptionWhenTryFilterByArrivalBeforeDepartureWithNullSegments() {
        assertThrows(FlightFilterException.class, () -> {
            Flight flight = new Flight(null);
            List<Flight> flights = List.of(flight);
            FlightFilter filter = new FlightFilterImpl(flights);
            filter.arrivalBeforeDeparture();
        });
    }

    @Test
    void shouldThrowExceptionWhenTryFilterByTotalTransferAtLeastWithNullSegments() {
        assertThrows(FlightFilterException.class, () -> {
            Flight flight = new Flight(null);
            List<Flight> flights = List.of(flight);
            FlightFilter filter = new FlightFilterImpl(flights);
            filter.totalTransferMoreThen(0);
        });
    }

    @Test
    void shouldReturnFilteredResult() {
        Flight flight = new Flight(null);
        List<Flight> flights = List.of(flight);
        FlightFilter filter = new FlightFilterImpl(flights);
        assertEquals(flights, filter.getResult());
    }

    @Test
    void shouldReturnFilteredByDepartureBefore() {
        LocalDateTime now = LocalDateTime.now();
        Segment firstSegment = new Segment(LocalDateTime.MIN, LocalDateTime.MAX);
        Segment secondSegment = new Segment(LocalDateTime.MAX, LocalDateTime.MIN);

        Flight satisfies = new Flight(List.of(firstSegment));
        Flight notSatisfies = new Flight(List.of(secondSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(satisfies, notSatisfies));
        FlightFilter expected = new FlightFilterImpl(List.of(satisfies));
        FlightFilter actual = filter.departureBefore(now);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyWhenFilterByDepartureBeforeSameTime() {
        LocalDateTime now = LocalDateTime.now();
        Segment firstSegment = new Segment(now, LocalDateTime.MAX);
        Flight notSatisfies = new Flight(List.of(firstSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(notSatisfies));
        FlightFilter expected = new FlightFilterImpl(List.of());
        FlightFilter actual = filter.departureBefore(now);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnNotFilteredWhenFlightsNotSatisfiesFilterByDepartureBefore() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        Segment firstSegment = new Segment(future, LocalDateTime.MAX);
        Flight notSatisfies = new Flight(List.of(firstSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(notSatisfies));
        FlightFilter expected = new FlightFilterImpl(List.of());
        FlightFilter actual = filter.departureBefore(LocalDateTime.now());

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFilteredByArrivalBeforeDeparture() {
        Segment firstSegment = new Segment(LocalDateTime.MIN, LocalDateTime.MAX);
        Segment secondSegment = new Segment(LocalDateTime.MAX, LocalDateTime.MIN);

        Flight satisfies = new Flight(List.of(secondSegment));
        Flight notSatisfies = new Flight(List.of(firstSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(satisfies, notSatisfies));
        FlightFilter expected = new FlightFilterImpl(List.of(satisfies));
        FlightFilter actual = filter.arrivalBeforeDeparture();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyWhenFilterByArrivalBeforeDepartureWithSameTime() {
        Segment firstSegment = new Segment(LocalDateTime.MIN, LocalDateTime.MAX);
        Segment secondSegment = new Segment(LocalDateTime.MAX, LocalDateTime.MAX);

        Flight notSatisfies = new Flight(List.of(firstSegment));
        Flight ArrivalEqDeparture = new Flight(List.of(secondSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(notSatisfies, ArrivalEqDeparture));
        FlightFilter expected = new FlightFilterImpl(List.of());
        FlightFilter actual = filter.arrivalBeforeDeparture();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFilteredByTotalTransferMoreThenTwoHours() {
        LocalDateTime now = LocalDateTime.now();

        Segment firstSegment = new Segment(now, now.plusHours(1));
        Segment secondSegment = new Segment(now.plusHours(4), now.plusHours(6));
        Segment thirdSegment = new Segment(now, now.plusDays(1));
        Segment forthSegment = new Segment(now.plusDays(1).plusMinutes(59), now.plusDays(2));
        Segment fifthSegment = new Segment(now, now.plusMinutes(2));

        Flight satisfies = new Flight(List.of(firstSegment, secondSegment));
        Flight notSatisfies = new Flight(List.of(thirdSegment, forthSegment));
        Flight noTransfer = new Flight(List.of(fifthSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(satisfies, notSatisfies, noTransfer));
        FlightFilter expected = new FlightFilterImpl(List.of(satisfies));
        FlightFilter actual = filter.totalTransferMoreThen(2);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyWhenNoneSatisfiesByTotalTransferMoreThenHour() {
        LocalDateTime now = LocalDateTime.now();

        Segment firstSegment = new Segment(now, now.plusHours(1));
        Segment secondSegment = new Segment(now.plusHours(2), now.plusHours(6));
        Segment thirdSegment = new Segment(now, now.plusDays(1));
        Segment forthSegment = new Segment(now.plusDays(1).plusMinutes(1), now.plusDays(2));

        Flight transferHour = new Flight(List.of(firstSegment, secondSegment));
        Flight transferOneMinute = new Flight(List.of(thirdSegment, forthSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(transferHour, transferOneMinute));
        FlightFilter expected = new FlightFilterImpl(List.of());
        FlightFilter actual = filter.totalTransferMoreThen(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAllWhenAllSatisfiesByTotalTransferMoreThenHour() {
        LocalDateTime now = LocalDateTime.now();

        Segment firstSegment = new Segment(now, now.plusHours(1));
        Segment secondSegment = new Segment(now.plusHours(3), now.plusHours(6));
        Segment thirdSegment = new Segment(now, now.plusHours(4));
        Segment forthSegment = new Segment(now.plusHours(10), now.plusDays(2));

        Flight transfer1twoHours = new Flight(List.of(firstSegment, secondSegment));
        Flight transferSixHours = new Flight(List.of(thirdSegment, forthSegment));

        FlightFilter filter = new FlightFilterImpl(List.of(transfer1twoHours, transferSixHours));
        FlightFilter actual = filter.totalTransferMoreThen(1);

        assertEquals(filter, actual);
    }
}