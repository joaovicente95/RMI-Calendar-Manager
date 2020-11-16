package event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Classe event
 * <p>
 * Clase para guardar um evento
 *
 * @author Joao Vicente
 */
public class Event implements Serializable {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    // Controla se o alerta do evento já foi dado
    private boolean flag;

    /**
     * Construtor da classe
     *
     * @param title
     * @param location
     * @param startDate
     * @param endDate
     */
    public Event(String title, String location, Date startDate, Date endDate, boolean flag) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flag = flag;
    }

    /**
     * Método getTitle
     * <p>
     * retorna o titulo do evento
     *
     * @return string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Método setTitle
     * <p>
     * Define o titulo do evento
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Método getLocation
     * <p>
     * retorna a localização do evento
     *
     * @return string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Método setLocation
     * <p>
     * Define a localização do evento
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Método getStartDate
     * <p>
     * retorna a data de inicio  evento
     *
     * @return Date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Método getEndDate
     * <p>
     * retorna a data de fim do evento
     *
     * @return Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Método setEndDate
     * <p>
     * Define a data de fim
     *
     * @param endDate data de fim do evento
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Método getFlag
     * <p>
     * retorna a flag do evento
     *
     * @return boolean
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * Método setFlag
     * <p>
     * Define a flag do evento
     *
     * @param flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * Método toSring
     * <p>
     * Devolve os dados de um evento
     *
     * @return string com os dados do evento
     */
    @Override
    public String toString() {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return "Title= " + title +
                ", Location= " + location +
                ", StartDate= " + formatDate.format(startDate) +
                ", EndDate= " + formatDate.format(endDate);
    }

    /**
     * Método equals
     * <p>
     * Compara dois eventos
     *
     * @param obj Objeto do tipo evento
     * @return
     */
    public boolean equals(Event obj) {
        boolean flag = false;
        if (this.getTitle().equals(obj.getTitle()) && this.getLocation().equals(obj.getLocation()) && this.getStartDate().equals(obj.getStartDate()) && this.getEndDate().equals(obj.getEndDate()))
            flag = true;
        return flag;
    }
}
