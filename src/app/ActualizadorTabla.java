package app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ActualizadorTabla extends Thread {
	private ArrayList<String> referencias;
	private ConcurrentHashMap<Integer, Pagina> tabla;
	private int cantidadReferencias;

	public ActualizadorTabla(ArrayList<String> referencias, ConcurrentHashMap<Integer, Pagina> tabla,
			int cantidadReferencias) {
		this.referencias = referencias;
		this.tabla = tabla;
		this.cantidadReferencias = cantidadReferencias;
	}

	public void run() {
		for (int i = 0; i < cantidadReferencias; i++) {
			String[] referencia = referencias.get(i).split(",");
			Integer numPagina = Integer.parseInt(referencia[0]);
			System.out.println(numPagina);
			String operacion = referencia[1];
			System.out.println(operacion);
			try {
				actualizarTabla(numPagina, operacion);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void actualizarTabla(int numPagina, String operacion) throws InterruptedException {
		Pagina page = tabla.get(numPagina);
		// TODO: ask why first reference is not page error.
		if (page.isLoaded() == false && App.darNumPagCargadas() < App.darNumPagRAM()) {
			page.load();
			App.falloGenerado();
			App.cargarPagina();
		} else if (page.isLoaded() == false) {
			App.falloGenerado();
			// TODO: Algoritmo de reemplazo add always when a reference occurs
		}
		if (operacion.equals("r")) {
			page.reference();
		} else if (operacion.equals("m")) {
			page.modify();
		}
		tabla.replace(numPagina, page);
		Thread.sleep(1);
	}
}
