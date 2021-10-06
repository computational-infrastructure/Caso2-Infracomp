package app;
import java.util.ArrayList;
import java.util.Hashtable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class app
{
    static String pathToProperties = "config.txt";
    static boolean carga = false;
    static int numMarcosDePaginaRAM;
    static int numPaginasProceso;
    static int numReferencias;
    static Hashtable<Integer, Pagina> tabla;
    static ArrayList<String> secuenciaReferencias = new ArrayList<>();

    public static void cargarDatos() {
		Scanner sc = new Scanner(System.in);
		while (!carga) {
			// Carga de datos del archivo Properties
			try {
				File f = new File(pathToProperties);
                Scanner lector = new Scanner(f);
                numMarcosDePaginaRAM = Integer.parseInt(lector.nextLine());
                numPaginasProceso = Integer.parseInt(lector.nextLine());
                numReferencias = Integer.parseInt(lector.nextLine());
                while (lector.hasNextLine())
                {
                    secuenciaReferencias.add(lector.nextLine());
                }
				System.out.println("Configuración cargada");
				carga = true;
                lector.close();
				sc.close();
			} catch (FileNotFoundException ex) {
				System.out.println("No se encontró el archivo.");
				System.out.println("Por favor ingresa la ruta del archivo de propiedades:");
				pathToProperties = sc.nextLine();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    public static void main(String[] args)
    {
        cargarDatos();
        tabla = new Hashtable<>(numMarcosDePaginaRAM);

        for (int i = 0; i<numMarcosDePaginaRAM; i++)
        {
            Pagina pag = new Pagina(i);
            pag.load();
            tabla.put(i,pag);
        }
        System.out.println(tabla);
    }

}
