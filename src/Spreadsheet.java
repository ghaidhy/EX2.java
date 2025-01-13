// מחלקה זו מייצגת גיליון נתונים שמכיל רשת של תאים
public class Spreadsheet {
    private Cell[][] cells; // רשת של תאים

    // בנאי: יוצר גיליון נתונים בגודל מסוים
    public Spreadsheet(int width, int height) {
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(""); // תא ריק בתחילה
            }
        }
    }

    // מעדכן את הערך של תא מסוים
    public void setCell(int x, int y, String value) {
        if (isValidCoordinate(x, y)) {
            cells[x][y].setValue(value);
        } else {
            System.out.println("Invalid cell coordinates.");
        }
    }

    // מחזיר את הערך של תא מסוים
    public String getCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return cells[x][y].getValue();
        }
        return "Invalid cell coordinates.";
    }

    // בודק אם הקואורדינטות חוקיות
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < cells.length && y >= 0 && y < cells[0].length;
    }

    // ממיר רפרנס כמו "A1" לקואורדינטות מספריות
    public int[] parseReference(String ref) {
        try {
            int col = ref.charAt(0) - 'A'; // ממיר את העמודה (A→0, B→1)
            int row = Integer.parseInt(ref.substring(1)) - 1; // ממיר את השורה
            return new int[]{col, row};
        } catch (Exception e) {
            return null; // אם הרפרנס לא חוקי
        }
    }

    // מחזיר את הערך של תא מרפרנס (כמו "A1")
    private Double getCellValueFromReference(String ref) {
        int[] coords = parseReference(ref);
        if (coords == null || !isValidCoordinate(coords[0], coords[1])) {
            return null; // אם הרפרנס לא חוקי
        }
        String cellValue = cells[coords[0]][coords[1]].getValue();

        if (cells[coords[0]][coords[1]].isNumber(cellValue)) {
            return Double.parseDouble(cellValue); // אם זה מספר, מחזיר את הערך המספרי
        } else if (cells[coords[0]][coords[1]].isFormula(cellValue)) {
            return computeFormulaWithReferences(cellValue); // מחשב את הנוסחה
        }
        return null; // אם זה טקסט או לא חוקי
    }

    // מחשב נוסחה שכוללת רפרנסים לתאים אחרים
    public Double computeFormulaWithReferences(String formula) {
        if (!formula.startsWith("=")) return null;

        formula = formula.substring(1);

        // מחליף רפרנסים (כמו "A1") בערכים שלהם
        for (int i = 0; i < formula.length(); i++) {
            if (Character.isLetter(formula.charAt(i))) {
                int j = i + 1;
                while (j < formula.length() && Character.isDigit(formula.charAt(j))) j++;

                String ref = formula.substring(i, j);
                Double refValue = getCellValueFromReference(ref);
                if (refValue != null) {
                    formula = formula.replace(ref, refValue.toString());
                } else {
                    return null; // אם הרפרנס לא חוקי
                }
            }
        }

        return new Cell("").computeFormula("=" + formula);
    }

    // מחשב את כל הערכים בגיליון
    public String[][] evalAll() {
        String[][] results = new String[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                String value = cells[x][y].getValue();

                if (cells[x][y].isNumber(value)) {
                    results[x][y] = value; // אם זה מספר
                } else if (cells[x][y].isFormula(value)) {
                    Double result = computeFormulaWithReferences(value);
                    results[x][y] = (result != null) ? result.toString() : "ERR_FORM";
                } else if (cells[x][y].isText(value)) {
                    results[x][y] = value; // אם זה טקסט
                }
            }
        }

        return results;
    }

    // מדפיס את הגיליון עם הערכים המחושבים
    public void printEvaluatedSpreadsheet() {
        String[][] results = evalAll();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                System.out.print(results[x][y] + "\t");
            }
            System.out.println();
        }
    }

    public int width() {
        return cells.length;
    }

    public int height() {
        return cells[0].length;
    }
}
