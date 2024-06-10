package concurrente; 
import java.util.Arrays;
import java.util.Random;

public class RadixSortConHilos extends Thread {
	//METODO RADIX SORT CONCURRENTE
    private static int[] vector;
    private int inicio;
    private int fin;
    private int exp;
    private static int[] output;
    private static int[] count = new int[10];
    private int[] localCount = new int[10];

    public RadixSortConHilos(int inicio, int fin, int exp) {
        this.inicio = inicio;
        this.fin = fin;
        this.exp = exp;
    }

    @Override
    public void run() {
        // Inicializar el conteo local
        Arrays.fill(localCount, 0);

        // Contar la frecuencia de los dígitos
        for (int i = inicio; i < fin; i++) {
            localCount[(vector[i] / exp) % 10]++;
        }

        // Sincronizar y combinar los resultados
        synchronized (count) {
            for (int i = 0; i < 10; i++) {
                count[i] += localCount[i];
            }
        }
    }

    public static void radixSort(int[] arr) {
        vector = arr; // Asignar el arreglo a la variable estática
        int max = getMax(arr);
        output = new int[arr.length];

        for (int exp = 1; max / exp > 0; exp *= 10) {
            // Inicializar el conteo global
            Arrays.fill(count, 0);

            // Crear hilos
            int numHilos = 4; // Número de hilos
            RadixSortConHilos[] hilos = new RadixSortConHilos[numHilos];
            int chunkSize = arr.length / numHilos;

            for (int i = 0; i < numHilos; i++) {
                int start = i * chunkSize;
                int end = (i == numHilos - 1) ? arr.length : (i + 1) * chunkSize;
                hilos[i] = new RadixSortConHilos(start, end, exp);
                hilos[i].start();
            }

            // Esperar a que todos los hilos terminen
            try {
                for (RadixSortConHilos hilo : hilos) {
                    hilo.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Modificar el array de conteo para que contenga las posiciones finales
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // Construir el array de salida
            for (int i = arr.length - 1; i >= 0; i--) {
                int digit = (arr[i] / exp) % 10;
                output[--count[digit]] = arr[i];
            }

            // Copiar el array de salida al array original
            System.arraycopy(output, 0, arr, 0, arr.length);
        }
    }

    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int size = 100000000; // Tamaño del arreglo
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
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) / 1e6  + " ms");
    }
}
