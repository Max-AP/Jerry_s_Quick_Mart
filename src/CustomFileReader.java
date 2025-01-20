import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomFileReader {
    private Scanner fileReader;

    public CustomFileReader(String fileName) {
        try {
            File currentDir = new File("");
            String formattedDir = currentDir.getAbsolutePath() + "\\" + fileName;
            fileReader = new Scanner(new File(formattedDir));
        } catch (FileNotFoundException e) {
            System.out.println("File not found, proceeding without an inventory file will not work. Please try again. ");
        }
    }

    public ArrayList<Item> readItems(){
        ArrayList<Item> items = new ArrayList<>();
        if (fileReader == null){
            return null;
        }
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            items.add(new Item(data));
        }
        return items;
    }

    public void endReader(){
        if (fileReader != null){
            fileReader.close();
        }
    }


}
