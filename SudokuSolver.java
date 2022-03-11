package com.lilium.sudoku;

import com.lilium.sudoku.mnist.evaluation.EvalUtil;
import com.lilium.sudoku.util.Gen;
import com.lilium.sudoku.util.SudokuGenetic;
import com.lilium.sudoku.util.Utils;
import nu.pattern.OpenCV;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.opencv.core.Mat;

public class SudokuSolver {
    private static final String IMAGE = "sudoku.jpg";

    public static void main(final String args[]) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OpenCV.loadLocally();
        // Load trained network
        final MultiLayerNetwork trainedNetwork = EvalUtil.loadModel();

        // Load and process image
        final Mat processedImage = Utils.preProcessImage(Utils.loadImage(IMAGE));
        // Load debugging image (same as one that is being processed)
        final Mat debuggingImage = Utils.loadImage(IMAGE);

        // Mark outer rectangle and corners (we do this just for debugging, and since it is cool :P)
        Utils.markOuterRectangleAndCorners(processedImage, debuggingImage);

        // Remove all lines from processed image
        Utils.removeLines(processedImage);

        // Get sudoku matrix with estimated values
        final int[][] sudokuMatrix = Utils.getSudokuMatrix(processedImage, trainedNetwork);

        // Solve and print out solution
        SudokuGenetic sudokuGenetic = new SudokuGenetic();
        sudokuGenetic.setInitialGen(sudokuMatrix);
        int [] gen = SudokuGenetic.TwoToOneArray(sudokuMatrix);
        SudokuGenetic.initialPopulation(gen);
            // Print solved matrix to the console
            Gen rs = sudokuGenetic.execute();
            int[][] result= SudokuGenetic.oneToTwoArray(rs.getGenMatrix());
            System.out.println("fitness score: "+SudokuGenetic.fitness(rs));
            Utils.printOutMatrix(result);

            // Print solved matrix to the image
            Utils.printSolutionToImage(processedImage, debuggingImage, result);

           // System.out.println("#### NOT ABLE TO SOLVE ####");


        // Save processed and debugging images
        Utils.saveImage(processedImage, "processed-2.jpg");
        Utils.saveImage(debuggingImage, "debugging.jpg");
    }
}
