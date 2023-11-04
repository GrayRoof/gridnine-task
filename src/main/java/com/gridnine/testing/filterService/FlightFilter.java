package com.gridnine.testing.filterService;

import com.gridnine.testing.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightFilter {

    /**
     * Returns List of Flights in Filter
     * @return 'List<Flight>'
     */
    List<Flight> getResult();

    /**
     * Returns FlightFilter instance with filtered data by departure time
     * of any segment of flight is earlier than the specified date
     * @param dateTime - specified date
     * @return FlightFilter
     */
    FlightFilter departureBefore(LocalDateTime dateTime);

    /**
     * Returns FlightFilter instance with filtered data by arrival time
     * of any segment of flight is earlier than departure time
     * @return FlightFilter
     */
    FlightFilter arrivalBeforeDeparture();

    /**
     * Returns FlightFilter instance with filtered data by total transfer
     * time between segments of flight is more than specified amount of hours
     * @param hours - specified amount of hours
     * @return FlightFilter
     */
    FlightFilter totalTransferMoreThen(long hours);

}
