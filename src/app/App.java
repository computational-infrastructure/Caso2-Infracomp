package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class App {
	static String pathToProperties = "";
	static boolean carga = false;
	static int numMarcosDePaginaRAM;
	static int numPaginasProceso;
	static int numReferencias;
	static int numPaginasCargadas;
	static int numFallos;
	static ConcurrentHashMap<Integer, Pagina> tabla;
	static ArrayList<Integer> RAM = new ArrayList<>();
	static ArrayList<String> secuenciaReferencias = new ArrayList<>();

	public static void cargarDatos() {
		Scanner sc = new Scanner(System.in);
		while (!carga) {
			// Carga de datos del archivo Properties
			try {
				if (pathToProperties == "") {
					System.out.print(
							"Change input file path or just press enter for using the default path 'referencias/referencias8_128_75.txt' \n");
					pathToProperties = sc.nextLine();
					if (pathToProperties == "") {
						pathToProperties = "referencias/referencias8_128_75.txt";
					}
				}
				File f = new File(pathToProperties);

				Scanner lector = new Scanner(f);
				numMarcosDePaginaRAM = Integer.parseInt(lector.nextLine());
				numPaginasProceso = Integer.parseInt(lector.nextLine());
				numReferencias = Integer.parseInt(lector.nextLine());
				while (lector.hasNextLine()) {
					secuenciaReferencias.add(lector.nextLine());
				}
				System.out.println("Configuración cargada");
				carga = true;
				lector.close();
				sc.close();
				numPaginasCargadas = 0;
			} catch (FileNotFoundException ex) {
				System.out.println("No se encontró el archivo.");
				System.out.println("Por favor ingresa la ruta del archivo de propiedades:");
				pathToProperties = sc.nextLine();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		cargarDatos();
		tabla = new ConcurrentHashMap<>(numPaginasProceso);

		for (int i = 0; i < numPaginasProceso; i++) {
			Pagina pag = new Pagina(i);
			tabla.put(i, pag);
		}

		CyclicBarrier barrera = new CyclicBarrier(2);

		new ActualizadorTabla(secuenciaReferencias, tabla, numReferencias, barrera).start();

		try {
			barrera.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		System.out.println("El número de fallas de página generadas es: " + numFallos);
	}

	public static void cargarPagina() {
		numPaginasCargadas += 1;
	}

	public static void falloGenerado() {
		numFallos += 1;
	}

	public static int darNumPagCargadas() {
		return numPaginasCargadas;
	}

	public static int darNumPagRAM() {
		return numMarcosDePaginaRAM;
	}
}
