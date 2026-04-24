package game_pkg.country_pkg;

public class TravelGroup {
    private Country destination;
    private int arrivalDay;

    public TravelGroup(Country destination, int arrivalDay) {
        this.destination = destination;
        this.arrivalDay = arrivalDay;
    }

    public Country getDestination() { return destination; }
    public int getArrivalDay() { return arrivalDay; }
}