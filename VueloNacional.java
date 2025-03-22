package trabPracFinalPII;
import java.util.*;

public class VueloNacional extends Vuelo {

    public VueloNacional() {
    }

    private double [] precios;

    private int[] cantAsientos;
    
    private HashMap<Integer,Boolean> asientosTurista;

    private HashMap<Integer,Boolean> asientosEjecutivo;

    private double precioRefrigerio;
    
    private double impuesto = 0.20;

    public VueloNacional(String codigo, String origen, String destino, String fecha, int tripulantes, 
    		double valorRefrigerio, double [] precios, int [] cantAsientos) {
        super(codigo,origen,destino,fecha, tripulantes);
        
        //Para IREP de precio de refrigerio
        if(valorRefrigerio>0) {
        	precioRefrigerio=valorRefrigerio;
        }
        else
        	throw new RuntimeException("Precio de refrigerio invalido.");
        
        
        //Para IREP de precios de sectores del vuelo
        if (precios.length==2 || precios.length==3 ) {    	
        
	        boolean todosPreciosValidos = true;
	        boolean orden = true; //para que se respete primer precio turista, segundo ejecutivo, y si hay tercero ejecutivo
	        for (int i=0; i<precios.length; i++) {
	        	todosPreciosValidos &= precios[i]>0; //chequea precios positivos
	        	if(i<precios.length-1)
	        		orden &= precios[i]<precios[i+1]; //chequea orden creciente
	        }
	        
	        if (todosPreciosValidos && orden)
	        	this.precios= precios;
	        else
	        	throw new RuntimeException("Precios de sectores invalidos.");

	    //Para IREP de cantidades de asientos de cada sector
        boolean todosCantAsientosValidos = true;
        for (int i=0; i<cantAsientos.length;i++) {
        	todosCantAsientosValidos &= cantAsientos[i]>0; //todas las cantidades positivas
        }
        if (todosCantAsientosValidos && cantAsientos.length>0 && cantAsientos.length<4) {
        	this.cantAsientos = cantAsientos;
        	  generarCodigosAsientos(cantAsientos);
        }
        else
        	throw new RuntimeException ("Cantidades de asientos invalidas.");
        }	
    }

    //Para generar los codigos de los asientos de turista y ejecutivo
    private void generarCodigosAsientos(int [] cantAsientos) {
    	asientosTurista = new HashMap<>();
    	asientosEjecutivo = new HashMap<>();
    	int continua=0;
    	for (int i=0; i<cantAsientos[0]; i++) {
    		asientosTurista.put(i+1,false);
    		continua++;
    	}
    	
    	for (int i=0; i<cantAsientos[1]; i++) {
    		asientosEjecutivo.put(i+continua+1,false);
    	}	
 
    }	

    @Override
    ////devolver:--> clave:  el número de asiento  --> valor:  la sección a la que pertenecen los asientos
    public HashMap <Integer, String> consultarAsientosDisponibles() {
        HashMap <Integer, String> asientosDisponibles= new HashMap <>();
        
        Iterator<HashMap.Entry<Integer,Boolean>> it= asientosTurista.entrySet().iterator();
    	HashMap.Entry<Integer,Boolean> entrada;
    	Boolean val;
    	while(it.hasNext()) {
    		entrada = it.next();
    		val = entrada.getValue();
    		if (val.equals(false))
    			asientosDisponibles.put(entrada.getKey(), "Turista");
    	}
        
    	Iterator<HashMap.Entry<Integer,Boolean>> it2= asientosEjecutivo.entrySet().iterator();
      	HashMap.Entry<Integer,Boolean> entrada2;
      	Boolean val2;
      	while(it2.hasNext()) {
      		entrada2 = it2.next();
      		val2 = entrada2.getValue();
      		if (val2.equals(false))
      			asientosDisponibles.put(entrada2.getKey(), "Ejecutivo");
      	}
        
        return asientosDisponibles;
    }
    
    public int[] consultarCantAsientos() {
    	return cantAsientos;
    }
    
    public double[] consultarPrecios() {
    	return precios;
    }

    public double consultarPrecioRefrigerio() {
    	return precioRefrigerio;
    }
    
    public void reservarAsiento(Integer dniCliente, Integer numAsiento) {
    	super.reservarAsiento(dniCliente, numAsiento);
    	if (asientosTurista.containsKey(numAsiento))
    		asientosTurista.replace(numAsiento, true);
    	else {
    		if (asientosEjecutivo.containsKey(numAsiento))
    		asientosEjecutivo.replace(numAsiento, true);
    	}
    }

    @Override
    public double devolverPrecioDeVenta(String seccion) {
    	String s = seccion.toLowerCase(); //seccion en minusculas
    	double p; //acumulador de precio final
        if(s.equals("turista")) {
        	p = precios[0]+precioRefrigerio;
        	return p+(p*impuesto);
        }
        if(s.equals("ejecutivo")) {
        	p = precios[1]+precioRefrigerio;
        	return p+(p*impuesto);
        }
        else
        	throw new RuntimeException("Sección invalida.");
    }

    public String consultarDestino() {
        return super.consultarDestino();
    }
    
    public double consultarImpuesto() {
    	return impuesto;
    }

    @Override
    public void cancelarPasaje(Integer numeroAsiento) {
    	super.cancelarPasaje(numeroAsiento);
    	if (asientosTurista.containsKey(numeroAsiento))
    		asientosTurista.replace(numeroAsiento, false);
    	else {
    		if (asientosEjecutivo.containsKey(numeroAsiento))
    		asientosEjecutivo.replace(numeroAsiento, false);
    	}
    }

    @Override
    public String consultarSeccion(Integer numeroAsiento) {
    	if (asientosTurista.containsKey(numeroAsiento))
    		return "turista";
    	else {
    		if (asientosEjecutivo.containsKey(numeroAsiento)) {
    			return "ejecutivo";
    		}
    		else
    			throw new RuntimeException("Numero de Asiento invalido.");
    	}
    }

    @Override
    public HashSet<Integer> consultarAsientosDisponiblesEnSeccion(String seccion) {
        HashSet <Integer> asientosDisponibles = new HashSet <>();
        String s = seccion.toLowerCase();
        
        if (s.equals("turista")) {
        	Iterator<HashMap.Entry<Integer,Boolean>> it= asientosTurista.entrySet().iterator();
          	HashMap.Entry<Integer,Boolean> entrada;
          	Boolean val;
          	while(it.hasNext()) {
          		entrada = it.next();
          		val = entrada.getValue();
          		if (val.equals(false))
          			asientosDisponibles.add(entrada.getKey());
          	}
        }
        else {
	        if (s.equals("ejecutivo")) {
	        	Iterator<HashMap.Entry<Integer,Boolean>> it= asientosEjecutivo.entrySet().iterator();
	          	HashMap.Entry<Integer,Boolean> entrada;
	          	Boolean val;
	          	while(it.hasNext()) {
	          		entrada = it.next();
	          		val = entrada.getValue();
	          		if (val.equals(false))
	          			asientosDisponibles.add(entrada.getKey());
	          	}
	        }
        }
        
        return asientosDisponibles;
    }
    
    public String consultarCodvuelo() {
    	return super.consultarCodVuelo();
    }

    @Override
    public String toString() {
    	String s= super.toString();
    	return s + " - NACIONAL" ;
    }

	@Override
	public double devolverPrecioDeVenta() {
		throw new RuntimeException ("No especifico seccion.");
	}

	@Override
	public boolean hayAsientosDisponiblesEnSeccion(String s) {
		HashSet<Integer> asientos= consultarAsientosDisponiblesEnSeccion(s);
		if(asientos.size()==0)
			return false;
		return true;
	}
}