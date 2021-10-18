package app;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class ActualizadorTabla extends Thread {
	private ArrayList<String> referencias;
	private ConcurrentHashMap<Integer, Pagina> tabla;
	private int cantidadReferencias;
	private CyclicBarrier barrera;

	public ActualizadorTabla(ArrayList<String> referencias, ConcurrentHashMap<Integer, Pagina> tabla,
			int cantidadReferencias, CyclicBarrier barrera) {
		this.referencias = referencias;
		this.tabla = tabla;
		this.cantidadReferencias = cantidadReferencias;
		this.barrera = barrera;
	}

	@Override
	public void run() {
		for (int i = 0; i < cantidadReferencias; i++) {
			String[] referencia = referencias.get(i).split(",");
			Integer numPagina = Integer.parseInt(referencia[0]);
			String operacion = referencia[1];
			try {
				actualizarTabla(numPagina, operacion);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        ReinicioPaginas.terminar();
		try {
			barrera.await();
		} catch (InterruptedException | BrokenBarrierException e) 
        {
			e.printStackTrace();
		}
	}

	private synchronized void actualizarTabla(int numPagina, String operacion) throws InterruptedException {
		Pagina page = tabla.get(numPagina);
		if (page.isLoaded() == false && App.RAM.size() < App.numMarcosDePaginaRAM) {
			page.load();
			App.RAM.add(numPagina);
			App.falloGenerado();
			App.cargarPagina();
		} else if (page.isLoaded() == false) {
			App.falloGenerado();
			new AlgoritmoActualizacion(tabla, numPagina).start();
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
