package strategy;

import model.ParkingFloor;
import model.ParkingSpot;
import model.Vehicle;

import java.util.List;
import java.util.Optional;

public class NearestFirst implements ParkingStrategy {
    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            Optional<ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                return spot;
            }
        }
        return Optional.empty();
    }
}