package app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ReinicioPaginas extends Thread {
	private ConcurrentHashMap<Integer, Pagina> tabla;
	private boolean terminado;

	public ReinicioPaginas(ConcurrentHashMap<Integer, Pagina> tabla) {
		this.tabla = tabla;
		terminado = false;
	}

	public void run() {
		while (!terminado) {
			try {
				for (int i : App.RAM) {
					Pagina pag = tabla.get(i);
					if (pag.isLoaded()) {
						pag.reset();
						tabla.replace(i, pag);
					}
				}
				// Tiempo del reloj
				Thread.sleep(20);
			} catch (Exception e) {}
		}
	}
}
