import java.util.Scanner;

// מחלקה ראשית להפעלת התוכנית
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Spreadsheet sheet = new Spreadsheet(3, 3); // יצירת גיליון בגודל 3x3

        System.out.println("Welcome to the Spreadsheet Program!");
        System.out.println("You can enter values or formulas for cells.");
        System.out.println("Type 'exit' to stop.");

        while (true) {
            System.out.print("Enter cell (e.g., A1): ");
            String cell = scanner.nextLine();
            if (cell.equalsIgnoreCase("exit")) break;

            System.out.print("Enter value (number, text, or formula): ");
            String value = scanner.nextLine();

            int[] coords = sheet.parseReference(cell);
            if (coords != null && coords[0] >= 0 && coords[1] >= 0) {
                sheet.setCell(coords[0], coords[1], value); // מעדכן את ערך התא
            } else {
                System.out.println("Invalid cell reference. Please try again.");
            }
        }

        System.out.println("\nEvaluated Spreadsheet:");
        sheet.printEvaluatedSpreadsheet(); // מדפיס את הגיליון הסופי
    }
}
