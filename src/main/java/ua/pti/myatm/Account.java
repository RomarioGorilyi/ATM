package ua.pti.myatm;

public interface Account {

    //Возвращает баланс на счете
    public double getBalance();

    //Снимает указанную сумму со счета
    //Возвращает сумму, которая была снята
    public double withdrow(double amount);
    
}
