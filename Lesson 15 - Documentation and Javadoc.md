# Lesson 15 - Documentation and Javadoc

When you call `word.length()` you don't open Java's `String.java` to see how it works. You read what it does, what it needs, and what it gives back. That description is **documentation**, and Java's is written in a special comment format called **Javadoc**.

Your classes need the same thing. Someone using your `Enemy` class (including you) should be able to use it without reading its code.

---

## 1. Three Kinds of Comments

The compiler ignores all three:

```java
// line comment: everything after // on this line

/* block comment:
   can run over several lines */

/**
 * Javadoc comment: starts with /** and goes directly above
 * a class, constructor, or method.
 */
```

- **`//`** explains a line or a small step **inside** a method.
- **`/* */`** is for longer notes, like the `Name / Description` header at the top of each file.
- **`/** */`** describes a class, constructor, or method for the people who **use** it. VS Code and the `javadoc` tool read these. They ignore the other two.

You will be expected to provide descriptive comments throughout your program as part of your project assessment.

---

## 2. Writing a Javadoc Comment

```java
/**
 * Lowers this enemy's health by the amount of a hit.
 *
 * @param amount the damage from the hit
 */
public void takeDamage(int amount) {
```

Every Javadoc comment has:

1. **A description.** The first sentence is the summary, so make it say what the method does: "Lowers this enemy's health...", not "This method is a method that...".
2. **One `@param` per parameter**, in order: the tag, the parameter's **exact name**, then what it means.
3. **`@return`** if the method returns something: what the value means. No `@return` on a `void` method or a constructor.

```java
/**
 * Checks whether this enemy is out of health.
 *
 * @return true if health is 0, false otherwise
 */
public boolean isDefeated() {
```

The class gets one too, above `public class`, saying what one object of the class **is**. `@author` goes here:

```java
/**
 * An enemy in a game. Every enemy has a name, a health that drops as it
 * takes damage, and the damage it does when it attacks.
 *
 * @author Mr Kowalczewski
 */
public class Enemy {
```

If the method has `@Override` above it, the Javadoc goes **above** the `@Override`.

---

## 3. What to Write

Javadoc is written for the **caller**: what goes in, what comes out, what changes. Not how.

| Weak | Better |
|---|---|
| `@param amount the amount` | `@param amount the damage from the hit` |
| `Subtracts amount from health and sets it to 0 if it's negative.` | `Lowers this enemy's health by the amount of a hit.` |
| `@return health` | `@return the health, never below 0` |

The weak versions repeat the name or describe the code line by line. If the code changes, they're wrong. "The damage from the hit" stays true however `takeDamage` is written.

`//` comments inside a method are for the **why** that the code can't show:

```java
// stop at 0 so a big hit can't leave negative health on screen
if (health < 0) {
    health = 0;
}
```

`// if health is less than 0, set it to 0` adds nothing. Anyone reading Java can see that.

---

## 4. Preconditions and Postconditions

A **precondition** is what must be true **before** the method runs for it to work. A **postcondition** is what's guaranteed to be true **after**. Both go in the Javadoc:

```java
/**
 * Lowers this enemy's health by the amount of a hit.
 *
 * Precondition: amount >= 0.
 * Postcondition: health is lower by amount, but never below 0.
 *
 * @param amount the damage from the hit
 */
```

The method **doesn't check** its preconditions. Keeping them true is the **caller's** job. Break one and the result is nonsense:

```java
Enemy troll = new Enemy("Troll", 50, 9);
troll.takeDamage(-20);
System.out.println(troll.getHealth());     // 70: the hit healed it
```

So the caller checks first:

```java
if (hit >= 0) {
    troll.takeDamage(hit);
}
```

Compare this with Lesson 12, where `withdraw` **checked** the amount and returned `false`. Both are fine designs. Either the method checks (and the Javadoc says what happens on bad input), or the method has a precondition (and the Javadoc says what the caller must guarantee). What's not fine is neither: no check and no precondition.

*You will not be required to add pre/post conditions in your documentation, but AP students need to know what these mean.*

---

## 5. Reading Documentation

**Hover.** Put the mouse over `takeDamage` anywhere it's called. VS Code shows the Javadoc from `Enemy.java` without opening the file. Hover over `length()` on a `String` and you see Java's own Javadoc. Every page of the official Java API documentation is generated from comments like yours.

**Generate the web pages.** From the lesson folder:

```bash
javadoc -d out/docs src/Enemy.java
```

Open `out/docs/Enemy.html` in a browser. It has the same layout as Oracle's page for `String`. Only `public` members appear: private attributes and helper methods are hidden from the people using the class.

---

## 6. Common Documentation Bugs

**`/*` instead of `/**`.** It's a plain block comment. Hover and `javadoc` ignore it.

**`@param` name doesn't match the parameter** (`@param money` on `addPayment(double dollars)`). `javadoc` stops with `error: @param name not found`.

**`@return` on a `void` method or constructor**, or none on a method that returns something.

**`<` in a Javadoc comment** (`Precondition: minutes < 60`). Javadoc is HTML, so `javadoc` stops with `error: malformed HTML`. Write `{@code minutes < 60}` or reword it with `>`.

**Stale comments.** The code changed, the comment didn't. A wrong comment is worse than none. When you change what a method does, change its Javadoc in the same edit.

---

## Try It Yourself

Run the companion file from the `src` folder:

```bash
javac DocumentationDemo.java
java DocumentationDemo
```

`DocumentationDemo` uses the documented `Enemy.java`. Open both side by side and hover over each method call.

Then work on the exercise in `ParkingMeter.java`. One method has Javadoc and no code. The rest have code and no Javadoc.

### AP Extension

AP free-response questions describe every method with a comment and preconditions, then say "assume the precondition is met". For each method in `ParkingMeter`, write down one call that **breaks** its precondition and what the method would do. Which ones give a wrong answer quietly, with no error?

---

## Course Expectations

### ICS 4U

"I can document every class, constructor, and method with Javadoc (description, `@param`, `@return`), state preconditions and postconditions, and use `//` comments to explain why" is a requirement of the course (A4.3, C2.1) and will be expected in your programming project.

### AP Expectations

The following are expectations of the AP exam and will show up on the final exam:

- Comments are ignored by the compiler; Java has `//` line comments, `/* */` block comments, and `/** */` Javadoc comments, which are used to create API documentation (Topic 1.8, 1.8.A.1)
- A precondition must be true just before a method runs for it to behave as expected; the method is not expected to check it (1.8.A.2)
- A postcondition must always be true after a method runs; it describes what's returned or the object's attributes afterward (1.8.A.3)
- An API specification tells a programmer how to use a library's classes, and its documentation describes each class's attributes and behaviours (Topic 1.7, 1.7.A.1)
- Procedural abstraction: a method can be used by knowing what it does, without knowing how it was written (Topic 1.9, 1.9.A.1)
