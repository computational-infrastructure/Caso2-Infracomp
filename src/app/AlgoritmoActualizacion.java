package app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AlgoritmoActualizacion extends Thread {
	private ConcurrentHashMap<Integer, Pagina> tabla;
	ArrayList<Integer> RAM;
	// Class 0: not referenced, not modified.
	ArrayList<Pagina> c0;
	// Class 1: not referenced, modified.
	ArrayList<Pagina> c1;
	// Class 2: referenced, not modified.
	ArrayList<Pagina> c2;
	// Class 3: referenced, modified.
	ArrayList<Pagina> c3;

	public AlgoritmoActualizacion(ConcurrentHashMap<Integer, Pagina> tabla,  ArrayList<Integer> RAM) {
		this.tabla = tabla;
		this.RAM = RAM;
	}

	private void getBitPageCategories() {
		for (Integer key : tabla.keySet()) {
			Pagina page = tabla.get(key);
			
			// Not referenced
		    if (!page.getR()) {
		    	if (!page.getM()) {
		    		c0.add(page);
		    	} else {
		    		c1.add(page);
		    	}
		    }
		    // Referenced
		    if (page.getR()) {
		    	if (!page.getM()) {
		    		c2.add(page);
		    	} else {
		    		c3.add(page);
		    	}
		    }
		}
	}
	
	private void removePageNRU() {
		ArrayList<Pagina> selectedClass = null;
		if (!c0.isEmpty()) {
			selectedClass = c0;
		}else if (c1.isEmpty()) {
			selectedClass = c1;
		}else if (c2.isEmpty()) {
			selectedClass = c2;
		}else if (c3.isEmpty()) {
			selectedClass = c3;
		}
		
		int index = (int) (Math.random() * selectedClass.size());
		Integer pageNum = selectedClass.get(index).getNumPagina();
		Pagina page = tabla.get(pageNum);
		page.unload();
		RAM.remove(Integer.valueOf(pageNum));
	}

	public void run() {
	    try {
	        while (true) {
	            Thread.sleep(20);
	    		getBitPageCategories();
	            removePageNRU();
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}
