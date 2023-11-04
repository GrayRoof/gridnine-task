package com.gridnine.testing.filterService;

import com.gridnine.testing.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightFilter {


    List<Flight> getResult();
    FlightFilter departureBefore(LocalDateTime dateTime);
    FlightFilter arrivalBeforeDeparture();
    FlightFilter totalTransferAtLeast(long hours);

}
