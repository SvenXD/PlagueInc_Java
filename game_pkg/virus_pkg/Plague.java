package game_pkg.virus_pkg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plague {
    
    private PlagueType plagueType;
    private double infection, severity, lethality;
    private List<Habilities> habilities;

    private double dnaPoints;

    public Plague(PlagueType plagueType){
        this.plagueType = plagueType;
        dnaPoints = 3; 
        this.infection = 1.0;
        this.severity = 0.5;
        this.lethality = 0.0;
        habilities = new ArrayList<>();
    }

    public void addHability(Habilities habilities){
        this.habilities.add(habilities);
    }

    public double getInfection(){
        return infection;
    }

    public double getSeverity(){
        return severity;
    }

    public double getLethality(){
        return lethality;
    }

    public int getDNAPoints(){
        return (int)dnaPoints;
    }

    public void addToDNA(int addition){
        this.dnaPoints += addition;
    }

    public void popBioHazardBubble(){
        if(this.severity >= 0.3){
            this.dnaPoints += new Random().nextInt(3, Math.min((int)(this.getSeverity() * 10), 6));
        }else{
            this.dnaPoints += new Random().nextInt(1,2);  
        }
    }

    public void popDNABubble(){
        if(this.severity >= 0.2){
            this.dnaPoints += new Random().nextInt(2, Math.min((int)(this.getSeverity() * 10), 6));
        }else{
            this.dnaPoints += new Random().nextInt(1,2);  
        }
    }

    public int getDrugResistanceLevel(){
        if (habilities.contains(Habilities.DRUG_RES_II)){
            return 2;
        }else if(habilities.contains(Habilities.DRUG_RES_I)){
            return 1;
        }
        return 0;
    }
    
    public int getColdResistanceLevel(){
        if (habilities.contains(Habilities.COLD_RES_II)){
            return 2;
        }else if(habilities.contains(Habilities.COLD_RES_I)){
            return 1;
        }
        return 0;
    }

    public int getHotResistanceLevel(){
        if (habilities.contains(Habilities.HOT_RES_II)){
            return 2;
        }else if(habilities.contains(Habilities.HOT_RES_I)){
            return 1;
        }
        return 0;
    }
}
