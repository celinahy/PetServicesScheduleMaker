package edu.ucalgary.oop;
import java.lang.*;
/**
 * This class is used to create the Hour object, which contains the amount of time volunteer status and current tasks
 * Needed to create the schedule.
 * @author Sergiy Redko, Nicole Heather
 * @version 1.0
 * @since 1.0
 */
public class Hour{
    private int remainingTime;
    private ExtraVolunteerStatus backupStatus;
    private StringBuilder currentTasks;

    /**
     * This constructor creates the hour object and assigns its private data members
     */
    public Hour(){
        this.remainingTime = 60;
        this.backupStatus = ExtraVolunteerStatus.NOTNEEDED;
        this.currentTasks = new StringBuilder();
    }

    /**
     * Getter for remainingTime.
     */
    public int getRemainingTime(){
        return remainingTime;
    }

    /**
     * Subtracts timeTaken from remainingTime.
     * @param timeTaken is the time taken to do a task.
     */
    public void subRemainingTime(int timeTaken){
        remainingTime = remainingTime - (timeTaken);
    }

    /**
     * Adds timeTaken to remainingTime.
     * @param newTime is the time added to the remainingTime.
     * @return returns new time.
     */
    public int addRemainingTime(int newTime){
        remainingTime = remainingTime + newTime;
        return remainingTime;
    }

    /**
     * Adds a new task to currentTasks.
     * @param newTask is the task being added.
     */
    public void addTaskString(String newTask){
            currentTasks.append(newTask);
    }

    /**
     * Getter for currentTasks.
     * @return new task String.
     */
    public StringBuilder getCurrentTasks(){
        return currentTasks;
    }

    /**
     * Deletes parts of currentTasks.
     * @param a is the beginning index.
     * @param b is the end index.
     */
    public void taskStringDelete(int a, int b){
        if(a > b){
            throw new IllegalArgumentException();
        }
        currentTasks.delete(a, b);
    }

    /**
     * Getter for backupstatus.
     * @return backupstatus.
     */
    public ExtraVolunteerStatus getBackupStatus(){
        return backupStatus;
    }

    /**
     * Setter for backupstatus.
     */
    public void setBackupStatus(ExtraVolunteerStatus newStatus){
        backupStatus = newStatus;
    }

    /**
     * Typecasts currentTasks to String.
     * @return  String currentTasks.
     */
    public String print() {
        //StringBuilder out = new StringBuilder();
        //out.append("    ∙ Some tasks here...");
        //return out.toString();
        return getCurrentTasks().toString();
    }
}