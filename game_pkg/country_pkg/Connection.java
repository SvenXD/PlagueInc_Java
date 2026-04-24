package game_pkg.country_pkg;

import game_pkg.country_pkg.Enums.ConnectionType;

public class Connection {
    private Country destinationCountry;
    private ConnectionType connection;
    private int travelDays;

    public Connection(Country destinationCountry, ConnectionType connection, int travelDays){
        this.destinationCountry = destinationCountry;
        this.connection = connection;
        this.travelDays = travelDays;
    }

    public ConnectionType getTypeOfConnection(){
        return connection;
    }

    public Country getDestination(){
        return destinationCountry;
    }

    public int getTravelDays(){
        return travelDays;
    }
}
