package designPatterns.state;

import designPatterns.state.bankomat.Atm;

public class HackedAtm /*extends Atm*/ {

    //@Override
    public void insertPin(int pin) {
        System.out.println("Twój 'tajny' pin: " + pin);
    }
}
