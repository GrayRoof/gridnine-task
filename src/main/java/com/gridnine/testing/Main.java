package com.gridnine.testing;

import com.gridnine.testing.dao.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.filterService.FlightFilter;
import com.gridnine.testing.filterService.FlightFilterImpl;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        FlightFilter filter = new FlightFilterImpl(flights);

        List<Flight> departureBeforeNow = filter.departureBefore(LocalDateTime.now()).getResult();
        List<Flight> arrivalBeforeDeparture = filter.arrivalBeforeDeparture().getResult();
        List<Flight> totalTransferAtLeastTwoHours = filter.totalTransferMoreThen(2).getResult();

        String result = "All flights: \n" +
                flights + "\n\n" +
                "Only Flights where Departure time is before now: \n" +
                departureBeforeNow + "\n\n" +
                "Only Flights where Arrival time is before Departure time: \n" +
                arrivalBeforeDeparture + "\n\n" +
                "Only Flights where total transfer time is more then two hours: \n" +
                totalTransferAtLeastTwoHours + "\n\n";

        System.out.println(result);
    }
}