package designPatterns.state.bankomat;

public class NoPinState extends AtmState {
    public NoPinState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard() {
        System.out.println("Najpierw wprowadź pin, karta nie została przyjęta");
    }

    @Override
    public void insertPin(int pin) {
        if (pin == 1234) {
            System.out.println("Pin poprawny, zmiana stanu na: gotowy do wypłaty");
            atm.setState(new ReadyState(atm));
        } else {
            System.out.println("Niepoprawny pin");
        }

    }

    @Override
    public void takeCash(int cash) {
        System.out.println("Nie można wypłacić, wpisz pin!");
    }
}
