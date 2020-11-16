package files;

import event.*;
import interfaces.ReminderInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Classe Reminder
 * <p>
 * Notificar o utilizador do começo de um evento
 *
 * @author Joao Vicente
 */
public class Reminder extends UnicastRemoteObject implements ReminderInterface {
    /**
     * Construtor
     *
     * @throws RemoteException
     */
    public Reminder() throws RemoteException {
    }

    /**
     * Método notify
     * <p>
     * Imprime o evento e uma mesnagem
     *
     * @param event   Um objeto do tipo evento
     * @param message Mesagem a apresentar
     * @throws RemoteException
     */
    @Override
    public void notify(Event event, String message) throws RemoteException {
        System.out.println(event.toString() + message);
    }
}
