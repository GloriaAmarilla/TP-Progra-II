package trabPracFinalPII;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VueloInternacional extends VueloNacional {

    public VueloInternacional() {
    }

    private Set<String> escalas;

    private int cantidadRefrigerios;

    private HashMap<Integer,Boolean> asientosPrimeraClase;

    public VueloInternacional(String codigo,String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, int cantRefrigerios, double [] precios, int [] cantAsientos, Set<String> escalas) {
        super(codigo,origen,destino,fecha,tripulantes,valorRefrigerio,precios,cantAsientos);
        this.cantidadRefrigerios=cantRefrigerios;
        this.escalas=escalas;
        generarCodigosAsientos(cantAsientos);
    }
    
    
    private void generarCodigosAsientos(int [] cantAsientos) {
    	asientosPrimeraClase = new HashMap<>();
    	int continua= cantAsientos[0]+cantAsientos[1];
		for (int i=0; i<cantAsientos[2]; i++) {
			asientosPrimeraClase.put(i+continua+1,false);
		}
    }

    @Override
    public HashMap <Integer, String>  consultarAsientosDisponibles() {
    	HashMap<Integer,String> asientosDisponibles = new HashMap<>();
        asientosDisponibles=super.consultarAsientosDisponibles();
        
        for(HashMap.Entry<Integer,Boolean> entrada:asientosPrimeraClase.entrySet()) {
        	if(entrada.getValue() == false)
        		asientosDisponibles.put(entrada.getKey(), "primera");
        }
    	return asientosDisponibles;
    }

    public void reservarAsiento(Integer dniCliente, Integer numAsiento) {
    	super.reservarAsiento(dniCliente, numAsiento);
    	if(asientosPrimeraClase.containsKey(numAsiento))
    		asientosPrimeraClase.replace(numAsiento, true);
    }

    @Override
    public double devolverPrecioDeVenta(String seccion) {
//    	String seccionAdevolverPrecio=seccion.toLowerCase();
    	double precioSeccion=consultarPrecioSeccion(seccion);
    	return precioFinalCalculado(precioSeccion);	
    }
    
   
    private double consultarPrecioSeccion(String seccion) {
    	double[] preciosDeSeccion = super.consultarPrecios();
    	double precioSeccion=0.0;
    	if (seccion.equals("turista"))
    		precioSeccion = preciosDeSeccion[0];
    	else {
    		if (seccion.equals("ejecutivo"))
    			precioSeccion = preciosDeSeccion[1];
    	
    		else {
    			if (seccion.equals("primera"))
				precioSeccion = preciosDeSeccion[2];
    		}
    	}
    	

    	return precioSeccion;

	}
    
    private double precioFinalCalculado(double precioSeccion) {
    	double precioDeUnRefrigerio=super.consultarPrecioRefrigerio();
    	double total= precioSeccion+(precioDeUnRefrigerio*cantidadRefrigerios);
    	return total + (total*super.consultarImpuesto());
    }

    public String consultarDestino() {
        return super.consultarDestino();
    }

    @Override
    public void cancelarPasaje(Integer numeroAsiento) {
        super.cancelarPasaje(numeroAsiento);
        habilitarAsiento(numeroAsiento);
    }

    private void habilitarAsiento(Integer numeroAsiento) {
        if(asientosPrimeraClase.containsKey(numeroAsiento))
        	asientosPrimeraClase.put(numeroAsiento, false);
    }

    @Override
    public String consultarSeccion(Integer numeroAsiento) {
    	if(asientosPrimeraClase.containsKey(numeroAsiento))
    		return "primera";
        return super.consultarSeccion(numeroAsiento);
    }
    
  

    @Override
    public HashSet<Integer> consultarAsientosDisponiblesEnSeccion(String seccion) {
        HashSet<Integer> asientosDisponiblesEnSeccion = new HashSet <>();
        if(seccion.toLowerCase().equals("primera"))
        	for(HashMap.Entry<Integer,Boolean> entrada:asientosPrimeraClase.entrySet()) {
            	if(entrada.getValue() == false)
            		asientosDisponiblesEnSeccion.add(entrada.getKey());
            }
        else
            asientosDisponiblesEnSeccion=super.consultarAsientosDisponiblesEnSeccion(seccion);
        return asientosDisponiblesEnSeccion;
    }

    public Set<String> consultarEscalas() {
        return escalas;
    }
    
    @Override
    public boolean hayAsientosDisponiblesEnSeccion(String s) {
		HashSet<Integer> asientos= consultarAsientosDisponiblesEnSeccion(s);
		if(asientos.size()==0)
			return false;
		return true;
	}
    
    @Override
    public String toString() {
    	String s = super.consultarCodvuelo()+" - "+super.consultarOrigen()+" - "+super.consultarDestino()+" - "+super.consultarFecha()+" - INTERNACIONAL";
    	return s;
    }

}