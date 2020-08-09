package designPatterns.state.bankomat;

public class NoCashState extends AtmState {
    public NoCashState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard() {
        System.out.println("Karta nie przyjęta - brak pieniędzy w bankomacie!");
    }

    @Override
    public void insertPin(int pin) {
        System.out.println("Pin nie przyjęty - brak pieniędzy w bankomacie!");
    }

    @Override
    public void takeCash(int cash) {
        System.out.println("Nie wypłacimy pieniędzy - brak pieniędzy w bankomacie!");
    }
}
