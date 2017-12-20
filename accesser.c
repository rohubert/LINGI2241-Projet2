#include <stdio.h>
#include <stdlib.h>
int main()
{

    int size = 30;

    // printf("Enter the size of the matrix matrix\n");
    // scanf("%d", &size);
    int first[size][size];
    int second[size][size];
    int result[size][size];

    for(int i = 0; i < size; i++)
    {
        for(int j = 0; j < size; j++)
        {
            first[i][j] = rand();
            second[i][j] = 2+i+j;
            result[i][j] = 0;
        }
    }

    for(int i = 0; i < size; i++)
    {
        for(int j = 0; j < size; j++)
        {
            result[i][j] = first[i][j] * second[i][j];
            printf("%d\n", result[i][j]);
        }
    }

    return 0;
}
