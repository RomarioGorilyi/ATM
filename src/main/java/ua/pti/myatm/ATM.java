package ua.pti.myatm;

import ua.pti.myatm.exceptions.NoCardInsertedException;
import ua.pti.myatm.exceptions.NotEnoughMoneyInATMException;
import ua.pti.myatm.exceptions.NotEnoughMoneyInAccountException;

public class ATM {

    private double moneyInATM;
    private Card card;
    double maxAmountOfMoney = 500000;

    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM) {
        if (moneyInATM < 0) {
            throw new IllegalArgumentException("Amount of money in ATM can't be negative.");
        }
        else if (moneyInATM > maxAmountOfMoney) {
            throw new IllegalArgumentException("Amount of money in ATM can't be bigger than allowed max amount of cash in ATM.");
        }
        this.moneyInATM = moneyInATM;
        this.card = null;
    }

    // Возвращает каоличестов денег в банкомате
    public double getMoneyInATM() {
        return moneyInATM;
    }

    public boolean insertCard(Card card) {
        if (this.card != null) {
            throw new UnsupportedOperationException("Card is already inserted in ATM.");
        }
        this.card = card;
        return true;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode) throws NoCardInsertedException {
        if (card.isBlocked())
        {
            System.out.println("Card is blocked.");
            return false;
        }
        else if(!card.checkPin(pinCode)) {
            System.out.println("Incorrect password.");
            return false;
        }
        else {
            this.card = card;
            return true;
        }
    }

    //Возвращает сколько денег есть на счету
    public double checkBalance() throws NoCardInsertedException {
        if (card.isBlocked()){
            throw new IllegalArgumentException("Card is blocked");
        }
        return this.card.getAccount().getBalance();
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws NoCardInsertedException, NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException {
        Account account = card.getAccount();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount of expected to get money can't be negative.");
        }
        else {
            if (amount > this.getMoneyInATM()) {
                throw new NotEnoughMoneyInATMException();
            } else if (amount > this.checkBalance()) {
                throw new NotEnoughMoneyInAccountException();
            } else {
                moneyInATM -= account.withdrow(amount);
            }
            return this.checkBalance();
        }
    }

    public void getCardFromATM() throws NoCardInsertedException {
        if (this.card != null) {
            this.card = null;
        }
        else {
            throw new NoCardInsertedException();
        }
    }
}
