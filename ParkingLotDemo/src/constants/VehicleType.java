package constants;

public enum VehicleType {
    BIKE, CAR, TRUCK;

    public boolean canFit(VehicleType required) {
        if (this == TRUCK) return true;
        if (this == CAR) return required != TRUCK;
        return this == BIKE && required == BIKE;
    }
}
