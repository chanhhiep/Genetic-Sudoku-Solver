package com.lilium.sudoku.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuGenetic {

    public static final int POP_SIZE = 15000;//Population size
    public static final double MUTATION_RATE = 0.6;
    public static final int MAX_ITERATIONS = 300;
    public static List<Gen> population = new ArrayList<Gen>();
    public static int[][] initialGen;
    Random rd = new Random();
    public static void initialPopulation(int[] nGen){
        for(int i=0;i<POP_SIZE;i++) {
            Gen gen = new Gen(nGen);
            gen.generateGen();
            population.add(gen);
        }
        System.out.println(population);
    }
    public Gen execute(){
        for(int i=1;i<=MAX_ITERATIONS;i++) {
            List<Gen> newPopulation = new ArrayList<Gen>();
            for(int j=0 ;j<=POP_SIZE;j++) {
                Gen geneX = getRandomParentSelection();
                Gen geneY = getRandomParentSelection();
                Gen geneChild = crossOver(geneX,geneY);
                if(rd.nextDouble()>MUTATION_RATE) mutate(geneChild);
                if(fitness(geneChild)==0) {
                    return geneChild;
                }
                newPopulation.add(geneChild);
            }
            population = newPopulation;
        }
        return getBestSelection();
    }
    public Gen getBestSelection(){
        Gen min = population.get(0);
        for(Gen g:population){
            if (fitness(g) < fitness(min)) {
                min=g;
            }
        }
        return min;
    }
    /*public Gen getRandomSelection(){
        Gen newGen = population.get(rd.nextInt(POP_SIZE));
        System.out.println(newGen.genMatrix);
        return newGen;
    }*/
    public Gen getRandomParentSelection(){

        int k=3;
        Gen result= population.get(rd.nextInt(POP_SIZE));
        for(int i=1;i<k;i++) {
            Gen n= population.get(rd.nextInt(POP_SIZE));
            if(fitness(n)<fitness(result))result=n;
        }
        return result;
    }
    /*public Gen getRouletteWheelSelection(){
        int max = 0;
        for (Gen g : population)
            if (fitness(g)> max) max = fitness(g);
//        int sum = 0;
//        for (Sudoku sudoku : sudokus)
//            sum += sudoku.getFitnessValue();
//        for (Sudoku sudoku : sudokus)
//            sudoku.setProbability(sudoku.getFitnessValue() / (sum * 1.0));

        int sum = 0;
        for (Gen g : population)
            sum += max - fitness(g);
        /*for (Sudoku sudoku : sudokus)
            sudoku.setProbability((max - sudoku.fitnessValue) / (sum * 1.0));
*/
 /*       double random = Math.random() * sum;
        int i;
        for (i = 0; i < population.size() && random > 0; i++) {
            random -= max -fitness(population.get(i));
        }
        return population.get(i-1);


    }*/
    public static int[][] oneToTwoArray(int[] value){
        int [][] result = new int[(int) Math.sqrt(value.length)][(int) Math.sqrt(value.length)];
        for(int i=0;i< value.length;i++){
            result[i / result.length][ i % result.length]=value[i];
        }
        return result;
    }

    public static int[] TwoToOneArray(int[][] value){
        int[] result = new int[value.length * value.length];
        for(int i=0;i<result.length;i++){
            result[i]=value[i/ value.length][i% value.length];
        }
        return result;
    }
    public static int fitness(Gen gene){
        /*int fitness = 0;
        int[][] newGene = oneToTwoArray(gene.getGenMatrix());
        //9x9
        for (int row = 0; row< newGene.length; row++){
            for (int col = 0; col < newGene.length; col++) {
              //find col duplicate
                int num= newGene[row][col];
                for(int otherCol=col+1;otherCol<newGene.length;otherCol++){
                    if(num==newGene[row][otherCol]){
                        fitness+=10;
                    }
                }
                //find row duplicate
                for(int otherRow=row+1;otherRow<newGene.length;otherRow++){
                    if(num==newGene[otherRow][col]){
                        fitness+=10;
                    }
                }
                if(newGene[col][row]==0){
                    fitness+=1000;
                }
            }
        }*/
        /*//3x3
        int blockSize = (int) Math.sqrt(newGene.length);
        for (int i = 0; i < newGene.length; i += blockSize) {
            for (int j = 0; j < newGene.length; j += blockSize) {
                boolean[] blockFlag = new boolean[newGene.length + 1];

                for (int k = 0; k < blockSize; k++) {
                    for (int l = 0; l < blockSize; l++) {
                        if (blockFlag[newGene[i + k][j + l]])
                            fitness++;
                        blockFlag[newGene[i + k][j + l]] = true;
                    }
                }
            }
        }*/
        //3x3
        /*int blockSize = (int) Math.sqrt(newGene.length);
        for(int col = 0;col< newGene.length;col += blockSize){
            for(int row =0;row< newGene.length;row += blockSize){
                //block
                for(int blockCol=0;blockCol<blockSize;blockCol++){
                    for(int blockRow=0;blockRow<blockSize;blockRow++){
                        for (int otherBlockCol=blockCol+1;otherBlockCol< blockSize;otherBlockCol++){
                            for(int otherBlockRow=blockRow+1;otherBlockRow<blockSize;otherBlockRow++){
                                if(newGene[col+blockCol][row+blockRow]==newGene[col+otherBlockCol][row+otherBlockRow]){
                                    fitness+=10;
                                }
                            }
                        }
                    }
                }
            }
        }
        return fitness;
        */
        int fitness = 0;
        int[][] newGene = oneToTwoArray(gene.genMatrix);
        int[][] newInitialGene = initialGen;
        for (int i = 0; i < newGene.length; i++) {
            boolean[] rowFlag = new boolean[newGene.length + 1];
            boolean[] colFlag = new boolean[newGene.length + 1];
            for (int j = 0; j < newGene.length; j++) {
                if (rowFlag[newGene[i][j]])
                    fitness++;
                if (colFlag[newGene[j][i]])
                    fitness++;
                if ((newInitialGene[i][j] != 0 && newInitialGene[i][j] != newGene[i][j]) || newGene[i][j] == 0)
                    fitness += 1000;
                rowFlag[newGene[i][j]] = true;
                colFlag[newGene[j][i]] = true;
            }
        }
        int blockSize = (int) Math.sqrt(newGene.length);
        for (int i = 0; i < newGene.length; i += blockSize) {
            for (int j = 0; j < newGene.length; j += blockSize) {
                boolean[] blockFlag = new boolean[newGene.length + 1];

                for (int k = 0; k < blockSize; k++) {
                    for (int l = 0; l < blockSize; l++) {
                        if (blockFlag[newGene[i + k][j + l]])
                            fitness++;
                        blockFlag[newGene[i + k][j + l]] = true;
                    }
                }
            }
        }

        return fitness;
    }
    public void mutate(Gen gene){
        int[] newGene = gene.getGenMatrix();
        int demension = (int) Math.sqrt(newGene.length);
        for(int i=0;i<rd.nextInt(newGene.length);i++){
            newGene[rd.nextInt(newGene.length)]=rd.nextInt(demension)+1;
        }
        gene.setGenMatrix(newGene);
    }
    public Gen crossOver(Gen genX,Gen genY){
        int[] matrixX= genX.genMatrix;
        int[] matrixY = genY.genMatrix;
        int[] result=new int[matrixX.length];
        for(int i=0;i<matrixX.length;i++){
            if(i<rd.nextInt(matrixX.length)){
                result[i] = matrixX[i];
            }else {
                result[i] =matrixY[i];
            }
        }
        return new Gen(result);
    }

    public void setInitialGen(int[][] initialGen) {
        SudokuGenetic.initialGen = initialGen;
    }
}
