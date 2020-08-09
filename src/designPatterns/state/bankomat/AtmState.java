package designPatterns.state.bankomat;

public abstract class AtmState {

    Atm atm;

    public AtmState(Atm atm) {
        this.atm = atm;
    }

    public abstract void insertCard();

    public abstract void insertPin(int pin);

    public abstract void takeCash(int cash);
}
