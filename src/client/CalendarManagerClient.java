package client;

import event.*;
import interfaces.*;
import files.Reminder;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Classe CalendarManagerClient
 * <p>
 * Parte do cliente
 *
 * @author Joao Vicente
 */
public class CalendarManagerClient {

    private final static Scanner input = new Scanner(System.in);

    /**
     * Método para verificar se um campo esta vazio
     *
     * @param nameField Nome do campo para verificar
     * @return String com o valor do campo
     */
    private static String verifyField(String nameField) {
        String ax;
        while (true) {
            System.out.print(nameField + ": ");
            ax = input.nextLine().trim();
            // Verificr se o campo esta vazio
            if (ax.isEmpty())
                System.out.println("Campo vazio!");
            else
                break;
        }
        return ax;
    }

    public static void main(String args[]) {
        int numEvent;
        ArrayList<Event> events;
        String username, op, title, date, location;
        DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateStart, dateEnd;
        try {
            CalendarManagerInterface calendar = (CalendarManagerInterface) Naming.lookup("Calendar");
            Reminder remI = new Reminder();

            // Ler nome do utilizador
            username = verifyField("username");
            // Registar/Caregar user
            if (calendar.register(username, remI)) {
                while (true) {
                    System.out.println("1- Add\n2- Update\n3- Remove\n4- List\n0- exit");
                    op = input.nextLine();

                    switch (op) {
                        case "1":
                            title = verifyField("Nome do evento");
                            location = verifyField("Localizacao");
                            try {
                                date = verifyField("Data de inicio com o formato (dd-mm-yy HH:mm)");
                                dateStart = formatDate.parse(date);
                                date = verifyField("Data de fim com o formato (dd-mm-yy HH:mm)");
                                dateEnd = formatDate.parse(date);
                                if (dateEnd.before(dateStart) || dateStart.before(new Date())) {
                                    System.out.println("Data invalida!");
                                } else {
                                    if (calendar.add(username, new Event(title, location, dateStart, dateEnd, false))) {
                                        System.out.println("Novo evento adicionado!");
                                    } else
                                        System.out.println("Ocorreu algum erro");
                                }
                            } catch (Exception e) {
                                System.out.println("Data incorrecta!!\n\n");
                            }
                            break;

                        case "2":
                            events = calendar.list(username);
                            if (!events.isEmpty()) {
                                for (int i = 0; i < events.size(); i++) {
                                    if (events.get(i).getFlag()) {
                                        events.remove(i);
                                    } else
                                        System.out.println(i + 1 + "- " + events.get(i).toString());
                                }
                                // Escolher um evento
                                try {
                                    numEvent = Integer.parseInt(verifyField("Escolha um evento"));
                                    if (numEvent > events.size() || numEvent < 1) {
                                        System.out.println("Numero de envento incorrecto!");
                                    } else {
                                        // Definir campos do evento
                                        title = verifyField("Nome do evento");
                                        location = verifyField("Localizacao");
                                        date = verifyField("Data de inicio com o formato (dd-mm-yy HH:mm)");
                                        dateStart = formatDate.parse(date);
                                        date = verifyField("Data de fim com o formato (dd-mm-yy HH:mm)");
                                        dateEnd = formatDate.parse(date);
                                        if (dateEnd.before(dateStart) || dateStart.before(new Date())) {
                                            System.out.println("Data invalida!");
                                        } else {
                                            if (calendar.update(username, events.get(numEvent - 1),
                                                    new Event(title, location, dateStart, dateEnd, false)))
                                                System.out.println("Evento alterado!");
                                            else
                                                System.out.println("Ocorreu algum erro");
                                        }
                                    }
                                    // Converter string em numero erroe
                                } catch (NumberFormatException e) {
                                    System.out.println("Numero de envento incorrecto!");
                                    // Converter string para data erro
                                } catch (ParseException e) {
                                    System.out.println("Data incorrecta!!\n\n");
                                }

                            } else {
                                System.out.println("Sem eventos!\n");
                            }
                            break;

                        case "3":
                            events = calendar.list(username);
                            if (!events.isEmpty()) {
                                for (int i = 0; i < events.size(); i++) {
                                    if (events.get(i).getFlag()) {
                                        events.remove(i);
                                    } else
                                        System.out.println(i + 1 + "- " + events.get(i).toString());
                                }
                                // Escolher um evento
                                try {
                                    numEvent = Integer.parseInt(verifyField("Escolha um evento"));
                                    if (numEvent > events.size() || numEvent < 1) {
                                        System.out.println("Numero de envento incorrecto!");
                                    } else {
                                        if (calendar.remove(username, events.get(numEvent - 1)))
                                            System.out.println("Evento apagado!");
                                        else
                                            System.out.println("Ocorreu algum erro");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Numero de envento incorrecto!");
                                }
                            } else {
                                System.out.println("Sem eventos!\n");
                            }
                            break;

                        case "4":
                            events = calendar.list(username);
                            if (!events.isEmpty()) {
                                for (int i = 0; i < events.size(); i++) {
                                    if (events.get(i).getFlag()) {
                                        events.remove(i);
                                    } else
                                        System.out.println(events.get(i).toString());
                                }
                            } else {
                                System.out.println("Sem eventos!\n");
                            }
                            break;

                        case "0":
                            calendar.unregister(username);
                            System.exit(0);
                            break;
                    }
                }
            } else {
                System.out.println("Utilizador ja ligado");
            }
        } catch (ConnectException e) {
            System.out.println("Sem ligacao ao servidor !");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
