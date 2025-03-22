package trabPracFinalPII;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Vuelo {

    private String codigo;

    private String origen;

    private String destino;

    private String fecha;

    private int totalTripulantes;

    private HashMap<Integer,Integer> asientosConPasajeros;

    //private double impuento;
    
    public Vuelo() {
    }

    public Vuelo(String codigo, String origen, String destino, String fecha, int tripulantes) {
        if (codigoValido(codigo))
        	this.codigo= codigo;
        else
        	throw new RuntimeException("Codigo invalido.");
        
        if (!origen.isEmpty())
        	this.origen= origen;
        else
        	throw new RuntimeException("Origen invalido.");
        
        if (!destino.isEmpty())
        	this.destino= destino;
        else
        	throw new RuntimeException("Destino invalido.");
 
        if (esValidaFecha(fecha))
        	this.fecha= fecha;
        else
        	throw new RuntimeException("Fecha invalida.");
        
        if(tripulantes >0)
        	this.totalTripulantes= tripulantes;
        
        asientosConPasajeros=new HashMap <>();
        
        
    }

    private boolean codigoValido(String cod){
//    	* Devuelve el código del Vuelo con el formato: {Nro_vuelo_publico}-PUB. Por ejemplo--> 103-PUB
    	String fin = cod.substring(cod.length()-4);
    	if (fin.equals("-PUB") || fin.equals("-PRI")) {
	    	String digitos= cod.substring(0,cod.length()-4);
	    	Integer num = Integer.parseInt(digitos);
	    	if (num>=0)
	    		return true;
    	}
		return false;
	}

	private boolean esValidaFecha(String fecha) {
		String dia, mes, anio, guion1,guion2;
		dia = fecha.substring(0,2);
		mes = fecha.substring(3, 5);
		anio = fecha.substring(6);
		guion1= fecha.substring(2,3);
		guion2= fecha.substring(5, 6);
		Integer d =Integer.parseInt(dia);
		Integer m =Integer.parseInt(mes);
		Integer a =Integer.parseInt(anio);
		
		if (guion1.equals("/") && guion2.equals("/")) {
			if (d>=1 && d<=31) {

				if (m>=1 && m<=12) {
					if (a>=2000)
						return true;
				}
			}
		}
		return false;
	}

	public String consultarOrigen() {
        return origen;
    }

    public String consultarDestino() {
        return destino;
    }

    public String consultarFecha() {
        return fecha;
    }


    public void reservarAsiento(Integer dniCliente, Integer numAsiento) {
    	asientosConPasajeros.put(numAsiento,dniCliente);
    }
    
    public abstract boolean hayAsientosDisponiblesEnSeccion(String s);

    public HashMap <Integer,Integer> consultarAsientosYPasajeros() {
        return asientosConPasajeros;
    }
    
    public void cancelarPasaje(Integer numeroAsiento) {
    	asientosConPasajeros.remove(numeroAsiento);
    }
    
    public boolean estaEstePasajero(Integer dni) {
    	return asientosConPasajeros.containsValue(dni);
    }
    
    @Override
    public String toString() {
    	String s= codigo + " - "+ origen+" - "+destino+" - "+fecha;
    	return s;
    }

    
	public abstract String consultarSeccion(Integer numeroAsiento) ;

	public abstract HashMap<Integer, String> consultarAsientosDisponibles() ;

	public abstract double devolverPrecioDeVenta(String seccion) ;

	public abstract HashSet<Integer> consultarAsientosDisponiblesEnSeccion(String seccion) ;

	public String consultarCodVuelo() {
		return codigo;
	}

	public abstract double devolverPrecioDeVenta() ;

}