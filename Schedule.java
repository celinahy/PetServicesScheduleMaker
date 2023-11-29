package edu.ucalgary.oop;

import java.sql.Array;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.*;
import java.util.Arrays;

/**
 * This class is used to create the Schedule object, which contains the tasks for all hours.
 * @author Sergiy Redko, Nicole Heather, Gio De Verra
 * @version 1.0
 * @since 1.0
 */
public class Schedule {
    private Hour[] hour = new Hour[24];
    private ArrayList<Treatment> treatmentList;
    private String[] coyoteFeedingList;
    private String[] foxFeedingList;
    private String[] porcupineFeedingList;
    private String[] beaverFeedingList;
    private String[] racoonFeedingList;
    private String[] coyoteCleaningList;
    private String[] foxCleaningList;
    private String[] porcupineCleaningList;
    private String[] beaverCleaningList;
    private String[] racoonCleaningList;

    /**
     * This constructor creates the schedule object and assigns its private data members
     */
    public Schedule(){
        for (int i = 0; i < 24; i++){
            hour[i] = new Hour();
        }
    }

    /**
     * Creates lists of treatments, feedings, and cleanings.
     * @param url URL of the database.
     * @param user Username of the database.
     * @param password Password of the database.
     * @throws CouldNotConnectToDatabaseException when the connection to the database fails.
     * @throws SQLException when the SQL query fails.
     * @throws CouldNotCloseDatabaseConnectionException when the connection to the database fails to close.
     */
    public void createTaskList(String url, String user, String password) throws CouldNotConnectToDatabaseException, SQLException, CouldNotCloseDatabaseConnectionException{
        SqlConnector connector = new SqlConnector(url, user, password);
        connector.createConnection();

        treatmentList = connector.getTreatments();
        coyoteFeedingList = connector.getFeedings(AnimalSpecies.COYOTE);
        foxFeedingList = connector.getFeedings(AnimalSpecies.FOX);
        porcupineFeedingList = connector.getFeedings(AnimalSpecies.PORCUPINE);
        beaverFeedingList = connector.getFeedings(AnimalSpecies.BEAVER);
        racoonFeedingList = connector.getFeedings(AnimalSpecies.RACOON);
        coyoteCleaningList = connector.getCleanings(AnimalSpecies.COYOTE);
        foxCleaningList = connector.getCleanings(AnimalSpecies.FOX);
        porcupineCleaningList = connector.getCleanings(AnimalSpecies.PORCUPINE);
        beaverCleaningList = connector.getCleanings(AnimalSpecies.BEAVER);
        racoonCleaningList = connector.getCleanings(AnimalSpecies.RACOON);

        connector.closeConnection();
    }

    /**
     * Updates the database with the new treatments.
     * @param url URL of the database.
     * @param user Username of the database.
     * @param password Password of the database.
     * @param updatedTreatments List of treatments to be updated.
     * @throws CouldNotConnectToDatabaseException when the connection to the database fails.
     * @throws SQLException when the SQL query fails.
     * @throws CouldNotCloseDatabaseConnectionException when the connection to the database fails to close.
     */
    public void updateTreatments(String url, String user, String password, ArrayList<Treatment> updatedTreatments) throws CouldNotConnectToDatabaseException, SQLException, CouldNotCloseDatabaseConnectionException{
        SqlConnector connector = new SqlConnector(url, user, password);
        connector.createConnection();

        connector.updateTreatments(updatedTreatments);

        connector.closeConnection();
    }

    /**
     * Calls AssignTreatments, AssignFeedings and AssignCleanings.
     * @throws CannotCreateScheduleException if the schedule is impossible to build.
     */
    public void createSchedule() throws CannotCreateScheduleException {
        assignTreatments();
        assignFeedings();
        assignCleanings();
    }

    /**
     * Assigns the treatments to the schedule.
     */
    public void assignTreatments() throws CannotCreateScheduleException{
        Treatment[][] procedurePriority = new Treatment[6][treatmentList.size()];
        int[] numOfEntries = new int[6];
        for(int i = 0; i < 6; i++){
            numOfEntries[i] = 0;
        }

        for(int i = 0; i < treatmentList.size(); i++){
            Treatment procedure = treatmentList.get(i);
            int procMaxSlot = procedure.getMaxWindow();
            int entryIndex = numOfEntries[procMaxSlot];

            procedurePriority[procMaxSlot][entryIndex] = procedure;

            numOfEntries[procMaxSlot] = entryIndex + 1;
        }

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < treatmentList.size(); j++){
                if(procedurePriority[i][j] == null){
                    continue;
                }

                Treatment procedure = procedurePriority[i][j];
                int procStart = procedure.getStartHour();

                String procDescription = procedure.getDescription();
                String procAnimalName = procedure.getAnimalName();

                int procDuration = procedure.getDuration();
                int procMaxSlot = procedure.getMaxWindow();
                Hour chosenTime = null;

                int hourFound = findSlotSimpleTask(procStart, procDuration, procMaxSlot);
                if(hourFound < 0){
                    boolean success = assignVolunteer(procStart, procMaxSlot);
                    if(!success){
                        throw new CannotCreateScheduleException("Cannot assign " + procDescription + " to schedule for (" + procAnimalName + ").");
                    }
                    j--;
                    continue;
                }
                else{
                    hour[hourFound].subRemainingTime(procDuration);
                    chosenTime = hour[hourFound];
                }

                StringBuilder taskDescrip = new StringBuilder();
                taskDescrip.append("    - " + procDescription);
                taskDescrip.append(" (");
                taskDescrip.append(procAnimalName);
                taskDescrip.append(")\n");

                chosenTime.addTaskString(taskDescrip.toString());
            }
        }

    }

    /**
     * Assigns the feedings to the schedule.
     */
    public void assignFeedings() throws CannotCreateScheduleException{

        assignFeeding(AnimalSpecies.FOX, foxFeedingList);
        assignFeeding(AnimalSpecies.COYOTE, coyoteFeedingList);
        assignFeeding(AnimalSpecies.PORCUPINE, porcupineFeedingList);
        assignFeeding(AnimalSpecies.BEAVER, beaverFeedingList);
        assignFeeding(AnimalSpecies.RACOON, racoonFeedingList);

    }

    /**
     * Assigns a single feeding to the schedule.
     *
     * @param animalChoice The animal species to be fed.
     * @param animalList The list of animals to be fed.
     */
    public void assignFeeding(AnimalSpecies animalChoice, String[] animalList) throws CannotCreateScheduleException{

        int startHr = Constants.getFeedStartHr(animalChoice);
        int feedPrep = Constants.getFeedPrep(animalChoice);
        int feedTime = Constants.getFeedDuration(animalChoice);
        int animalsToFeed = animalList.length;
        int alreadyFed = 0;
        int[] allFeedTimes = new int[1];

        Hour[] feedingHours = new Hour[3];
        int[] timeLeft = new int[3];
        int highestTimeLeft = 0;
        int hourWithHighTime = -1;

        int groupedTime = feedPrep + (feedTime * animalsToFeed);

        if(animalList.length == 0){
            return;
        }

        for(int i = 0; i < 3; i++){
            feedingHours[i] = hour[startHr + i];
            timeLeft[i] = feedingHours[i].getRemainingTime();

            if(timeLeft[i] > highestTimeLeft){
                highestTimeLeft = timeLeft[i];
                hourWithHighTime = i;
            }
        }

        if(groupedTime <= highestTimeLeft){
            Hour chosenTime = hour[startHr + hourWithHighTime];
            hour[startHr + hourWithHighTime].subRemainingTime(groupedTime);

            StringBuilder taskDescrip = new StringBuilder();
            taskDescrip.append("    - Feeding - ");
            taskDescrip.append(animalChoice.toString().toLowerCase());
            taskDescrip.append(" (");
            taskDescrip.append(animalList.length);
            taskDescrip.append(": ");
            for(int i = 0; i < animalList.length; i++){
                taskDescrip.append(animalList[i]);
                if(i != animalList.length - 1){
                    taskDescrip.append(", ");
                }
            }
            taskDescrip.append(")\n");

            chosenTime.addTaskString(taskDescrip.toString());
            return;
        }

        for(int i = 0; i < 3; i++){
            int selectHourFed = ((hour[startHr + i].getRemainingTime()) - feedPrep) / feedTime;
            int firstHourFed = ((hour[startHr].getRemainingTime()) - feedPrep) / feedTime;
            int secondHourFed = ((hour[startHr + 1].getRemainingTime()) - feedPrep) / feedTime;
            int thirdHourFed = ((hour[startHr + 2].getRemainingTime()) - feedPrep) / feedTime;
            int totalAmountFed = firstHourFed + secondHourFed + thirdHourFed;

            if(totalAmountFed < animalsToFeed){
                boolean volunteerFeed = assignVolunteer(startHr, 3);

                if(volunteerFeed == true){
                    i--;
                    continue;
                }
                else{
                    throw new CannotCreateScheduleException("Thrown in AssignFeeding for " + animalChoice + ", cannot fit tasks into feeding period: " + startHr + "-" + (startHr + 2));
                }
            }
            else{
                animalsToFeed = splitFeedings(animalsToFeed, selectHourFed, animalChoice, animalList, alreadyFed, i);
                if(animalsToFeed <= 0){
                    return;
                }
                alreadyFed += selectHourFed;
            }
        }
    }

    /**
     * Calls helper functions AssignCleaning.
     * @throws CannotCreateScheduleException if schedule is impossible to create.
     */
    public void assignCleanings() throws CannotCreateScheduleException {
        assignCleaning(foxCleaningList, AnimalSpecies.FOX);
        assignCleaning(beaverCleaningList, AnimalSpecies.BEAVER);
        assignCleaning(coyoteCleaningList, AnimalSpecies.COYOTE);
        assignCleaning(racoonCleaningList, AnimalSpecies.RACOON);
        assignCleaning(porcupineCleaningList, AnimalSpecies.PORCUPINE);
    }

    /**
     * Calls FindSlotSimpleTask and formatTask, is responsible for finding the correct hour objects to assign cleaning tasks to.
     * If this function cannot assign all tasks in a single hour in a given window, it then will split the tasks into different hours.
     * @throws CannotCreateScheduleException if schedule is impossible to create.
     * @param list is the list of animals that must have cleaning assigned to them.
     * @param species is the animal species of the given list.
     */
    public void assignCleaning(String[] list, AnimalSpecies species) throws CannotCreateScheduleException {
        if(list.length == 0){
            return;
        }

        int[] assignedHour = new int[list.length];

        for(int i = 0; i < list.length; i++){
            int hourFound = findSlotSimpleTask(0, Constants.getCleanDuration(species), 24);
            if(hourFound < 0){
                boolean success = assignVolunteer(0, 24);
                if(!success){
                    throw new CannotCreateScheduleException("Cannot assign cleanings to schedule for " + list[i] + "(" + species.toString().toLowerCase() + ").");
                }
                i--;
            }
            else{
                hour[hourFound].subRemainingTime(Constants.getCleanDuration(species));
                assignedHour[i] = hourFound;
            }
        }

        assignedHour = Arrays.stream(assignedHour).sorted().toArray();

        int currentHour = assignedHour[0];
        ArrayList<String> animalsInHour = new ArrayList<>();

        for(int i = 0; i < assignedHour.length; i++){
            animalsInHour.add(list[i]);
            if(i == assignedHour.length -1 || assignedHour[i+1] != currentHour){
                assignCleaningsToHour(animalsInHour, species, currentHour);
                animalsInHour.clear();
                if(i != assignedHour.length -1){
                    currentHour = assignedHour[i+1];
                }
            }
        }
    }

    /**
     * Assign the task to given hour.
     * @param chosenHour is the hour to assign the task to.
     * @param animalNames is the list of animals to assign to the task.
     * @param species is the animal species of the given list.
     * @param taskType is the type of task to assign.
     */
    public void formatTask(int chosenHour, String[] animalNames, AnimalSpecies species, String taskType) {
        Hour chosenTime = hour[chosenHour];
        int animals = 0;
        for (String animalName : animalNames) {
            if (animalName != null) {
                animals++;
            }
        }
        chosenTime.addTaskString("    - " + taskType + " - " + species.toString().toLowerCase() + " (" + animals + ": ");
        for (String animalName : animalNames) {
            if (animalName == null) {
                continue;
            }
            chosenTime.addTaskString(animalName + ", ");
        }
        chosenTime.taskStringDelete(chosenTime.getCurrentTasks().length() - 2, chosenTime.getCurrentTasks().length());
        chosenTime.addTaskString(")" + System.getProperty("line.separator"));
    }

    /**
     * Assigns the cleanins to the given hour.
     * @param hourIndex is the index of the hour that the task is being assigned to.
     * @param animals is the list of animals that are being assigned to the task.
     * @param species is the animal species of the given list.
     */
    public void assignCleaningsToHour(ArrayList<String> animals, AnimalSpecies species, int hourIndex){
        StringBuilder taskDescrip = new StringBuilder();
        taskDescrip.append("    - Cleaning - ");
        taskDescrip.append(species.toString().toLowerCase());
        taskDescrip.append(" (");
        taskDescrip.append(animals.size());
        taskDescrip.append(": ");
        for(int i = 0; i < animals.size(); i++){
            taskDescrip.append(animals.get(i));
            if(i != animals.size() - 1){
                taskDescrip.append(", ");
            }
        }
        taskDescrip.append(")\n");

        hour[hourIndex].addTaskString(taskDescrip.toString());
    }

    /**
     * splitFeedings takes in a certain amount of animals to feed and schedules them for the certain hour
     * @param totalToFeed is the total amount of animals to feed
     * @param numToFeed is the amount able to be fed within the specified hour
     * @param animalChoice is the animal species
     * @param animalList is the list of animal names to feed
     * @param numDone is the number of animals already fed
     * @param increment is the increment for the specific hour
     * @return the updated amount of animals to feed left
     */
    public int splitFeedings(int totalToFeed, int numToFeed, AnimalSpecies animalChoice, String[] animalList, int numDone, int increment){
        int startHr = Constants.getFeedStartHr(animalChoice);
        int feedPrep = Constants.getFeedPrep(animalChoice);
        int feedTime = Constants.getFeedDuration(animalChoice);
        int timeTaken = 0;
        int numOfAnimals = 0;
        String[] numAnimalsFed = new String[numToFeed];

        if(numToFeed > totalToFeed){
            numToFeed = totalToFeed;
        }

        timeTaken = (numToFeed * feedTime) + feedPrep;
        Hour chosenHour = hour[startHr + increment];
        chosenHour.subRemainingTime(timeTaken);

        for(int i = numDone; i < (numToFeed + numDone); i++){
            numAnimalsFed[i - numDone] = animalList[i];
        }
        // formatTask(startHr + increment, numAnimalsFed, animalChoice, "Feeding");

        totalToFeed = totalToFeed - numToFeed;

        for(int i = 0; i < numAnimalsFed.length; i++) {
            if(numAnimalsFed[i] == null){
                continue;
            }
            numOfAnimals++;
        }

        StringBuilder taskDescrip = new StringBuilder();
        taskDescrip.append("    - Feeding - ");
        taskDescrip.append(animalChoice.toString().toLowerCase());
        taskDescrip.append(" (");
        taskDescrip.append(numOfAnimals);
        taskDescrip.append(": ");
        for(int i = 0; i < numAnimalsFed.length; i++){
            if(numAnimalsFed[i] == null){
                continue;
            }
            taskDescrip.append(numAnimalsFed[i]);
            if((i + 1) < numAnimalsFed.length){
                if(numAnimalsFed[i + 1] != null) {
                    taskDescrip.append(", ");
                }
            }
        }
        taskDescrip.append(")\n");

        chosenHour.addTaskString(taskDescrip.toString());
        return totalToFeed;
    }

    /**
     * Is responsible for checking each hour within the window for time availability.
     * @param startH is the chosen hour that must be checked for remaining time, can be changed by the function when returned.
     * @param duration is the maximum amount of time that a task or tasks takes.
     * @param maxWindow is the maximum amount of hours that a task can be done.
     * @return the hour that has enough time for the task. Otherwise -1.
     */
    public int findSlotSimpleTask(int startH, int duration, int maxWindow){
        for(int examinedHour = startH; examinedHour < startH + maxWindow; examinedHour++){
            if(duration <= hour[examinedHour].getRemainingTime()){
                return examinedHour;
            }
        }
        // If we got here, there is no slot large enough in this hour. Return -1 to let upstream know.
        return -1;
    }

    /**
     * Assigns a volunteer to a given hour. Returns true if successful, false if not.
     * @param startHour The hour to start looking for a volunteer slot.
     * @param maxWindow The maximum number of hours to look for a volunteer slot.
     * @return True if a volunteer was assigned, false if not.
     */
    public boolean assignVolunteer(int startHour, int maxWindow){
        for(int i = startHour; i < startHour + maxWindow; i++){
            if(hour[i].getBackupStatus() != ExtraVolunteerStatus.REQUESTED){
                hour[i].setBackupStatus(ExtraVolunteerStatus.REQUESTED);
                // Now that we added a volunteer, we need to add 60 minutes to the hour.
                hour[i].addRemainingTime(60);
                return true;
            }
        }
        // If we got here, all of these hours already have a backup volunteer. Nothing can be done. Return false.
        return false;
    }

    /**
     * Getter for treatment list.
     * @return the treatment list.
     */
    public ArrayList<Treatment> getTreatmentList() {
        return treatmentList;
    }

    /**
     * @return the built schedule.
     */
    public String print(){
        StringBuilder out = new StringBuilder();
        out.append("Schedule for " + LocalDate.now().toString() + ":\n");
        for(int i = 0; i < hour.length; i++){
            out.append(i + ":00 ");
            if(hour[i].getBackupStatus() == ExtraVolunteerStatus.REQUESTED){
                out.append("[+ backup volunteer]");
            }
            out.append("\n");
            out.append(hour[i].print());
        }
        return out.toString();
    }

    /**
     * Prints the remaining time for each hour.
     * This is a diagnostic function.
     */
    public void printHourDetails(){
        for(int i = 0; i < hour.length; i++){
            System.out.println(i + " remaining time: " + hour[i].getRemainingTime() + "   Vounteer status: " + hour[i].getBackupStatus());
        }
    }

    /**
     * Getter for the volunteer list.
     * @return the volunteer list.
     */
    public boolean[] getVolunteerList(){
        boolean[] volunteerList = new boolean[24];

        for (int i = 0; i < 24; i++){
            if (hour[i].getBackupStatus() == ExtraVolunteerStatus.REQUESTED){
                volunteerList[i] = true;
            }
        }
        return volunteerList;
    }

    /**
     * Getter for the hour array.
     * @return the hour array.
     */
    public Hour[] getHours(){
        return hour;
    }

    /**
     * Getter for the feeding list.
     */
    public void setCoyoteFeedingList(String[] coyoteFeedingList){
        this.coyoteFeedingList = coyoteFeedingList;
    }

    /**
     * Getter for the feeding list.
     */
    public void setFoxFeedingList(String[] foxFeedingList){
        this.foxFeedingList = foxFeedingList;
    }

    /**
     * Getter for the feeding list.
     */
    public void setPorcupineFeedingList(String[] porcupineFeedingList){
        this.porcupineFeedingList = porcupineFeedingList;
    }

    /**
     * Getter for the feeding list.
     */
    public void setBeaverFeedingList(String[] beaverFeedingList){
        this.beaverFeedingList = beaverFeedingList;
    }

    /**
     * Getter for the feeding list.
     */
    public void setRaccoonFeedingList(String[] raccoonFeedingList){
        this.racoonFeedingList = raccoonFeedingList;
    }

    /**
     * Getter for cleaning list.
     * @param coyoteCleaningList the cleaning list.
     */
    public void setCoyoteCleaningList(String[] coyoteCleaningList){
        this.coyoteCleaningList = coyoteCleaningList;
    }

    /**
     * Getter for cleaning list.
     * @param foxCleaningList the cleaning list.
     */
    public void setFoxCleaningList(String[] foxCleaningList){
        this.foxCleaningList = foxCleaningList;
    }

    /**
     * Getter for cleaning list.
     * @param porcupineCleaningList the cleaning list.
     */
    public void setPorcupineCleaningList(String[] porcupineCleaningList){
        this.porcupineCleaningList = porcupineCleaningList;
    }

    /**
     * Getter for cleaning list.
     * @param beaverCleaningList the cleaning list.
     */
    public void setBeaverCleaningList(String[] beaverCleaningList){
        this.beaverCleaningList = beaverCleaningList;
    }

    /**
     * Getter for cleaning list.
     * @param racoonCleaningList the cleaning list.
     */
    public void setRacoonCleaningList(String[] racoonCleaningList){
        this.racoonCleaningList = racoonCleaningList;
    }
}
