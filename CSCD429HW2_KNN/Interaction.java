import java.io.*;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;

public class Interaction {
    String GeneID1, GeneID2, Type, Expression_Corr;
    public Interaction(String GeneID1, String GeneID2, String Type, String Expression_Corr) {
        this.GeneID1 = GeneID1;
        this.GeneID2 = GeneID2;
        this.Type = Type;
        this.Expression_Corr = Expression_Corr;
    }
    public static ArrayList<Interaction> constructData(File file) {
        try {
            ArrayList<Interaction> interactionList = new ArrayList<Interaction>();
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");

                String GeneID1 = data[0];
                String GeneID2 = data[1];
                String Type = data[2];
                String Expression_Corr = data[3];

                Interaction interaction = new Interaction(GeneID1, GeneID2, Type, Expression_Corr);

                interactionList.add(interaction);
            }
            sc.close();
            interactionList.remove(0);
            return interactionList;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
