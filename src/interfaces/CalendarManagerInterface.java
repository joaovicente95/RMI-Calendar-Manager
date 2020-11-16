package interfaces;

import event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface CalendarManagerInterface extends Remote {
    public boolean add(String user, Event event) throws RemoteException;

    public boolean update(String user, Event event, Event newEvent) throws RemoteException;

    public boolean remove(String user, Event event) throws RemoteException;

    public ArrayList<Event> list(String user) throws RemoteException;

    public boolean register(String user, ReminderInterface reminderInterface) throws RemoteException;

    public boolean unregister(String user) throws RemoteException;
}
