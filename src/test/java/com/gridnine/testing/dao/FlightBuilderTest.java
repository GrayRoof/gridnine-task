package com.gridnine.testing.dao;

import com.gridnine.testing.model.Flight;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightBuilderTest {

    @Test
    void shouldCreateFlights() {
        List<Flight> flights = FlightBuilder.createFlights();
        assertEquals(6, flights.size());

        assertEquals(1, flights.get(0).getSegments().size());
        assertEquals(2, flights.get(1).getSegments().size());
        assertEquals(1, flights.get(2).getSegments().size());
        assertEquals(1, flights.get(3).getSegments().size());
        assertEquals(2, flights.get(4).getSegments().size());
        assertEquals(3, flights.get(5).getSegments().size());
    }


}