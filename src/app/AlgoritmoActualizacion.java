package app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AlgoritmoActualizacion extends Thread {
	private ConcurrentHashMap<Integer, Pagina> tabla;
	private static ArrayList<Integer> pagesNRU;

	public AlgoritmoActualizacion(ConcurrentHashMap<Integer, Pagina> tabla, Integer pageReferenced) {
		this.tabla = tabla;
		AlgoritmoActualizacion.pagesNRU.add(pageReferenced);
	}

	public void run() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int index = (int) (Math.random() * AlgoritmoActualizacion.pagesNRU.size());
		// Random page from the group of not referenced in 20ms.
		Integer pageNum = AlgoritmoActualizacion.pagesNRU.get(index);

		Pagina page = tabla.get(pageNum);
		page.resetR();
	}
}
