package interfaces;

import event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface Reminder
 *
 * @author Joao Vicente
 */
public interface ReminderInterface extends Remote {
    public void notify(Event event, String message) throws RemoteException;
}
