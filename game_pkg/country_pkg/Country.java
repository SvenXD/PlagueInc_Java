package game_pkg.country_pkg;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game_pkg.country_pkg.Enums.Climate;
import game_pkg.country_pkg.Enums.ConnectionType;
import game_pkg.country_pkg.Enums.Location;
import game_pkg.country_pkg.Enums.Wealth;
import game_pkg.virus_pkg.Plague;

public class Country {

    private final int population;
    private int healthyPopulation;
    private int deadPopulation;
    private double infectedPopulation;
    private int daysPerTravel;
    private String name;
    private List<Connection> countryConnections = new ArrayList<>();
    private double maxContribution;

    private Climate climate;
    private Location location;
    private Wealth wealth;

    private boolean airportsOpen = true;
    private boolean portsOpen = true;
    private boolean bordersOpen = true;

    public Country(int population, String name, Climate climate, Location location, Wealth wealth, int daysPerTravel, double maxContribution){
        this.name = name;
        this.population = population;
    
        this.climate = climate;
        this.location = location;
        this.wealth = wealth;

        this.healthyPopulation = population;
        this.infectedPopulation = 0.0;
        this.deadPopulation = 0;
        this.daysPerTravel = daysPerTravel;

        this.maxContribution = maxContribution;
    }

    public void infectCountry(){
        if(infectedPopulation == 0.0){
            infectedPopulation = 1.0;
            healthyPopulation --;
        }
    }

    /*  
    TODO: Redo for the plague when its can develop abilities 
    */

    public void recalculateInfected(Plague plague){
        if(infectedPopulation > population){
            return;
        }
        double growthBonus = 0.07;

        if(this.climate == Climate.COLD || this.climate == Climate.HOT){
            growthBonus *= 0.8;
        }

        if(this.climate == Climate.COLD){
            if(plague.getColdResistanceLevel() == 0){
                growthBonus *= 0.8;
            }else if(plague.getColdResistanceLevel() == 1){
                growthBonus *= 0.9;
            }
        }else if (this.climate == Climate.HOT){
            if(plague.getHotResistanceLevel() == 0){
                growthBonus *= 0.8;
            }else if(plague.getHotResistanceLevel() == 1){
                growthBonus *= 0.9;
            }
        }

        if(this.wealth != Wealth.POOR){
            if(this.wealth == Wealth.MIDDLE && plague.getDrugResistanceLevel() == 0){
                growthBonus *= 0.8;
            }else if(this.wealth == Wealth.RICH){
                if(plague.getDrugResistanceLevel() == 1){
                    growthBonus *= 0.6;
                }else if(plague.getDrugResistanceLevel() == 0){
                    growthBonus *= 0.4;
                }
            }                
        }

        int oldInfected = (int)infectedPopulation;

        infectedPopulation *= (1.0 + growthBonus - new Random().nextDouble(0,0.005));

    double deltaInfected = infectedPopulation - oldInfected;
    if(deltaInfected >= 1.0) {
        healthyPopulation -= (int) deltaInfected;
    }
    if(healthyPopulation < 0){
        healthyPopulation = 0;
    }

        if(infectedPopulation > population){
            infectedPopulation = population;
            infectedPopulation -= deadPopulation;
            healthyPopulation = 0;
        }
    }

    public void recalculateDeadCount(Plague plague){
        double deathrate = plague.getLethality() * 0.1;
        int deaths = (int)(infectedPopulation*deathrate);

        deadPopulation += deaths;
       if(infectedPopulation <= 0) infectedPopulation = 0;
       if(deadPopulation >= population){ deadPopulation = population;}

    }

    public int getTotalPopulation(){
        return population;
    }

    public int getInfectedPopulation(){
        return (int)infectedPopulation;
    }

    public double getInfectionPercentage() {
        return ((double) infectedPopulation / population) * 100;
    }

    public int getDeadPopulation(){
        return deadPopulation;
    }

    public int getHealthyPopulation(){
        return healthyPopulation;
    }

    public int getDaysPerTravel(){
        return daysPerTravel;
    }

    public String getCountryName(){
        return name;
    }

    public double getFundingContribution(Plague plague, double globalPercentajePopulationInfected){

        double infectionPercent = getInfectionPercentage();
        double localThreat;

        if (infectionPercent < 10) {
            localThreat = 0.2;
        } else if (infectionPercent < 30) {
            localThreat = 0.5;
        } else if (infectionPercent < 60) {
            localThreat = 0.8;
        } else {
            localThreat = 1.0;
        }

        return this.maxContribution * (plague.getSeverity()*2) * localThreat * globalPercentajePopulationInfected;
    }

    public void addConnection(Country c, ConnectionType cType, int travelDays){
        countryConnections.add(new Connection(c, cType, travelDays));
    }

    public List<Connection> getConnections(){
        return countryConnections;
    }
}
