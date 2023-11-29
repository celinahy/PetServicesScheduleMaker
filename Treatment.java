package edu.ucalgary.oop;

/**
 * This class represents a single treatment.
 * @author Sergiy Redko, Nicole Heather
 * @version 1.0
 * @since 1.0
 */
public class Treatment {

    private int animalID;
    private String animalName;
    private int taskID;
    private int treatmentID;
    private int startHour;
    private int duration;
    private String description;
    private int maxWindow; 

    /**
     * This constructor creates a new Treatment object.
     * @param animalId The ID of the animal that the treatment is for.
     * @param animalName The name of the animal that the treatment is for.
     * @param taskId The ID of the task that the treatment is for.
     * @param treatmentId The ID of the treatment.
     * @param startHour The hour that the treatment starts.
     * @param duration The duration of the treatment.
     * @param description The description of the treatment.
     * @param window The maximum window of time that the treatment can be moved.
     */
    public Treatment(int animalId, String animalName, int taskId, int treatmentId, int startHour,int duration, String description, int window) {
        if (startHour < 0 || startHour > 23) {
            throw new IllegalArgumentException("Start hour must be between 0 and 23, but was " + startHour);
        }
        if (duration < 0) {
            throw new IllegalArgumentException("Duration must be greater or equal to 0");
        }
        if (window < 0) {
            throw new IllegalArgumentException("Window must be greater or equal to 0");
        }

        this.animalID = animalId;
        this.animalName = animalName;
        this.taskID = taskId;
        this.treatmentID = treatmentId;
        this.startHour = startHour;
        this.duration = duration;
        this.description = description;
        this.maxWindow = window;
    }

    /**
     * Animal ID getter.
     * @return animal ID.
     */
    public int getAnimalID(){
        return animalID;
    }

    /**
     * Animal name getter.
     * @return animal name.
     */
    public String getAnimalName(){
        return animalName;
    }

    /**
     * Task ID getter.
     * @return task ID.
     */
    public int getTaskID(){
        return taskID;
    }

    /**
     * Treatment ID getter.
     * @return treatment ID.
     */
    public int getTreatmentID(){
        return treatmentID;
    }

    /**
     * Start hour getter.
     * @return start hour.
     */
    public int getStartHour(){
        return startHour;
    }

    /**
     * Duration getter.
     * @return duration.
     */
    public int getDuration(){
        return duration;
    }

    /**
     * Description getter.
     * @return description.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Max window getter.
     * @return max window.
     */
    public int getMaxWindow(){
        return maxWindow;
    }

    /**
     * Prints the treatment.
     * This is a diagnostic method.
     */
    public void print(){
        System.out.println("animalId:" + animalID + "  animalName:" + animalName + "  taskId:" + taskID + "  treatmentId:" + treatmentID + "  startHour:" + startHour + "  duration:" + duration + "  description:" + description + "  maxWindow:" + maxWindow);
    }

    /**
     * Sets the start hour of the treatment. The start hour must be between 0 and 23.
     * @param startHour The start hour of the treatment.
     * @throws IllegalArgumentException if the start hour is not between 0 and 23.
     */
    public void setStartHour(int startHour) throws IllegalArgumentException{
        if (startHour < 0 || startHour > 23) {
            throw new IllegalArgumentException("Start hour must be between 0 and 23, and was " + startHour + ".");
        }
        this.startHour = startHour;
    }
}
