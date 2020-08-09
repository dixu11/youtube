package designPatterns.state.bankomat;

public class NoCardState extends AtmState {

    public NoCardState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard() {
        if (atm.getCash() <= 100) {
            System.out.println("Zbyt mało pieniedzy w bankomacie - bankomat zmienia stan na zablokowany - przyjdź jutro!");
            atm.setState(new NoCashState(atm));
            return;
        }

        atm.setState(new NoPinState(atm));
        System.out.println("Karta przyjęta, zmiana stanu: oczekiwanie pinu");

    }

    @Override
    public void insertPin(int pin) {
        System.out.println("Nie można wprowadzić pinu - najpierw daj karte");
    }

    @Override
    public void takeCash(int cash) {
        System.out.println("Najpierw karta, potem kasa!");
    }
}
