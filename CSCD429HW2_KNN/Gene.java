import java.io.*;
import java.util.*;

public class Gene {
    String GeneID, Essential, Class, Complex, Phenotype, Motif, Chromosome, Function, Localization;
    boolean linked;
    Map<String, Integer> weights;

    public Gene(String GeneID, String Essential, String Class, String Complex, String Phenotype, String Motif, String Chromosome, String Function, String Localization) {
        this.GeneID = GeneID;
        this.Essential = Essential;
        this.Class = Class;
        this.Complex = Complex;
        this.Phenotype = Phenotype;
        this.Motif = Motif;
        this.Chromosome = Chromosome;
        this.Function = Function;
        this.Localization = Localization;
        this.linked = false;
        this.weights = new LinkedHashMap<>();
    }

    public static ArrayList<Gene> constructData(File file) {
        try {
            ArrayList<String> geneIDs = new ArrayList<String>();
            ArrayList<Gene> geneList = new ArrayList<Gene>();
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");

                String GeneID = data[0];
                String Essential = data[1];
                String Class = data[2];
                String Complex = data[3];
                String Phenotype = data[4];
                String Motif = data[5];
                String Chromosome = data[6];
                String Function = data[7];
                String Localization = data[data.length - 1];

                Gene gene = new Gene(GeneID, Essential, Class, Complex, Phenotype, Motif, Chromosome, Function, Localization);
                geneList.add(gene);

            }

            sc.close();
            geneList.remove(0);
            return geneList;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

