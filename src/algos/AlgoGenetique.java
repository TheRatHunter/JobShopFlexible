package algos;

import donneesDuProbleme.Solution;

import java.util.ArrayList;
import java.util.Random;

public class AlgoGenetique {

    private int nbIterations;
    private int poolSize;

    public AlgoGenetique() {
        this.nbIterations = 0;
        this.poolSize = 1;
    }


    //Renvoie un ArrayList trié de solutions après un certain nombre de générations
    public ArrayList<Solution> algoGenetiqueTournoiMutations(ArrayList<Solution> poolInitial, Integer nbGenerations) {
        ArrayList<Solution> resultat = poolInitial; //poolInitial est sensé arriver trié
        poolSize = poolInitial.size();

        // Génération d'enfants par mutations (les 50 meilleurs se reproduisent)
        ArrayList<Solution> enfants = new ArrayList<>();

        Random rng = new Random();
        rng.setSeed(System.nanoTime());
        Integer random = Math.abs(rng.nextInt());
        Integer indice = random%2;

        for (int z = 0; z<(poolSize/2); z++) {
            Solution temp = poolInitial.get(z);
            if (indice == 0){
                temp = CalculMutations.mutationChangementSurMASimple(poolInitial.get(z));
            } else {
                temp = CalculMutations.mutationSwapSurOSSimple(poolInitial.get(z));
            }
            enfants.add(temp);
            random = Math.abs(rng.nextInt());
            indice = random%2;
        }

        //Tournoi pour sélectionner poolSize individus
        ArrayList<Solution> tout = new ArrayList<>();
        tout.addAll(poolInitial);
        tout.addAll(enfants);

        //Sélectionner à chaque fois 2, choisir le meilleur tant qu'on en a pas 100
        ArrayList<Solution> nouvelleGénération = new ArrayList<>();
        Integer indice1;
        Integer indice2;
        while (nouvelleGénération.size()!=poolSize) {

            //Récupération de deux indices au hasard
            random = Math.abs(rng.nextInt());
            indice1 = random%(tout.size());
            indice2 = indice1;
            while (indice2==indice1) {
                random = Math.abs(rng.nextInt());
                indice2 = random%(tout.size());
            }

            //On récupère le meilleur et on le met dans nouvelleGénération
            if (tout.get(indice1).getCout()<tout.get(indice2).getCout()) {
                nouvelleGénération.add(tout.get(indice1));
                tout.remove(indice1);
            } else {
                nouvelleGénération.add(tout.get(indice2));
                tout.remove(indice2);
            }

        }

        nbIterations++;

        nouvelleGénération.sort(Solution::compareTo);

        System.out.println("[Génération "+nbIterations+"] La meilleure solution connue à ce jour a un coût de: "
                +nouvelleGénération.get(0).getCout());

        if (nbIterations<nbGenerations) {
            resultat = algoGenetiqueTournoiMutations(nouvelleGénération, nbGenerations);
        }
        else {
            resultat = nouvelleGénération;
            nbIterations = 0;
        }

        return resultat;
    }
}