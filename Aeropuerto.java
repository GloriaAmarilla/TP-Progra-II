package trabPracFinalPII;
public class Aeropuerto {

    private String nombre;

    private String pais;

    private String provincia;

    private String direccion;

    private char interONacional;

    private double totalRecaudado;
    
    public Aeropuerto() {
    }

    public Aeropuerto(String nombre, String pais, String provincia, String direccion) {
        if (nombre.length()!=0)
        	this.nombre = nombre;
        else
        	throw new RuntimeException("Nombre invalido.");
       
        if (pais.length()!=0)
        	this.pais = pais;
        else
        	throw new RuntimeException("Pais invalido.");
       
        if (provincia.length()!=0)
        	this.provincia = provincia;
        else
        	throw new RuntimeException("Provincia invalida.");
       
        if (direccion.length()!=0)
        	this.direccion = direccion;
        else
        	throw new RuntimeException("Direccion invalida.");
       
        String p = pais.toLowerCase();
        if (p.equals("argentina"))
        	this.interONacional= 'n';
        else
        	this.interONacional='i';
    }

    public String consultarNombre() {
        return nombre;
    }

    public char consultarNacionalOInternacional() {
        return interONacional;
    }

    public double consultarRecaudacion() {
        return totalRecaudado;
    }

    public void incrementarRecaudacion(double precioVenta) {
        totalRecaudado += precioVenta;
    }
    
    @Override
    public String toString() {
    	return nombre +"\n"+ pais +" - "+ provincia +"\nDireccion: "+ direccion;
    }

}