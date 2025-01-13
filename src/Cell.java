import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

// מחלקה זו מייצגת תא בודד בתוך גיליון הנתונים
public class Cell {
    private String value; // ערך התא

    // בנאי: יוצר תא עם ערך התחלתי
    public Cell(String value) {
        this.value = value;
    }

    // מחזיר את הערך הנוכחי של התא
    public String getValue() {
        return value;
    }

    // מעדכן את הערך של התא
    public void setValue(String value) {
        this.value = value;
    }

    // בודק אם הערך הוא מספר
    public boolean isNumber(String text) {
        return text.matches("-?\\d+(\\.\\d+)?");
    }

    // בודק אם הערך הוא טקסט
    public boolean isText(String text) {
        return !isNumber(text) && !isFormula(text);
    }

    // בודק אם הערך הוא נוסחה (מתחיל ב"=")
    public boolean isFormula(String text) {
        return text.startsWith("=");
    }

    // מחשב נוסחה פשוטה כמו "=1+2" או "=3*5"
    public Double computeFormula(String formula) {
        if (!isFormula(formula)) return null;

        // מסיר את סימן "=" מההתחלה
        formula = formula.substring(1);

        // מחשב את הנוסחה באמצעות מנוע JavaScript
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            return Double.valueOf(engine.eval(formula).toString());
        } catch (Exception e) {
            return null; // אם הנוסחה לא חוקית
        }
    }
}
