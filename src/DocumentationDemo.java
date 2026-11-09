/*
 * Name:        Lesson 15: Documentation and Javadoc (DocumentationDemo.java)
 * Description: Uses the documented Enemy class: the three kinds of comments,
 *              reading Javadoc by hovering, and what happens when a caller
 *              ignores a precondition.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */

/**
 * Lesson 15 demo. Open Enemy.java beside this file.
 */
public class DocumentationDemo {

    /**
     * Runs the demo.
     *
     * @param args not used
     */
    public static void main(String[] args) {

        // the three kinds of comments: the compiler ignores all of them
        // this is a line comment: everything after // on this line
        /* this is a block comment:
           it can run over several lines */
        /** this is a Javadoc comment: it goes directly above a class, constructor, or method */

        // hover: put the mouse over Enemy, takeDamage, or isDefeated below.
        // VS Code shows the Javadoc from Enemy.java without opening the file.
        Enemy goblin = new Enemy("Goblin", 30, 4);
        goblin.takeDamage(12);
        System.out.println(goblin.getName() + " has " + goblin.getHealth() + " health");
        System.out.println("Defeated? " + goblin.isDefeated());

        // postcondition: after takeDamage, health is never below 0
        goblin.takeDamage(100);
        System.out.println("After a huge hit: " + goblin.getHealth() + " health");

        // precondition: takeDamage needs amount >= 0. The method doesn't check.
        // Breaking the precondition is the CALLER's bug, and the result is nonsense:
        Enemy troll = new Enemy("Troll", 50, 9);
        troll.takeDamage(-20);
        System.out.println("Troll after -20 damage: " + troll.getHealth() + " health");

        // the caller keeps the precondition true by checking first
        int hit = -20;
        if (hit >= 0) {
            troll.takeDamage(hit);
        } else {
            System.out.println("Ignored a negative hit");
        }

        // Java's own classes are documented the same way: hover over length() and toUpperCase()
        String word = "javadoc";
        System.out.println(word.length() + " letters: " + word.toUpperCase());
    }

}
