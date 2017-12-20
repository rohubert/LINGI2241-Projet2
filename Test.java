public class Test
{

    public static void main(String[] args)
    {
        String a = "2 3 [2.0,3.0,4.0,5.0]";
        char[] b = a.toCharArray();

        String n = new String();
        int i = 0;
        while(b[i]!=(' ')){
            n += b[i];

            i++;
        }

        i++;

        String p = new String();
        while(b[i]!=(' ')){
            p += b[i];

            i++;
        }

        i++;
        i++;
        String m = new String();
        while(b[i]!=']')
        {
            m+=b[i];
            i++;
        }
        String[] matrice = m.split(",");
        int size = Integer.parseInt(n);
        double[][] real_matrice = new double[size][size];
        int index = 0;
        for(int row = 0; row < size; row++)
        {
            //real_matrice[row] = new double[size];
            for(int column = 0; column < size; column++)
            {

                real_matrice[row][column] = Double.parseDouble(matrice[index]);
                index++;
            }
        }
        double[][] product = new double[size][size];
        double[][] result = new double[size][size];

        for(int power = 0; power < Integer.parseInt(p)-1; power++)
        {
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

            for(i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        if(power == 0)
                        {
                            product[i][j] += real_matrice[i][k] * real_matrice[k][j];

                        }
                        else{
                            product[i][j] += result[i][k] * real_matrice[k][j];
                        }

                    }
                }
            }
        }
        for(int row = 0; row < size; row++)
        {
            for(int column = 0; column < size; column++)
            {
                System.out.print("["+product[row][column]+"]");
            }
            System.out.println('\n');
        }

    }

}
