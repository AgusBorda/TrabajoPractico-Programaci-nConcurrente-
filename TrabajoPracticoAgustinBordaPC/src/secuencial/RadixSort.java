package secuencial;
import java.util.Arrays;
import java.util.Random;

public class RadixSort {
	
    // Método principal para realizar el Radix Sort  -  METODO SECUENCIAL
    public static void radixSort(int[] arr) {
        // Encuentra el número máximo para saber el número de dígitos
        int max = getMax(arr);
        
        // Realiza el counting sort para cada dígito, empezando por el menos significativo
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(arr, exp);
        }
    }

    // Método para obtener el número máximo en el arreglo
    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    // Método para realizar el counting sort según el dígito representado por exp
    private static void countingSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n]; // Array para almacenar el resultado ordenado
        int[] count = new int[10]; // Array para almacenar el conteo de los dígitos (base 10)

        // Inicializa el array de conteo a 0
        Arrays.fill(count, 0);

        // Almacena el conteo de ocurrencias de los dígitos
        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
        }

        // Modifica el array de conteo para que contenga la posición de cada dígito en output
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Construye el array output
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        // Copia el array output al array original arr
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }

    // Método principal para probar el algoritmo
    public static void main(String[] args) {
    	int size = 1000000000; // Tamaño del arreglo
        int[] arr = new int[size];

        // Llenar el arreglo con números aleatorios
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10000); // Números aleatorios entre 0 y 9999
        }
        
        //System.out.println("Arreglo original: " + Arrays.toString(arr));
        long startTime = System.nanoTime();
        radixSort(arr);
        long endTime = System.nanoTime();
        
        //System.out.println("Arreglo ordenado: " + Arrays.toString(arr));
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) / 1e6 + " ms");
    }
}
