import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingAccount extends ReentrantLock {
    volatile Double balance;
    Lock lock;
    volatile int holdCount;
    Condition condition;

    // Constructor
    SavingAccount(){
        lock = new ReentrantLock();
        condition = lock.newCondition();
        balance = 0.0;
        holdCount = 0;
    }

    // subtracts amount if balance is at least amount.
    // otherwise blocks until the balance == amount or greater.
    void withdraw(float amount) throws InterruptedException {
        lock.lock();
        // checks if balance is enough to withdraw
        if(balance < amount) {
            condition.await();
        }else{
            balance = balance - amount;
            System.out.println("balance: " + balance + ", withdraw amount: " + balance);
        }
        lock.unlock();
        condition.signalAll();
    }

    // adds amount to the balance
    void deposit(float amount) {
        lock.lock();
        balance += amount;
        System.out.println("Deposit: " + amount);
        System.out.println("Balance: " + balance);
        lock.unlock();
    }
}
