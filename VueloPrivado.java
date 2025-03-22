package trabPracFinalPII;
import java.util.*;

public class VueloPrivado extends Vuelo {

    public VueloPrivado() {
    }

    private double precioPorJet;

    private int cantAsientosPorJet=15;

    private Integer dniComprador;

    private Set<Integer> acompaniantes;

    private double precioFinal;

    private int cantJet;
    
    private double impuesto = 0.30;

    public VueloPrivado(String codigo, String origen, String destino, String fecha, int tripulantes, double precio, Integer dniComprador, HashSet<Integer> acompaniantes) {
    	super (codigo,origen,destino,fecha,tripulantes);
    	
    	if(precio>0.0)
    		this.precioPorJet = precio;
    	else
    		throw new RuntimeException("Precio no valido");
    	
    	this.dniComprador = dniComprador;
    	
    	this.acompaniantes = acompaniantes;
    	
    	calcularCantJet();
    	
    }

    @Override
    public double devolverPrecioDeVenta() {//devuelve el precio total por todo los jet 
        double p = cantDeJet() * precioPorJet;
        precioFinal = p + p*impuesto;
        return precioFinal;
    }

    public String consultarDestino() {//devuelve el destino asignado
        return super.consultarDestino();
    }

    public int cantDeJet() {//Devuelve la cantidad de Jet nesecarios
        return cantJet;
    }
    
    private void calcularCantJet() {// Calcula la cantidad de Jet necesrios para tranportar a todos
    	int cantidadDePasajeros=acompaniantes.size()+1;
    	cantJet= cantidadDePasajeros/cantAsientosPorJet;
    	if((cantJet*-cantAsientosPorJet)+cantidadDePasajeros>0)
    		cantJet+=1;
    }
    
    @Override
    public String toString() {
    	String s= super.toString();
    	return s + " - PRIVADO"+" ("+cantDeJet()+")";
    }
    
    public Integer consultarDNIcomprador() {
    	return dniComprador;
    }

	@Override
	public String consultarSeccion(Integer numeroAsiento) {
		throw new RuntimeException("No secciones en vuelo privado.");
	}

	@Override
	public HashMap<Integer, String> consultarAsientosDisponibles() {
		throw new RuntimeException("No asientos disponibles en vuelo privado.");
	}

	@Override
	public double devolverPrecioDeVenta(String seccion) {
		throw new RuntimeException("No secciones en vuelo privado.");
	}

	@Override
	public HashSet<Integer> consultarAsientosDisponiblesEnSeccion(String seccion) {
		throw new RuntimeException("No secciones en vuelo privado.");
	}

	@Override
	public boolean hayAsientosDisponiblesEnSeccion(String s) {
		throw new RuntimeException("No secciones en vuelo privado.");
	}



}