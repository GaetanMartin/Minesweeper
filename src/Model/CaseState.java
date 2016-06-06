/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

/**
 * Enumerate the possible states of a case
 * @author Gaetan
 */
public enum CaseState {
    
    UNDISCOVERED,
    DISCOVERED,
    FLAGGED,
    EMPTY,
    TRIGGERED,
    TRAPPED,   
}
