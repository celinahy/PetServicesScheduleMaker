package edu.ucalgary.oop;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test suite for most of the project. Database tests are in DatabaseTest.java, since they require a
 * valid database plus user-login pair.
 * @author Sergiy Redko, Nicole Heather, Gio De Verra
 * @version 1.0
 * @since 1.0
 */
public class Tests {

    /**
     * Tests that the AnimalSpecies enum has the correct number of values and that the values are
     * correct.
     */
    @Test
    public void testAnimalSpeciesEnum() {
        assertEquals("AnimalSpecies enum has incorrect number of values.", 5, AnimalSpecies.values().length);
        assertEquals("AnimalSpecies enum has incorrect value of fox.", "FOX", AnimalSpecies.FOX.toString());
        assertEquals("AnimalSpecies enum has incorrect value of coyote", "COYOTE", AnimalSpecies.COYOTE.toString());
        assertEquals("AnimalSpecies enum has incorrect value of porcupine.", "PORCUPINE", AnimalSpecies.PORCUPINE.toString());
        assertEquals("AnimalSpecies enum has incorrect value of beaver.", "BEAVER", AnimalSpecies.BEAVER.toString());
        assertEquals("AnimalSpecies enum has incorrect value of racoon.", "RACOON", AnimalSpecies.RACOON.toString());
    }

    /**
     * Tests that the Constants class has the correct values for feeding preparation, feeding duration
     * and cleaning duration.
     */
    @Test
    public void testConstants() {
        assertEquals("Constants class has incorrect value of fox feeding duration.", 5, Constants.getFeedDuration(AnimalSpecies.FOX));
        assertEquals("Constants class has incorrect value of coyote feeding duration.", 5, Constants.getFeedDuration(AnimalSpecies.COYOTE));
        assertEquals("Constants class has incorrect value of porcupine feeding duration.", 5, Constants.getFeedDuration(AnimalSpecies.PORCUPINE));
        assertEquals("Constants class has incorrect value of beaver feeding duration.", 5, Constants.getFeedDuration(AnimalSpecies.BEAVER));
        assertEquals("Constants class has incorrect value of racoon feeding duration.", 5, Constants.getFeedDuration(AnimalSpecies.RACOON));

        assertEquals("Constants class has incorrect value of fox feeding preparation duration.", 5, Constants.getFeedPrep(AnimalSpecies.FOX));
        assertEquals("Constants class has incorrect value of coyote feeding preparation duration.", 10, Constants.getFeedPrep(AnimalSpecies.COYOTE));
        assertEquals("Constants class has incorrect value of porcupine feeding preparation duration.", 0, Constants.getFeedPrep(AnimalSpecies.PORCUPINE));
        assertEquals("Constants class has incorrect value of beaver feeding preparation duration.", 0, Constants.getFeedPrep(AnimalSpecies.BEAVER));
        assertEquals("Constants class has incorrect value of racoon feeding preparation duration.", 0, Constants.getFeedPrep(AnimalSpecies.RACOON));

        assertEquals("Constants class has incorrect value of fox cleaning duration.", 5, Constants.getCleanDuration(AnimalSpecies.FOX));
        assertEquals("Constants class has incorrect value of coyote cleaning duration.", 5, Constants.getCleanDuration(AnimalSpecies.COYOTE));
        assertEquals("Constants class has incorrect value of porcupine cleaning duration.", 10, Constants.getCleanDuration(AnimalSpecies.PORCUPINE));
        assertEquals("Constants class has incorrect value of beaver cleaning duration.", 5, Constants.getCleanDuration(AnimalSpecies.BEAVER));
        assertEquals("Constants class has incorrect value of racoon cleaning duration.", 5, Constants.getCleanDuration(AnimalSpecies.RACOON));

        assertEquals("Constants class has incorrect value of fox feeding start hour.", 0, Constants.getFeedStartHr(AnimalSpecies.FOX));
        assertEquals("Constants class has incorrect value of coyote feeding start hour.", 19, Constants.getFeedStartHr(AnimalSpecies.COYOTE));
        assertEquals("Constants class has incorrect value of porcupine feeding start hour.", 19, Constants.getFeedStartHr(AnimalSpecies.PORCUPINE));
        assertEquals("Constants class has incorrect value of beaver feeding start hour.", 8, Constants.getFeedStartHr(AnimalSpecies.BEAVER));
        assertEquals("Constants class has incorrect value of racoon feeding start hour.", 0, Constants.getFeedStartHr(AnimalSpecies.RACOON));
    }

    /**
     * Tests the getters of Treatment class.
     */
    @Test
    public void testTreatmentClass() {
        Treatment treatment = new Treatment(666, "Mr. Sandman", 999,998, 15, 23, "Bring me your dream", 3);
        assertEquals("getAnimalID() does not return correct animal ID.", 666, treatment.getAnimalID());
        assertEquals("getAnimalName() dos not return correct animal name.", "Mr. Sandman", treatment.getAnimalName());
        assertEquals("getTaskID() does not return correct task ID.", 999, treatment.getTaskID());
        assertEquals("getTaskName() does not return correct task name.", 998, treatment.getTreatmentID());
        assertEquals("getStartHour() does not return correct task start hour.", 15, treatment.getStartHour());
        assertEquals("getDuration() does not return correct task duration.", 23, treatment.getDuration());
        assertEquals("getDescription() does not return correct task description.", "Bring me your dream", treatment.getDescription());
        assertEquals("getMaxWindow() does not return correct task window.", 3, treatment.getMaxWindow());
    }

    /**
     * Tests that Treatment class throws an IllegalArgumentException when the start hour is not
     * between 0 and 23.
     */
    @Test
    public void testStartHourInvalidArgumentException(){
        try {
            Treatment treatment = new Treatment(666, "Mr. Sandman", 999,998, 24, 23, "Bring me your dream", 3);
            fail("Start hour should be between 0 and 23. Treatment() must throw an IllegalArgumentException when start hour is > 23");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Treatment treatment = new Treatment(666, "Mr. Sandman", 999,998, -1, 23, "Bring me your dream", 3);
            fail("Start hour should be between 0 and 23. Treatment() must throw an IllegalArgumentException when start hour is < 0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Tests that Treatment class throws an IllegalArgumentException when the duration is <= 0.
     */
    @Test
    public void testDurationIllegalArgumentException(){
        try {
            Treatment treatment = new Treatment(666, "Mr. Sandman", 999,998, 15, -1, "Bring me your dream", 3);
            fail("Duration should be >= 0. Treatment() must throw an IllegalArgumentException when duration is < 0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Tests that Treatment class throws an IllegalArgumentException when the max window is < 0.
     */
    @Test
    public void testMaxWindowIllegalArgumentException(){
        try {
            Treatment treatment = new Treatment(666, "Mr. Sandman", 999,998, 15, 23, "Bring me your dream", -1);
            fail("Max window should be >= 0. Treatment() must throw an IllegalArgumentException when max window is < 0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Tests that getRemainingTime returns the correct value.
     */
    @Test
    public void testGetRemainingTime(){
        Hour hour = new Hour();
        assertEquals("getRemainingTime returns incorrect value", 60, hour.getRemainingTime());
        hour.subRemainingTime(100);
        assertEquals("getRemainingTime returns incorrect value", -40, hour.getRemainingTime());
        hour.subRemainingTime(-15);
        assertEquals("getRemainingTime returns incorrect value", -25, hour.getRemainingTime());
    }

    /**
     * Tests that addRemainingTime produces the correct value.
     */
    @Test
    public void testAddRemainingTime(){
        Hour hour = new Hour();
        hour.addRemainingTime(20);
        assertEquals("getRemainingTime returns incorrect value", 80, hour.getRemainingTime());
        hour.addRemainingTime(-30);
        assertEquals("getRemainingTime returns incorrect value", 50, hour.getRemainingTime());
        hour.addRemainingTime(0);
        assertEquals("getRemainingTime returns incorrect value", 50, hour.getRemainingTime());
    }

    /**
     * Tests that subRemainingTime produces the correct value.
     */
    @Test
    public void testSubRemainingTime(){
        Hour hour = new Hour();
        hour.subRemainingTime(20);
        assertEquals("getRemainingTime returns incorrect value", 40, hour.getRemainingTime());
        hour.subRemainingTime(-90);
        assertEquals("getRemainingTime returns incorrect value", 130, hour.getRemainingTime());
        hour.subRemainingTime(0);
        assertEquals("getRemainingTime returns incorrect value", 130, hour.getRemainingTime());
    }

    /**
     * Tests that addTaskString produces the correct value.
     */
    @Test
    public void testAddTaskString(){
        Hour hour = new Hour();
        hour.addTaskString("New task - (3: animal, animal, animal)");
        assertEquals("AddTaskString incorrectly appends currentTasks", "New task - (3: animal, animal, animal)", hour.getCurrentTasks().toString());
        Hour hour2 = new Hour();
        hour2.addTaskString("Test");
        assertEquals("AddTaskString incorrectly appends currentTasks", "Test", hour2.getCurrentTasks().toString());
        hour2.addTaskString("");
        assertEquals("AddTaskString incorrectly appends currentTasks", "Test", hour2.getCurrentTasks().toString());

    }

    /**
     * Tests that stringDelete produces the correct value.
     */
    @Test
    public void testStringDelete(){
        Hour hour = new Hour();
        hour.addTaskString("12345");
        hour.taskStringDelete(3,4);
        assertEquals("StringDelete incorrectly deletes currentTasks", "1235", hour.getCurrentTasks().toString());
        try{
            hour.taskStringDelete(7, 3);
        }
        catch(IllegalArgumentException e){}
        //Expected
    }

    /**
     * Tests that getBackupStatus returns the correct value.
     */
    @Test
    public void testGetBackupStatus(){
        Hour hour = new Hour();
        assertEquals("GetBackupStatus returns the wrong value", ExtraVolunteerStatus.NOTNEEDED, hour.getBackupStatus());
        hour.setBackupStatus(ExtraVolunteerStatus.DENIED);
        assertEquals("GetBackupStatus returns the wrong value", ExtraVolunteerStatus.DENIED, hour.getBackupStatus());
        hour.setBackupStatus(ExtraVolunteerStatus.APPROVED);
        assertEquals("GetBackupStatus returns the wrong value", ExtraVolunteerStatus.APPROVED, hour.getBackupStatus());
    }

    /**
     * Tests that setBackupStatus produces the correct value.
     */
    @Test
    public void testSetBackupStatus(){
        Hour hour = new Hour();
        hour.setBackupStatus(ExtraVolunteerStatus.APPROVED);
        assertEquals("SetBackupStatus returns wrong value", ExtraVolunteerStatus.APPROVED, hour.getBackupStatus());
        hour.setBackupStatus(ExtraVolunteerStatus.REQUESTED);
        assertEquals("SetBackupStatus returns wrong value", ExtraVolunteerStatus.REQUESTED, hour.getBackupStatus());
    }

    /**
     * Tests Print returns the correct value.
     */
    @Test
    public void testPrint(){
        Hour hour = new Hour();
        hour.addTaskString("New task - (3: animal, animal, animal)");
        assertEquals("Print returns the incorrect value", "New task - (3: animal, animal, animal)", hour.getCurrentTasks().toString());
        hour.addTaskString(" Testing for Print");
        assertEquals("Print returns the incorrect value", "New task - (3: animal, animal, animal) Testing for Print", hour.getCurrentTasks().toString());
        hour.taskStringDelete(0, hour.getCurrentTasks().length() - 1);
        assertEquals("Print returns the incorrect value", "t", hour.getCurrentTasks().toString());
    }

    /**
     * Tests that Hour() returns the creates a correct Hour object and returns correct hour data members.
     */
    @Test
    public void testHourClass(){
        Hour hour = new Hour();
        assertEquals("Hour has incorrect remainingTime", 60, hour.getRemainingTime());
        assertEquals("Hour has incorrect currentTasks", "", hour.getCurrentTasks().toString());
        assertEquals("Hour has incorrect backupstatus", ExtraVolunteerStatus.NOTNEEDED, hour.getBackupStatus());
        hour.subRemainingTime(-65);
        hour.setBackupStatus(ExtraVolunteerStatus.REQUESTED);
        hour.addTaskString("New Test");
        assertEquals("Hour has incorrect remainingTime", 125, hour.getRemainingTime());
        assertEquals("Hour has incorrect currentTasks", "New Test", hour.getCurrentTasks().toString());
        assertEquals("Hour has incorrect backupstatus", ExtraVolunteerStatus.REQUESTED, hour.getBackupStatus());
    }


    /**
     * Tests that getHours returns the correct hour objects.
     */
    @Test
    public void testGetHours(){
        Schedule test = new Schedule();
        Hour[] testArray = test.getHours();

        for(int i = 0; i < 24; i++) {
            testArray[i].subRemainingTime(10);
            testArray[i].setBackupStatus(ExtraVolunteerStatus.REQUESTED);
            testArray[i].addTaskString("Hello");
        }
        for(int i = 0; i < 24; i++){
            assertEquals("Hour has incorrect remainingTime", 50, testArray[i].getRemainingTime());
            assertEquals("Hour has incorrect currentTasks", "Hello", testArray[i].getCurrentTasks().toString());
            assertEquals("Hour has incorrect backupstatus", ExtraVolunteerStatus.REQUESTED, testArray[i].getBackupStatus());
        }
    }

    /**
     * Tests that all setters for each list other than treatment in schedule can be changes manually.
     */
    @Test
    public void testSetLists(){
        try {
            Schedule test = new Schedule();
            String[] newList = new String[]{"one", "two", "three"};
            test.setFoxCleaningList(newList);
            test.setFoxFeedingList(newList);
            test.setPorcupineCleaningList(newList);
            test.setPorcupineFeedingList(newList);
            test.setBeaverCleaningList(newList);
            test.setBeaverFeedingList(newList);
            test.setCoyoteCleaningList(newList);
            test.setCoyoteFeedingList(newList);
            test.setRaccoonFeedingList(newList);
            test.setRacoonCleaningList(newList);
            test.assignCleanings();
            test.assignFeedings();
            assertEquals("setList functions failed to update lists","    - Cleaning - fox (3: one, two, three)\n" +
                    "    - Cleaning - beaver (3: one, two, three)\n" +
                    "    - Cleaning - coyote (3: one, two, three)\n" +
                    "    - Cleaning - racoon (3: one, two, three)\n", test.getHours()[0].getCurrentTasks().toString());
            assertEquals("setList functions failed to update lists","    - Feeding - fox (3: one, two, three)\n" +
                    "    - Feeding - racoon (3: one, two, three)\n", test.getHours()[2].getCurrentTasks().toString());
        }
        catch(CannotCreateScheduleException | NullPointerException e){}
    }

    /**
     * Tests that find slot simple task is able to find valid slots, and if not throws CannotCreateScheduleException.
     */
    @Test
    public void testFindSlotSimpleTask(){
        Schedule test = new Schedule();
        try {
            int error = test.findSlotSimpleTask(0, 100, 24);
            if(error < 0 ){
                throw new CannotCreateScheduleException("Slot not found");
            }
        }
        catch(CannotCreateScheduleException e){}
        //Expected

        int error = test.findSlotSimpleTask(22, 5, 2);
        assertEquals("findSlotSimpleTask could not find slot for valid duration, start hour and window", 22, error);

    }

    /**
     * Tests that assignVolunteer correctly assigns backup if valid, and checks to see if a volunteer is  already assigned.
     */
    @Test
    public void testAssignVolunteer(){
        Schedule test = new Schedule();
        boolean value = test.assignVolunteer(0, 1);
        assertEquals("assignVolunteer was not able to assign volunteers to valid hours", true, value);
        boolean value2 = test.assignVolunteer(0, 1);
        assertEquals("assignVolunteer wrongly assigned volunteers to hours that already had them", false, value2);
    }

    /**
     * Tests to see if assignCleanings and assignFeedings correctly throws CannotCreateScheduleException
     */
    @Test
    public void testCannotCreateScheduleException() {
        try {
            Schedule test = new Schedule();
            for (int i = 0; i < 24; i++) {
                test.getHours()[i].subRemainingTime(60);
            }
            String[] newList = new String[]{"one", "two", "three"};
            test.setFoxCleaningList(newList);
            test.setFoxFeedingList(newList);
            test.setPorcupineCleaningList(newList);
            test.setPorcupineFeedingList(newList);
            test.setBeaverCleaningList(newList);
            test.setBeaverFeedingList(newList);
            test.setCoyoteCleaningList(newList);
            test.setCoyoteFeedingList(newList);
            test.setRaccoonFeedingList(newList);
            test.setRacoonCleaningList(newList);
            test.assignCleanings();
            test.assignFeedings();
        } catch (CannotCreateScheduleException e) {
        }
        //Expected
    }

    @Test
    public void testAssignFeedings(){
        try {
            Schedule test = new Schedule();
            String[] newList = new String[]{"one", "two", "three"};
            test.setFoxFeedingList(newList);
            test.setPorcupineFeedingList(newList);
            test.setBeaverFeedingList(newList);
            test.setCoyoteFeedingList(newList);
            test.setRaccoonFeedingList(newList);
            test.assignFeedings();
            assertEquals("assignFeedings was unable to correctly assign tasks", "    - Feeding - fox (3: one, two, three)\n", test.getHours()[0].print());

            newList = new String[]{"one", "two", "three", "four", "five"};
            test.setFoxFeedingList(newList);
            test.setPorcupineFeedingList(newList);
            test.setBeaverFeedingList(newList);
            test.setCoyoteFeedingList(newList);
            test.setRaccoonFeedingList(newList);
            test.assignFeedings();
            assertEquals("assignFeedings was unable to correctly assign tasks", "    - Feeding - racoon (3: one, two, three)\n" +
                        "    - Feeding - racoon (5: one, two, three, four, five)\n", test.getHours()[1].print());
        }
        catch(CannotCreateScheduleException e){}
    }

    /**
     * Tests to see if assignCleanings will correctly be assigned in hours based on their individual cleaning times.
     */
    @Test
    public void testAssignCleanings() {
        try {
            Schedule test = new Schedule();
            String[] newList = new String[]{"one", "two", "three"};
            test.setFoxCleaningList(newList);
            test.setBeaverCleaningList(newList);
            test.setCoyoteCleaningList(newList);
            test.setRacoonCleaningList(newList);
            test.setPorcupineCleaningList(newList);
            test.assignCleanings();

            assertEquals("assignCleanings could not correctly assign cleanings for hour 0", "    - Cleaning - fox (3: one, two, three)\n" +
                    "    - Cleaning - beaver (3: one, two, three)\n" +
                    "    - Cleaning - coyote (3: one, two, three)\n" +
                    "    - Cleaning - racoon (3: one, two, three)\n", test.getHours()[0].print());

            newList = new String[]{"one", "two", "three", "four", "five"};
            test.setFoxCleaningList(newList);
            test.setBeaverCleaningList(newList);
            test.setCoyoteCleaningList(newList);
            test.setRacoonCleaningList(newList);
            test.setPorcupineCleaningList(newList);
            test.assignCleanings();

            assertEquals("assignCleanings could not correctly assign cleanings for hour 1", "    - Cleaning - porcupine (3: one, two, three)\n" +
                    "    - Cleaning - fox (5: one, two, three, four, five)\n" +
                    "    - Cleaning - beaver (1: one)\n", test.getHours()[1].print());
            assertEquals("assignCleanings could not correctly assign cleanings for hour 1", "    - Cleaning - beaver (4: two, three, four, five)\n" +
                    "    - Cleaning - coyote (5: one, two, three, four, five)\n" +
                    "    - Cleaning - racoon (3: one, two, three)\n", test.getHours()[2].print());

        } catch (CannotCreateScheduleException e) {}
    }

    /**
     * Tests for correct exception when too many animals in one list make it impossible to create schedule
     */
   @Test
    public void testTooManyAnimalsThrowsCannotCreateScheduleException(){
        try {
            String[] newList = new String[100];
            Schedule test = new Schedule();
            for(int i = 0; i < 50; i++){
                newList[i] = "Stinky";
            }
            test.setFoxFeedingList(newList);
            test.setFoxCleaningList(newList);
            test.assignFeedings();
            test.assignCleanings();
        }
        catch(CannotCreateScheduleException e){}
        //Expected
    }

    /**
     * Test that formatTask is able to correctly append new tasks to a given hour
     */
    @Test
    public void testFormatTask(){
       Schedule test = new Schedule();
       String[] newList = new String[]{"one", "two", "three", "four", "five"};
       test.formatTask(0, newList, AnimalSpecies.FOX, "Feeding");
       assertEquals("formatTask does not append the correct string into chosen hour of 0", "    - Feeding - fox (5: one, two, three, four, five)"+System.getProperty("line.separator") ,
               test.getHours()[0].getCurrentTasks().toString());
       test.formatTask(0, newList, AnimalSpecies.RACOON, "Cleaning");
        assertEquals("formatTask does not append the correct string into chosen hour of 0",     "    - Feeding - fox (5: one, two, three, four, five)"+System.getProperty("line.separator") +
        "    - Cleaning - racoon (5: one, two, three, four, five)"+System.getProperty("line.separator"), test.getHours()[0].getCurrentTasks().toString());
    }

    /**
     * Tests that assignCleaningsToHour correctly
     */
    @Test
    public void testAssignCleaningsToHour(){
        Schedule test = new Schedule();
        ArrayList<String> newList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            newList.add("Stinky");
        }
        test.assignCleaningsToHour(newList, AnimalSpecies.PORCUPINE, 1);
        assertEquals("assignCleaningsToHour incorrectly appends task to hour 1", "    - Cleaning - porcupine (4: Stinky, Stinky, Stinky, Stinky)\n", test.getHours()[1].getCurrentTasks().toString());
    }

    /**
     * Tests that AssignCleanings and AssignCleaning throws proper exceptions
     */
    @Test
    public void testAssignCleaningsThrowsCannotCreateSchedule(){
        try {
            Schedule test = new Schedule();
            String[] newList = new String[]{"one", "two", "three"};
            test.setRacoonCleaningList(newList);
            test.setFoxCleaningList(newList);
            test.setCoyoteCleaningList(newList);
            test.setPorcupineCleaningList(newList);
            test.setBeaverCleaningList(newList);
            for (int i = 0; i < 24; i++) {
                test.getHours()[i].subRemainingTime(40);
            }
            test.assignCleanings();
        }
        catch(CannotCreateScheduleException e){}
        //Expected
    }

    /**
     * Tests that AssignFeedings and AssignFeeding throws proper exceptions
     */
    @Test
    public void testAssignFeedingsThrowsCannotCreateSchedule(){
        try {
            Schedule test = new Schedule();
            String[] newList = new String[]{"one", "two", "three"};
            test.setRaccoonFeedingList(newList);
            test.setFoxFeedingList(newList);
            test.setCoyoteFeedingList(newList);
            test.setPorcupineFeedingList(newList);
            test.setBeaverFeedingList(newList);
            for (int i = 0; i < 24; i++) {
                test.getHours()[i].subRemainingTime(40);
            }
            test.assignFeedings();
        }
        catch(CannotCreateScheduleException e){}
        //Expected
    }
}
