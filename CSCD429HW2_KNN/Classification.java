//Classification Method: KNN
/*Things I'll Need:
 * Set of stored records
 * Distance metric to compute distance between records
 * The value of k
 */

/*To classify an unknown record:
 * Compute distance to other training records
 * Identify k nearest neighbors
 * Use class labels of nearest neighbors to determine the class label of unknown record (e.g., by taking majority vote)
 */

/*Ideas on how I should handle missing values:
 * Ignore the tuples with them (seems impossible since a majority of tuples have missing values of some sort)
 * Ignore the attributes with the missing values (still seems troublesome since complex and class, the 2 highest weighted attributes, are missing the most)
 * Fill in the missing values? (this is practically impossible with a dataset of this size)
 * Fill it automatically with a...
 * -Global Constant (unrealistic)
 * -Attribute mean, median, or mode (more possible than a constant, but still unrealistic)
 * -Attribute mean, median, or mode BUT for the same class (a little more realistic, but still infeasible)
 * -Imputation, AKA the most probable value (this will probably be my method of approach)
 */

/*Goals:
* Figure out which X I will use, so that weights can be assigned
* Calculate the Euclidean distance of these weights
*
 */

import java.io.*;
import java.util.*;

public class Classification {
    //Weight Constants
    static final int WCOMPLEX = 1000;
    static final int WCLASS = 100;
    static final int WMOTIF = 10;
    static final int WINTERACTION = 1;

    //KNN K-Value
    static final int k = 5;

    public static class GeneComparator implements Comparator<Gene> {

        @Override
        public int compare(Gene o1, Gene o2) {
            return CharSequence.compare(o1.GeneID, o2.GeneID);
        }
    }

    public static void main(String[] args) {
        File genes_relation_data = new File("Genes_relation.data");
        File genes_relation_test = new File("Genes_relation.test");
        File interactions_relation_data = new File("Interactions_relation.data");

        File keys = new File("keys.txt");

        ArrayList<Gene> geneList = Gene.constructData(genes_relation_data);
        ArrayList<Gene> geneTestList = Gene.constructData(genes_relation_test);
        ArrayList<Interaction> interactionList = Interaction.constructData(interactions_relation_data);

        assignAttributeWeights(geneList, geneTestList);
        assignInteractionWeight(geneTestList, interactionList);

        sortMap(geneTestList);

        guessLocalization(geneTestList, geneList);

        output(geneTestList);
    }

    //Distance Weighting
    public static void assignAttributeWeights(ArrayList<Gene> geneList, ArrayList<Gene> geneTestList) {
        for (Gene X : geneTestList) {
            for (Gene Y : geneList) {
                X.weights.put(Y.GeneID,
                        (X.Motif.equals(Y.Motif) ? WMOTIF: 0) +
                        (X.Class.equals(Y.Class) ? WCLASS: 0) +
                        (X.Complex.equals(Y.Complex) ? WCOMPLEX: 0));
            }
        }
    }

    public static void assignInteractionWeight(ArrayList<Gene> geneList, ArrayList<Interaction> interactionList) {
        for (Interaction interaction : interactionList) {
            for (Gene gene : geneList) {
                if ( (interaction.GeneID1.equals(gene.GeneID)) ) {
                    if (gene.weights.containsKey(interaction.GeneID2)) {
                        gene.weights.put(interaction.GeneID2, gene.weights.get(interaction.GeneID2) + 1);
                    } else {
                        gene.weights.put(interaction.GeneID2, 1);
                    }
                    break;
                }
                if ( (interaction.GeneID2.equals(gene.GeneID)) ) {
                    if (gene.weights.containsKey(interaction.GeneID1)) {
                        gene.weights.put(interaction.GeneID1, gene.weights.get(interaction.GeneID1) + 1);
                    } else {
                        gene.weights.put(interaction.GeneID1, 1);
                    }
                    break;
                }
            }
        }
    }

    public static void sortMap(ArrayList<Gene> geneList) {
        for (Gene gene : geneList) {
            List<Map.Entry<String, Integer>> list = new LinkedList<>(gene.weights.entrySet());

            Map<String, Integer> hm = new LinkedHashMap<>();
            while (!list.isEmpty()) {
                int highestWeight = -1;
                int highestIndex = -1;
                String highestID = " ";
                int i = 0;
                for (Map.Entry<String, Integer> entry : list) {
                    if (entry.getValue() > highestWeight) {
                        highestWeight = entry.getValue();
                        highestIndex = i;
                        highestID = entry.getKey();
                    }
                    i++;
                }
                hm.put(highestID, highestWeight);
                list.remove(highestIndex);
            }
            gene.weights = hm;
        }
    }

    public static void guessLocalization(ArrayList<Gene> geneTestList, ArrayList<Gene> geneList) {

        for (Gene gene : geneTestList) {
            String[] arr = new String[k];
            for (int i = 0; i < arr.length; i++) {
                String targetID = gene.weights.entrySet().iterator().next().getKey();
                for (Gene geene : geneList) {
                    if (geene.GeneID.equals(targetID)) {
                        arr[i] = geene.Localization;
                    }
                }
            }
            //TODO: Calculate Mode
            gene.Localization = (arr[0]);
        }
    }

    public static void output(ArrayList<Gene> geneTestList) {
        try {
            FileWriter writer = new FileWriter("output.txt");
            writer.write("GeneID,Localization\n");
            geneTestList.sort(new GeneComparator());
            String temp = "";
            for (Gene gene : geneTestList) {
                String temp2 = (gene.GeneID + "," + gene.Localization + "\n");
                if (!temp2.equals(temp)) {
                    writer.write(temp2);
                    temp = temp2;
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printGeneList(ArrayList<Gene> list) {
        for (Gene str : list) {
            System.out.println(str.GeneID + " " + str.Essential + " " + str.Class + " " + str.Complex + " " +
                    str.Phenotype + " " + str.Chromosome + " " + str.Localization + " " + str.weights +"\n");
        }
    }

    public static void printInteractionsList(ArrayList<Interaction> list) {
        for (Interaction str : list) {
            System.out.println(str.Type);
        }
    }
}

