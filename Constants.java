package edu.ucalgary.oop;

/**
 * This class contains all the constants used in the program.
 *
 * @author Sergiy Redko
 * @version 2.0
 * @since 1.0
 */
public class Constants {

    /**
     * Gets feeding preparation time for a given animal species.
     * @param species the animal species.
     * @return the feeding preparation time in minutes.
     */
    public static int getFeedPrep(AnimalSpecies species){
        switch(species){
            case FOX:
                return 5;
            case COYOTE:
                return 10;
            case PORCUPINE:
                return 0;
            case BEAVER:
                return 0;
            case RACOON:
                return 0;
            default:
                return 0;
        }
    }

    /**
     * Gets feeding duration for a given animal species.
     * @param species the animal species.
     * @return the feeding duration in minutes.
     */
    public static int getFeedDuration(AnimalSpecies species){
        switch(species){
            case FOX:
                return 5;
            case COYOTE:
                return 5;
            case PORCUPINE:
                return 5;
            case BEAVER:
                return 5;
            case RACOON:
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Gets cleaning time for a given animal species.
     * @param species the animal species.
     * @return the cleaning duration in minutes.
     */
    public static int getCleanDuration(AnimalSpecies species){
        switch(species){
            case FOX:
                return 5;
            case COYOTE:
                return 5;
            case PORCUPINE:
                return 10;
            case BEAVER:
                return 5;
            case RACOON:
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Gets the window of time to feed an animal
     * @param species the animal species.
     * @return the number indicating which window of time to feed them (24-hour clock)
     */
    public static int getFeedStartHr(AnimalSpecies species){
        switch(species){
            case FOX:
                return 0;
            case COYOTE:
                return 19;
            case PORCUPINE:
                return 19;
            case BEAVER:
                return 8;
            case RACOON:
                return 0;
            default:
                return 3;
        }
    }
}
