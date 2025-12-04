package helper;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import strategy.ParkingStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SpotManager {
    private final ConcurrentMap<String, ParkingSpot> spotById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Ticket> placeToTicket = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Ticket> ticketIndex = new ConcurrentHashMap<>();

    private final ParkingStrategy strategy;
    private final List<ParkingFloor> floors;

    private static final int CLAIM_RETRY = 3;

    public SpotManager(List<ParkingFloor> floors, ParkingStrategy strategy) {
        this.floors = Objects.requireNonNull(floors);
        this.strategy = Objects.requireNonNull(strategy);
    }

    public Optional<Ticket> parkVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        final String place = vehicle.getVehiclePlate();

        Ticket existing = placeToTicket.get(place);
        if (existing != null) return Optional.of(existing);

        for (int attempt = 0; attempt < CLAIM_RETRY; attempt++) {
            Optional<ParkingSpot> candidateSpot = strategy.findSpot(floors, vehicle);
            if (candidateSpot.isEmpty()) {
                return Optional.empty();
            }

            ParkingSpot candidate = candidateSpot.get();
            Ticket ticket = Ticket.create(candidate.getId(), vehicle.getVehiclePlate(), vehicle.getSize());

            if (candidate.tryClaim(ticket)) {
                spotById.putIfAbsent(candidate.getId(), candidate);
                placeToTicket.put(place, ticket);
                ticketIndex.put(ticket.ticketId(), ticket);
                return Optional.of(ticket);
            }
        }

        return Optional.empty();
    }

    public boolean exitVehicle(String ticketId) {
        Objects.requireNonNull(ticketId);
        Ticket t = ticketIndex.get(ticketId);
        if (t == null) return false; // unknown ticket

        ParkingSpot spot = spotById.get(t.spotId());
        if (spot == null) return false;

        boolean released = spot.tryRelease(t);
        if (released) {
            ticketIndex.remove(ticketId, t);
            placeToTicket.remove(t.vehiclePlate(), t);
            return true;
        }

        if (spot.currentTicket().isEmpty()) {
            ticketIndex.remove(ticketId, t);
            placeToTicket.remove(t.vehiclePlate(), t);
            return true;
        }

        return false;
    }
}


