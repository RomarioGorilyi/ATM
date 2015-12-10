package ua.pti.myatm.exceptions;

public class NotEnoughMoneyInATMException extends Exception {
    public NotEnoughMoneyInATMException() {
        super("Unfortunately, there is no such amount of money in ATM.");
    }
}