package app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AlgoritmoActualizacion extends Thread {
	private ConcurrentHashMap<Integer, Pagina> tabla;
	// Class 0: not referenced, not modified.
	ArrayList<Pagina> c0 = new ArrayList<Pagina>();
	// Class 1: not referenced, modified.
	ArrayList<Pagina> c1 = new ArrayList<Pagina>();
	// Class 2: referenced, not modified.
	ArrayList<Pagina> c2 = new ArrayList<Pagina>();
	// Class 3: referenced, modified.
	ArrayList<Pagina> c3 = new ArrayList<Pagina>();
	private int pageNum;

	public AlgoritmoActualizacion(ConcurrentHashMap<Integer, Pagina> tabla, int pageNum) {
		this.tabla = tabla;
		this.pageNum = pageNum;
	}

	private void getBitPageCategories() {
		for (int i : App.RAM) {
			Pagina page = tabla.get(i);

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

	private synchronized void removePageNRU() {
		ArrayList<Pagina> selectedClass = null;
		if (!c0.isEmpty()) {
			selectedClass = c0;
		} else if (!c1.isEmpty()) {
			selectedClass = c1;
		} else if (!c2.isEmpty()) {
			selectedClass = c2;
		} else if (!c3.isEmpty()) {
			selectedClass = c3;
		}

		int index = (int) (Math.random() * selectedClass.size());
		Integer pageNum = selectedClass.get(index).getNumPagina();
		Pagina page = tabla.get(pageNum);
		page.unload();
		tabla.replace(pageNum, page);
		App.RAM.remove(Integer.valueOf(pageNum));
	}

	private void addPage() {
		Pagina page = tabla.get(this.pageNum);
		page.load();
		tabla.replace(this.pageNum, page);
		App.RAM.add(this.pageNum);
	}

	@Override
	public void run() {
		getBitPageCategories();
		removePageNRU();
		addPage();
	}
}
