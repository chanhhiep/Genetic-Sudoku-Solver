package com.lilium.sudoku.util;

import java.util.Random;

public class Gen {

    int[] genMatrix ;
    /* public Gen(){
        genMatrix=new int[16];
    }*/
    public Gen(int[] genMatrix) {
        this.genMatrix = genMatrix;
    }

    public int[] getGenMatrix() {
        return genMatrix;
    }

    public void setGenMatrix(int[] genMatrix) {
        this.genMatrix = genMatrix;
    }
    public void generateGen(){
        Random rd = new Random();
        int[] newGen=new int[genMatrix.length];
        int dimension = (int) Math.sqrt(genMatrix.length);
        for(int i=0;i< genMatrix.length;i++){
                 newGen[i]=rd.nextInt(dimension)+1;
        }
        setGenMatrix(newGen);
    }
    public String toString(){
        StringBuilder result= new StringBuilder();
        for(Integer g:genMatrix){
            result.append(g);
        }
        return result.toString();
    }
}
