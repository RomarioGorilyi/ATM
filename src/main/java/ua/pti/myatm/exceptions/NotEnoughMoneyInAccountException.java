package ua.pti.myatm.exceptions;

public class NotEnoughMoneyInAccountException extends Exception {
    public NotEnoughMoneyInAccountException() {
        super("Unfortunately, there is no such amount of money in your account.");
    }
}