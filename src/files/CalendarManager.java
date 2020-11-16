package files;

import interfaces.*;
import event.*;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Classe CalendarManager
 * <p>
 * Classe para implementar metodos da interface CalendarManagerInterface
 *
 * @author Joao Vicente
 */
public class CalendarManager extends UnicastRemoteObject implements CalendarManagerInterface {
    // Nome do ficheiro que contem eventos
    private static final String EVENTS_DATA = "events.obj";

    private HashMap<String, ArrayList<Event>> events = new HashMap<>();
    private HashMap<String, ReminderInterface> registerUser = new HashMap<>();

    // Sincronismo
    private Object lock = new Object();

    /**
     * Construtor da classe
     *
     * @throws RemoteException
     */
    public CalendarManager() throws RemoteException {
        // Ler dados do ficherio.
        try {
            events = readFile(EVENTS_DATA);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Método saveFile
     * <p>
     * Guardar dados do hashmap para um ficheiro
     *
     * @param users HashMap com os dados do utilizador
     * @param fil   Nome do ficheiro para onde guardar os dados
     * @throws IOException
     */
    private static void saveFile(HashMap<String, ArrayList<Event>> users, String fil)
            throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fil))) {
            os.writeObject(users);
        }
    }

    /**
     * Método readFile
     * <p>
     * Ler dados de um ficheiro
     *
     * @param fil Nome do ficheiro de onde carregar os dados
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static HashMap<String, ArrayList<Event>> readFile(String fil)
            throws ClassNotFoundException, IOException {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(fil))) {
            return (HashMap<String, ArrayList<Event>>) is.readObject();
        }
    }

    /**
     * Método add
     * <p>
     * Tem como objetivo adicionar um novo evento ao array de eventos de um utilizador
     *
     * @param user  Nome do utilizador(String)
     * @param event Objeto evento para adiconar ao array
     * @return Retorna verdadeiro caso adicione com suçesso e falso caso não consiga
     * @throws RemoteException
     */
    @Override
    public boolean add(String user, Event event) throws RemoteException {
        boolean flag;
        ArrayList<Event> eventsUser = new ArrayList<>();

        synchronized (lock) {
            try {
                // Buscar eventos do utilizador
                eventsUser = events.get(user);
                eventsUser.add(event);
                events.replace(user, eventsUser);
                flag = true;
                // Guardar evento no ficheiro
                saveFile(events, EVENTS_DATA);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Método update
     * <p>
     * Actualizar os dados de um dado evento
     *
     * @param user     Nome do utilizador
     * @param event    Antigo evento
     * @param newEvent Novo evento
     * @return Retornar falso caso não consiga actualizar e verdadeiro quando altera
     * @throws RemoteException
     */
    @Override
    public boolean update(String user, Event event, Event newEvent) throws RemoteException {
        ArrayList<Event> eventsUser = events.get(user);
        boolean flag = false;
        int index = -1;
        synchronized (lock) {
            try {
                // Verifar se existem eventos no utilizador
                if (eventsUser == null) {
                } else {
                    //Procurar o index do evento a trocar
                    for (int i = 0; i < eventsUser.size(); i++) {
                        // Procurar pelo evento pretendido
                        if (eventsUser.get(i).equals(event)) {
                            // Buscar o index do evento a substituir
                            index = i;
                            break;
                        }
                    }
                    // Ver se existe o evento
                    if (index == -1)
                        flag = false;
                    else {
                        eventsUser.set(index, newEvent);
                        events.replace(user, eventsUser);
                        flag = true;
                        saveFile(events, EVENTS_DATA);
                    }
                }
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Método remove
     * <p>
     * Remover um dado evento de um utilizador
     *
     * @param user  Nome do utilizador
     * @param event Evento a remover
     * @return Retorna true caso ccnsiga eliminar e false caso não eliminado
     * @throws RemoteException
     */
    @Override
    public boolean remove(String user, Event event) throws RemoteException {
        ArrayList<Event> eventsUser = events.get(user);
        boolean flag = false;
        synchronized (lock) {
            try {
                // Verifar se existem eventos no utilizador
                if (eventsUser == null) {
                } else {
                    // Percorrer lista de eventos
                    for (int i = 0; i < eventsUser.size(); i++) {
                        // Procurar pelo evento pretendido
                        if (eventsUser.get(i).equals(event)) {
                            eventsUser.remove(i);
                            events.replace(user, eventsUser);
                            flag = true;
                            break;
                        }
                    }
                    saveFile(events, EVENTS_DATA);
                }
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Método list
     * <p>
     * Tem como objetivo buscar todos os eventos de um utilizador
     *
     * @param user Nome do utilizador
     * @return Retorna um arraylist com os eventos do utilizador pedido
     * @throws RemoteException
     */
    @Override
    public ArrayList<Event> list(String user) throws RemoteException {
        return events.get(user);
    }

    /**
     * Método register
     * <p>
     * Tem como objetivo de registar um novo utilizador
     *
     * @param user      String com o nome do utilizador
     * @param reminderI ReminderInterface
     * @return Retorna verdadeiro caso consiga adicionar o novo utilizador e falso caso ele já exista
     * @throws RemoteException
     */
    @Override
    public boolean register(String user, ReminderInterface reminderI) throws RemoteException {
        boolean flag = false;
        synchronized (lock) {
            // Ver se o utilizador já esta conectado
            if (registerUser.containsKey(user)) {
            }
            // Ver se o utilizador ja tem eventos no server
            else if (events.containsKey(user)) {
                registerUser.put(user, reminderI);
            } else {
                events.put(user, new ArrayList<>());
                registerUser.put(user, reminderI);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Método unregister
     * <p>
     * Tem a função de desligar um utilizador
     *
     * @param user Nome do utilizador
     * @return retorna verdadeiro caso consiga desligar
     * @throws RemoteException
     */
    @Override
    public boolean unregister(String user) throws RemoteException {
        boolean flag;
        synchronized (lock) {
            try {
                registerUser.remove(user);
                flag = true;
                saveFile(events, EVENTS_DATA);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Método getRegisterUser
     * <p>
     * Devolve o hashmap de utilizadores ligados
     *
     * @return hashmap
     */
    public HashMap<String, ReminderInterface> getRegisterUser() {
        return registerUser;
    }

}
