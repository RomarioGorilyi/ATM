package ua.pti.myatm.exceptions;

public class NoCardInsertedException extends Exception {
    public NoCardInsertedException() {
        super("There is no card in ATM.");
    }
}