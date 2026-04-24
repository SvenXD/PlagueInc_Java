package game_pkg.cure_pkg;

public class Cure {

    private double progress;
    private double funds;

    // 58 countries, 58 funds for 150 days
    public Cure(){
        this.progress = 0.0;
        this.funds = 0.0;
    }

    public void addToFunds(double funds){
        this.funds += funds;
    }

    public void resetFunds(){
        this.funds = 0.0;
    }

    public void updateCureProgress(){     // 100 / 150 = === 2/3
        this.progress += ((2.0/3.0) / 58.0) * funds;
    }

    public void popCureBubble(){
        this.progress -= 0.05;
    }

    public double cureProgress(){
       return  this.progress;
    }
}
