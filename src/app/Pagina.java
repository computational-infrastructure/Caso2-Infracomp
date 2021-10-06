package app;

public class Pagina 
{
    private int numPagina;
    private boolean present;
    private boolean M;
    private boolean R;

    public Pagina(int numPagina)
    {
        this.numPagina = numPagina;
        this.M = false;
        this.R = false;
        this.present = false;
    }

    public int getNumPagina()
    {
        return this.numPagina;
    }

    public boolean getM()
    {
        return this.M;
    }

    public boolean getR()
    {
        return this.R;
    }

    public boolean getPresent()
    {
        return this.present;
    }

    public synchronized void reference()
    {
        this.R = true;
    }

    public synchronized void modify()
    {
        this.R = true;
        this.M = true;
    }

    public synchronized void reset()
    {
        this.R = false;
        this.M = false;
    }

    public synchronized void load()
    {
        this.present = true;
        this.R = false;
        this.M = false;
    }

    public synchronized void unload()
    {
        this.present = false;
        this.R = false;
        this.M = false;
    }
}


