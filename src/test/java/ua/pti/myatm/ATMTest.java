/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import org.junit.Test;
import org.mockito.InOrder;
import ua.pti.myatm.exceptions.NoCardInsertedException;
import ua.pti.myatm.exceptions.NotEnoughMoneyInATMException;
import ua.pti.myatm.exceptions.NotEnoughMoneyInAccountException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ATMTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInputNegativeAmountOfMoneyInATM() {
        System.out.println("checkATMForNegativeValueInput");
        ATM atmTest = new ATM(-100.0);
        //System.out.println(atmTest.getMoneyInATM());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testATMForOverflowInput() {
        System.out.println("checkATMForOverflowInput");
        ATM atmTest = new ATM(999999999);
    }

    @Test
    public void testGetMoneyInATM() {
        System.out.println("getMoneyInATM");
        ATM atmTest = new ATM(100.0);
        double expResult = 100.0;
        double result = atmTest.getMoneyInATM();
        assertEquals(expResult, result, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInsertMoreThanOneCard() {
        System.out.println("insertMoreThanOneCard");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        atmTest.insertCard(mockCard);
        atmTest.insertCard(mockCard);
    }

    @Test
    public void testInsertOnlyOneCard() {
        System.out.println("insertOnlyOneCard");
        ATM atmTest = new ATM(0.0);
        Card mockCard = mock(Card.class);
        boolean result = atmTest.insertCard(mockCard);
        assertTrue(result);
    }

    @Test
    public void testValidateBlockedCard() throws NoCardInsertedException {
        System.out.println("validateBlockedCard");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        int pinCode = 7777;
        when(mockCard.isBlocked()).thenReturn(true);
        when(mockCard.checkPin(pinCode)).thenReturn(true);
        assertFalse(atmTest.validateCard(mockCard, pinCode));
        verify(mockCard).isBlocked();
    }

    @Test
    public void testValidateCardWithInvalidPin() throws NoCardInsertedException {
        System.out.println("validateCardWithInvalidPin");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        int pinCode = 7777;
        when(mockCard.isBlocked()).thenReturn(false);
        when(mockCard.checkPin(pinCode)).thenReturn(false);
        assertFalse(atmTest.validateCard(mockCard, pinCode));
    }

    @Test(expected = NullPointerException.class)
    public void testValidateCardNullCard() throws NoCardInsertedException {
        System.out.println("validateNullCard");
        ATM atmTest = new ATM(100);
        int pinCode = 7777;
        atmTest.validateCard(null, pinCode);
    }

    @Test
    public void testValidateValidCard() throws NoCardInsertedException {
        System.out.println("validateValidCard");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        int pinCode = 7777;
        when(mockCard.isBlocked()).thenReturn(false);
        when(mockCard.checkPin(pinCode)).thenReturn(true);
        boolean expResult = true;
        boolean result = atmTest.validateCard(mockCard, pinCode);
        assertEquals(expResult, result);
        InOrder inOrder = inOrder(mockCard);
        inOrder.verify(mockCard).isBlocked();
        inOrder.verify(mockCard).checkPin(pinCode);
    }

    @Test
    public void testCheckBalance() throws NoCardInsertedException {
        System.out.println("checkBalance");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        Account mockAccount = mock(Account.class);
        atmTest.insertCard(mockCard);
        when(mockCard.getAccount()).thenReturn(mockAccount);
        when(mockAccount.getBalance()).thenReturn(0.0);
        double expResult = 0.0;
        double result = atmTest.checkBalance();
        assertEquals(expResult, result, 0.0);
        InOrder inOrder = inOrder(mockCard, mockAccount);
        inOrder.verify(mockCard).getAccount();
        inOrder.verify(mockAccount).getBalance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCashLessThanZero() throws NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException,
            NoCardInsertedException {
        System.out.println("getCashLessThanZero");
        double amount = -100.0;
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        atmTest.insertCard(mockCard);
        Account mockAccount = mock(Account.class);
        when(mockCard.getAccount()).thenReturn(mockAccount);
        atmTest.getCash(amount);
    }

    @Test(expected = NotEnoughMoneyInATMException.class)
    public void testGetCashNotEnoughMoneyInATM() throws NoCardInsertedException, NotEnoughMoneyInAccountException,
            NotEnoughMoneyInATMException {
        System.out.println("getCashNotEnoughMoneyInATM");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        Account mockAccount = mock(Account.class);
        double amount = 1100.0;
        when(mockCard.getAccount()).thenReturn(mockAccount);
        atmTest.getCash(amount);
        InOrder inOrder = inOrder(mockCard, mockAccount);
        inOrder.verify(mockCard).getAccount();
        inOrder.verify(mockAccount).getBalance();
    }

    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void testGetCashNotEnoughMoneyInAccount() throws NoCardInsertedException, NotEnoughMoneyInAccountException,
            NotEnoughMoneyInATMException {
        System.out.println("getCashNotEnoughMoneyInAccount");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        Account mockAccount = mock(Account.class);
        double balance = 200.0;
        double amount = 500.0;
        when(mockCard.getAccount()).thenReturn(mockAccount);
        when(mockAccount.getBalance()).thenReturn(balance);
        when(mockCard.getAccount().withdrow(amount)).thenReturn(balance - amount);
        atmTest.getCash(amount);
        InOrder inOrder = inOrder(mockCard, mockAccount);
        inOrder.verify(mockCard).getAccount();
        inOrder.verify(mockAccount).getBalance();
    }

    @Test
    public void testGetCash() throws NotEnoughMoneyInAccountException, NotEnoughMoneyInATMException,
            NoCardInsertedException {
        System.out.println("getCashNotZeroBalance");
        double atmMoney = 1000.0;
        ATM atmTest = new ATM(atmMoney);
        Card mockCard = mock(Card.class);
        Account mockAccount = mock(Account.class);
        double balance = 600.0;
        double amount = 100.0;
        when(mockCard.getAccount()).thenReturn(mockAccount);
        when(mockAccount.getBalance()).thenReturn(balance);
        when(mockAccount.withdrow(amount)).thenReturn(amount);
        atmTest.getCash(amount);
        when(mockAccount.getBalance()).thenReturn(balance - amount);
        assertEquals(atmTest.getMoneyInATM(), atmMoney - amount, 0.0);
        assertEquals(atmTest.checkBalance(), balance - amount, 0.0);
        InOrder inOrder = inOrder(mockCard, mockAccount);
        inOrder.verify(mockCard, times(4)).getAccount();
        verify(mockAccount).withdrow(amount);
        inOrder.verify(mockAccount).getBalance();
    }

    @Test(expected = NoCardInsertedException.class)
    public void testGetCardFromATMIfCardIsNotInserted() throws NotEnoughMoneyInATMException, NotEnoughMoneyInAccountException,
            NoCardInsertedException {
        System.out.println("getCardFromATMIfCardIsNotInserted");
        ATM atmTest = new ATM(1000.0);
        atmTest.getCardFromATM();
    }

    @Test(expected = NoCardInsertedException.class)
    public void testGetCardFromATMMoreThanOneTime() throws NoCardInsertedException {
        System.out.println("getCardFromATMMoreThanOneTime");
        ATM atmTest = new ATM(1000.0);
        Card mockCard = mock(Card.class);
        atmTest.insertCard(mockCard);
        atmTest.getCardFromATM();
        atmTest.getCardFromATM();
    }
}