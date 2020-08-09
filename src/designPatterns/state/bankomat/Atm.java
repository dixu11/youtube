package designPatterns.state.bankomat;

public final class Atm {

    private AtmState state;
    private double cash;

    public Atm() {
        state = new NoCardState(this);
        cash = 10_000;
    }

   public void insertCard(){
        state.insertCard();
   }

   public  void insertPin(int pin){
        state.insertPin(pin);
   }

   public void takeCash(int cash){
        state.takeCash(cash);
   }

     void setState(AtmState state) {
        this.state = state;
    }

     double getCash() {
        return cash;
    }

     void decreaseCash(double cash) {
        this.cash -= cash;
    }
}
