/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
<<<<<<< HEAD
 */


import java.net.*;
import java.io.*;

public class KnockKnockProtocol {
    private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 5;

    private int state = WAITING;

    public String processInput(String theInput)
    {
        String msg = null;
        if (state == WAITING) {
            msg = "Now listening";
            state = SENTKNOCKKNOCK;
        }
        else if (state == SENTKNOCKKNOCK)
        {
            char[] b = theInput.toCharArray();

            String[] param = theInput.split(" ");
            int id = Integer.parseInt(param[0]);
            int size = Integer.parseInt(param[1]);
            int power = Integer.parseInt(param[2]);
            String m = param[3];

            //System.out.println("ID : " + id);

            //split the matrix in order to have a tab
            String[] matrice = m.split(",");

            double[][] real_matrice = convertTab(matrice, size);

            double[][] product = powerTab(real_matrice, size, power);

            //printMatrix(product, size);

            msg = createOutput(product, size);

            //System.out.println(msg);
        }
        return msg;
    }

    static double[][] convertTab(String[] line, int size)
    {

        double[][] real_matrice = new double[size][size];
        int index = 0;

        //transforming the matrix in a two dimensional array
        for(int row = 0; row < size; row++)
        {
            for(int column = 0; column < size; column++)
            {
                real_matrice[row][column] = Double.parseDouble(line[index]);
                index++;
            }
        }
        return real_matrice;

    }

    static double[][] powerTab(double[][] tab, int size, int p)
    {

        double[][] product = new double[size][size];
        double[][] result = new double[size][size];

        for(int power = 0; power < p-1; power++)
        {
            //we put product in result
            if(power>0)
            {
                for(int x = 0; x<size; x++)
                {
                    for(int y = 0; y<size; y++)
                    {
                        result[x][y] = product[x][y];
                        product[x][y] = 0;
                    }
                }
            }

            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        if(power == 0)
                            product[i][j] += tab[i][k] * tab[k][j];
                        else
                            product[i][j] += result[i][k] * tab[k][j];
                    }
                }
            }
        }
        return product;
    }

    static String createOutput(double[][] product, int size)
    {
        String msg = "[";

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(j == size-1 && i == size-1){
                    //End of Matrix
                    msg = msg+product[i][j]+"]";
                }
                else{
                    msg = msg+product[i][j]+",";
                }

            }
        }
        return msg;
    }

    static void printMatrix(double[][] product, int size)
    {
        for(int row = 0; row < size; row++)
        {
            for(int column = 0; column < size; column++)
                System.out.print("["+product[row][column]+"]");
            System.out.println('\n');
        }
    }

}
