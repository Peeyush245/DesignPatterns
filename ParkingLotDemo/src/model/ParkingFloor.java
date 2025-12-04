package model;

import java.util.*;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>(spots);
    }

    public List<ParkingSpot> getSpots() { return Collections.unmodifiableList(spots); }

    public int getFloorNumber() { return floorNumber; }

    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.getSize().canFit(vehicle.getSize()) && spot.isFree()) {
                return Optional.of(spot);
            }
        }
        return Optional.empty();
    }
}
