import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_NAME = "notes.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========= Notes App =========");
            System.out.println("1. Add Note");
            System.out.println("2. View All Notes");
            System.out.println("3. Search Notes");
            System.out.println("4. Delete a Note");
            System.out.println("5. Clear All Notes");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> addNote(sc);
                case "2" -> viewNotes();
                case "3" -> searchNotes(sc);
                case "4" -> deleteNote(sc);
                case "5" -> clearAllNotes(sc);
                case "6" -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please select 1–6.");
            }
        }
    }

    /** Add a new note to the file */
    private static void addNote(Scanner sc) {
        System.out.print("Enter your note: ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) {
            System.out.println("⚠️ Empty note not saved!");
            return;
        }
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) { // append mode
            writer.write(note + System.lineSeparator());
            System.out.println("Note saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /** Read all notes into a list */
    private static List<String> readAllNotes() {
        List<String> notes = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return notes;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return notes;
    }

    /** Display all notes with line numbers */
    private static void viewNotes() {
        List<String> notes = readAllNotes();
        if (notes.isEmpty()) {
            System.out.println("No notes found.");
            return;
        }
        System.out.println("\n--- Your Notes ---");
        for (int i = 0; i < notes.size(); i++) {
            System.out.println((i + 1) + ". " + notes.get(i));
        }
    }

    /** Search notes by keyword (case-insensitive) */
    private static void searchNotes(Scanner sc) {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }

        List<String> notes = readAllNotes();
        boolean found = false;
        System.out.println("\n--- Search Results ---");
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + notes.get(i));
                found = true;
            }
        }
        if (!found) System.out.println("No notes match your search.");
    }

    /** Delete a specific note by number */
    private static void deleteNote(Scanner sc) {
        List<String> notes = readAllNotes();
        if (notes.isEmpty()) {
            System.out.println("No notes to delete.");
            return;
        }

        viewNotes();
        System.out.print("Enter the note number to delete: ");
        String input = sc.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= notes.size()) {
                System.out.println("Invalid note number.");
                return;
            }
            notes.remove(index);
            writeAllNotes(notes);
            System.out.println("Note deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /** Clear all notes with confirmation */
    private static void clearAllNotes(Scanner sc) {
        System.out.print("Are you sure you want to delete ALL notes? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (confirm.equals("y")) {
            writeAllNotes(Collections.emptyList());
            System.out.println("All notes cleared!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    /** Overwrite the file with the given list of notes */
    private static void writeAllNotes(List<String> notes) {
        try (FileWriter writer = new FileWriter(FILE_NAME, false)) { // overwrite mode
            for (String note : notes) {
                writer.write(note + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
