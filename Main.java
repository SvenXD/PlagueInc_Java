import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game_pkg.country_pkg.Connection;
import game_pkg.country_pkg.Country;
import game_pkg.country_pkg.TravelGroup;
import game_pkg.country_pkg.Enums.Climate;
import game_pkg.country_pkg.Enums.ConnectionType;
import game_pkg.country_pkg.Enums.Location;
import game_pkg.country_pkg.Enums.Wealth;
import game_pkg.cure_pkg.Cure;
import game_pkg.virus_pkg.Habilities;
import game_pkg.virus_pkg.Plague;
import game_pkg.virus_pkg.PlagueType;
import javafx.application.Application;
import ui_pkg.ConceptOfMap;

public class Main {
    static final List<TravelGroup> travelList = new ArrayList<>();
    static int totalInfectedPeople = 1;

    public static void main(String[] args) {

        new Thread(() -> Application.launch(ConceptOfMap.class, args)).start();
        
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        int baseDelayMs = 290; // 100ms per day = 10 days per second
        int speedMultiplier = 1;
        int daysPassed = 0;

        boolean running = true;

        final List<Country> countries = new ArrayList<>();

        countries.add(new Country(72000000, "France", Climate.ARID, Location.RURAL, Wealth.POOR, 4,2));
        countries.add(new Country(100000, "UK", Climate.ARID, Location.RURAL, Wealth.RICH, 4,2));
        countries.add(new Country(300000, "Mexico", Climate.ARID, Location.RURAL, Wealth.MIDDLE, 3,1));
        countries.add(new Country(400000, "USA", Climate.ARID, Location.RURAL, Wealth.RICH, 4,3));

        countries.get(0).addConnection(countries.get(1), ConnectionType.AIR, 2);
        countries.get(1).addConnection(countries.get(2), ConnectionType.AIR, 4);
        countries.get(1).addConnection(countries.get(3), ConnectionType.AIR, 3);

        countries.get(0).infectCountry();


        Plague m_plague = new Plague(PlagueType.VIRUS);
        m_plague.addHability(Habilities.DRUG_RES_II);

        int deltaInfections = 0;
        int lastDeltaInfections = 0;

        Cure m_cure = new Cure();
        

        while (running) {
            long startTime = System.currentTimeMillis();

            /*
             * Insider logic
             */
            for (double i = 0; i < speedMultiplier; i++) {
                daysPassed++;

                updateDeathForAll(countries,m_plague);

                checkArrivals(daysPassed, m_plague);

                calculateIfConnectionInfected(countries, daysPassed);

                deltaInfections =  recalculateInfectedPerCountry(countries, m_plague, deltaInfections);
                //System.out.println("Delta: " + deltaInfections);
                //System.out.println("Last delta: " + lastDeltaInfections);
                lastDeltaInfections = calculateDNAPerPeriodInfection(daysPassed, m_plague, deltaInfections, lastDeltaInfections);


                /*   Cure */
                m_cure.addToFunds(calculateCureFunds(countries, m_plague, 1));
                m_cure.updateCureProgress();
                m_cure.resetFunds();

                /*  UI */

                for(Country c : countries){
                    if(c.checkIfFirstInfection()){
                            ConceptOfMap.getInstance().showBubbleAt(623.8, 252.8);
                    }
                }



                System.out.println("Day: " + daysPassed);
                System.out.println("France Infected: " + countries.get(0).getInfectedPopulation());
                System.out.println("Mexico Infected: " + countries.get(2).getInfectedPopulation());
                System.out.println("UK Infected: " + countries.get(1).getInfectedPopulation());
                System.out.println("USA Infected: " + countries.get(3).getInfectedPopulation());

                /*System.out.println("France infected: " + countries.get(0).getInfectedPopulation());
                System.out.println("France dead: " + countries.get(0).getDeadPopulation());
                System.out.println("France healty " + countries.get(0).getHealthyPopulation());

                System.out.println("DNA Points:  " + m_plague.getDNAPoints());*/
                

            }

            /* Thread logic and suff */
            int delayMs = baseDelayMs / speedMultiplier;

            long timeTaken = System.currentTimeMillis() - startTime;
            long sleepTime = delayMs - timeTaken;

            try {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                running = false;
            }

            // TODO: debug
            if (daysPassed >= 500)
                running = false;
        }
    }

    public static int recalculateInfectedPerCountry(List<Country> c, Plague plague, int deltaInfections) {
        deltaInfections = totalInfectedPeople;
        totalInfectedPeople = 0;
        for (Country country : c) {
            country.recalculateInfected(plague);
            totalInfectedPeople += country.getInfectedPopulation();
        }
        return totalInfectedPeople - deltaInfections;
    }

    public static void calculateIfConnectionInfected(List<Country> c, int day) {
        for (Country country : c) {
            if ((day % country.getDaysPerTravel() == 0) && !country.getConnections().isEmpty()) {
                double contagiousness = 50.0;
                double chance = ((double) country.getInfectedPopulation() / country.getTotalPopulation()) * 100
                        * contagiousness;
                if (chance > new Random().nextDouble(0.0, 100)) {
                    Connection con = country.getConnections()
                            .get(new Random().nextInt(country.getConnections().size()));

                    if(con.getTypeOfConnection() == ConnectionType.LAND){
                        con.getDestination().infectCountry();
                        continue;
                    }
                    travelList.add(new TravelGroup(con.getDestination(), day + con.getTravelDays()));
                }
            }
        }
    }

    public static void checkArrivals(int currentDay, Plague plague) {
        for (int i = travelList.size() - 1; i >= 0; i--) {
            TravelGroup group = travelList.get(i);

            if (currentDay >= group.getArrivalDay()) {
                if(group.getDestination().getInfectedPopulation() == 0.0){
                    plague.popBioHazardBubble();
                }
                group.getDestination().infectCountry();
                //System.out.println(">>> VIRUS LANDED in " + group.getDestination().getCountryName() + "!");
                travelList.remove(i); 
            }
        }
    }

    public static int calculateDNAPerPeriodInfection(int currentDay, Plague m_plague, int deltaInfections, int lastDeltaInfections){
        if(deltaInfections > (lastDeltaInfections * 1.06) && lastDeltaInfections != 0){ // 1.05 good enough
            int infection = (5 < new Random().nextInt(0,10)) ? 1 : 0;

            
            infection += new Random().nextInt(0,(int)(m_plague.getSeverity()*10));

            m_plague.addToDNA(infection);
            System.out.println("+ " + infection);
        }
        lastDeltaInfections = deltaInfections;
        return lastDeltaInfections;
    }

    public static int calculateCureFunds(List<Country> list, Plague plague, double percentageInfected){ //0 -> 1
        int contribution = 0;

        for(Country c:list){
            contribution += c.getFundingContribution(plague, percentageInfected);
        }

        return contribution;
    }

    public static void updateDeathForAll(List<Country> list, Plague plague){
        for(Country c: list){
            c.recalculateDeadCount(plague);
        }
    }
}
