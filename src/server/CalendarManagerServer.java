package server;

import event.Event;
import files.CalendarManager;
import interfaces.ReminderInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Classe CalendarManagerServer
 * <p>
 * Parte do servidor, onde é lançado o servidor e alertas de eventos
 *
 * @author Joao Vicente
 */
public class CalendarManagerServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            CalendarManager calendar = new CalendarManager();
            Naming.rebind("Calendar", calendar);
            System.out.println("Server ready!!");

            // Verificar os eventos, para lançar alertas
            while (true) {
                try {
                    // Buscar utilizadores ligados
                    HashMap<String, ReminderInterface> reminderIN = calendar.getRegisterUser();
                    // Guardar nome dos utilizadores
                    Set<String> nameUsers = reminderIN.keySet();

                    for (String user : nameUsers) {
                        // Guardar eventos de um utilizador
                        ArrayList<Event> listEvent = calendar.list(user);
                        //Percorrer a lista de eventos desse utilizador
                        for (int indx = 0; indx < listEvent.size(); indx++)

                            if (listEvent.get(indx).getStartDate().before(new Date())
                                    && listEvent.get(indx).getFlag() == false) {
                                reminderIN.get(user).notify(listEvent.get(indx), " esta a começar");
                                listEvent.get(indx).setFlag(true);
                            }
                    }
                    // Pausa de 10s
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                }
            }
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e);
        }
    }
}
