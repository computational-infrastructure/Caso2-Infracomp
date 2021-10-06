package app;
import java.util.ArrayList;
import java.util.Hashtable;

public class ActualizadorTabla extends Thread
{
    private ArrayList<String> referencias;
    private Hashtable<Integer, Pagina> tabla;
    private int cantidadReferencias;

    public ActualizadorTabla(ArrayList<String> referencias, Hashtable<Integer, Pagina> tabla, int cantidadReferencias)
    {
        this.referencias = referencias;
        this.tabla = tabla;
        this.cantidadReferencias = cantidadReferencias;
    }

    public void run()
    {
        for(int i=0; i<cantidadReferencias;i++)
        {
            Integer numPagina = Integer.parseInt(Character.toString(referencias.get(i).charAt(0)));
            String operacion = Character.toString(referencias.get(i).charAt(2));
            try 
            {
                actualizarTabla(numPagina, operacion);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        
    }

    private synchronized void actualizarTabla(int numPagina, String operacion) throws InterruptedException
    {
        Pagina page = tabla.get(numPagina);
        if (page.getPresent() == false && app.darNumPagCargadas() < app.darNumPagRAM())
        {
            page.load();
            app.cargarPagina();
        }
        else if(page.getPresent() == false)
        {
            app.falloGenerado();
            //Algoritmo de reemplazo
        }
        if(operacion.equals("r"))
        {
            page.reference();
        }
        else if (operacion.equals("m"))
        {
            page.modify();
        }
        tabla.replace(numPagina, page);
        Thread.sleep(1);
    }
}
