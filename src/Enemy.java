/*
 * Name:        Lesson 15: Documentation and Javadoc (Enemy.java)
 * Description: The Enemy class from Lesson 11, now fully documented with
 *              Javadoc. The code is unchanged. Used by DocumentationDemo.java.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */

/**
 * An enemy in a game. Every enemy has a name, a health that drops as it
 * takes damage, and the damage it does when it attacks.
 *
 * @author Mr Kowalczewski
 */
public class Enemy {

    private String name;
    private int health;
    private int damage;

    /**
     * Creates an enemy with full health.
     *
     * Precondition: health > 0 and damage >= 0.
     *
     * @param name   the name shown in the game
     * @param health the starting health
     * @param damage the damage done by each attack
     */
    public Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    /**
     * Returns this enemy's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this enemy's current health.
     *
     * @return the health, never below 0
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the damage this enemy does with each attack.
     *
     * @return the damage per attack
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Changes this enemy's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the damage this enemy does with each attack.
     *
     * Precondition: damage >= 0.
     *
     * @param damage the new damage per attack
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Attacks once.
     *
     * @return the damage dealt by this attack
     */
    public int attack() {
        return damage;
    }

    /**
     * Lowers this enemy's health by the amount of a hit.
     *
     * Precondition: amount >= 0.
     * Postcondition: health is lower by amount, but never below 0.
     *
     * @param amount the damage from the hit
     */
    public void takeDamage(int amount) {
        health = health - amount;
        // stop at 0 so a big hit can't leave negative health on screen
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Checks whether this enemy is out of health.
     *
     * @return true if health is 0, false otherwise
     */
    public boolean isDefeated() {
        return health == 0;
    }
}
