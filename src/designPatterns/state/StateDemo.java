package designPatterns.state;

import designPatterns.state.bankomat.Atm;
import designPatterns.state.bankomat.ReadyState;

public class StateDemo {

    //STATE
    public static void main(String[] args) {
        Atm atm = new Atm();
        /*atm.insertCard();
        atm.insertPin(1234);
        atm.takeCash(9990);
        //atm.takeCash(100);
        atm.insertCard();
        atm.insertCard();
        */
       atm.insertPin(2345);
        atm.takeCash(10_000);


    }
}
