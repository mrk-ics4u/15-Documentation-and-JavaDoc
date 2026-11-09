# ICS 4U0 — Lesson 15: Documentation and Javadoc

## Exercise

The city's new parking meter class works, but nobody documented it. The program that runs the meter is already written, in `src/MeterApp.java`. It reads the meter's settings and a list of payments and waits, and prints what happens. **It only works once your `ParkingMeter` class does.**

`src/ParkingMeter.java` has two jobs for you:

1. **Write `addPayment` from its Javadoc.** Its comment is complete and its body is a placeholder. The Javadoc is the whole specification: read it carefully, including the precondition and postcondition.
2. **Document everything else.** Every other constructor and method has working code and no Javadoc. Write one for each, and one for the class.

Everything you need is from Lesson 15:

- a **class Javadoc** above `public class ParkingMeter` (the `/* Name: */` header doesn't count),
- a Javadoc above **every constructor and method**, with a **description**, one **`@param`** per parameter (exact name, in order, with a description), and **`@return`** only on methods that return something,
- a **`Precondition:`** line for `timePasses` (it only works for `minutes >= 0`). Add preconditions and postconditions anywhere else they apply.

```
┌─────────────────────────────────────────────┐
│ ParkingMeter                                │
├─────────────────────────────────────────────┤
│ - minutesPerDollar : int                    │
│ - maxMinutes : int                          │
│ - minutesLeft : int                         │
├─────────────────────────────────────────────┤
│ + ParkingMeter(minutesPerDollar : int,      │
│                maxMinutes : int)            │
│ + getMinutesLeft() : int                    │
│ + getMaxMinutes() : int                     │
│ + addPayment(dollars : double) : int        │
│ + timePasses(minutes : int) : void          │
│ + isExpired() : boolean                     │
│ + costToFill() : double                     │
└─────────────────────────────────────────────┘
```

- **Don't change any method headers**, and **don't change `MeterApp.java`**.
- Some tests read your Javadoc. Their failure messages say which method and which tag to fix.
- Check your work by hovering over each method in `MeterApp.java`. What VS Code shows is what another programmer would see.

---

## Input

Three lines, then one line per event (`MeterApp` reads them for you):

| Line | Value | Type |
|------|-------|------|
| 1 | Minutes each dollar buys | whole number |
| 2 | Most minutes the meter can hold | whole number |
| 3 | Number of events, `n` | whole number |
| next `n` | `pay` and a dollar amount, or `wait` and a number of minutes | text, then a number |

Example input:

```
20
120
4
pay 2.50
wait 30
pay 6.00
wait 45
```

---

## Output

```
Paid $<dollars>: <added> min added, <left> min left      (one line per pay)
Waited <minutes> min: <left> min left                     (one line per wait)
Expired: <yes or no>
Cost to fill: $<cost>
```

For the example input above, the program must print **exactly**:

```
Paid $2.50: 50 min added, 50 min left
Waited 30 min: 20 min left
Paid $6.00: 100 min added, 120 min left
Waited 45 min: 75 min left
Expired: no
Cost to fill: $2.25
```

`MeterApp` prints these lines, so every difference comes from your `ParkingMeter` class.

---

## Testing

- Test your code yourself first.
- Open the **Testing** panel from the sidebar (flask icon) and click ▶ **Run Tests**.

A green check next to a test means it passed; a red X means it failed and will show you the expected vs. actual output.
