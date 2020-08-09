package designPatterns.state.bankomat;

public class ReadyState extends AtmState {
    public ReadyState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard() {
        System.out.println("Karta już wprowadzona");
    }

    @Override
    public void insertPin(int pin) {
        System.out.println("Pin już wprowadzony, możesz wypłacać");
    }

    @Override
    public void takeCash(int cash) {
        if (cash > atm.getCash()) {
            System.out.println("Zbyt duża ilość pieniędzy - wprowadź mniejszą kwotę");
            return;
        } else if (cash <= 0) {
            System.out.println("Należy podać dodatnią wartość");
            return;
        }

        System.out.println("Wypłacam gotówkę!");
        atm.decreaseCash(cash);
        atm.setState(new NoCardState(atm));
        System.out.println("Zwracam karte, miłego dnia");
    }
}
