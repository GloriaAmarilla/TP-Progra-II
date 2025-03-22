package trabPracFinalPII;
import java.util.*;

import java.io.*;
import java.util.*;

public class Cliente {

    public Cliente() {
    }

    private Integer dni;

    private String nombre;

    private String telefono;

    private HashMap<Integer,Integer> asientosConPasaje;
    
    private HashMap<Integer,Boolean> codigosPasajes;

    private HashMap<Integer,String> pasajesConCodigosDeVuelos;

    public Cliente(Integer dni, String nombre, String telefono) {
    	if(dni>=1000000)
    		this.dni=dni;
    	else
    		throw new RuntimeException("DNI Invalido");
    	if(nombre != "")
    		this.nombre=nombre;
    	else
    		throw new RuntimeException("Nombre no valido");
    	if(telefono.length()>7)
    		this.telefono=telefono;
    	else
    		throw new RuntimeException("Telefono no valido");
    	asientosConPasaje = new HashMap<>();
    	codigosPasajes = new HashMap <>();
    	pasajesConCodigosDeVuelos= new HashMap <>();
    		
    }
    
    public String consultarNombre() {
    	return nombre;
    }
    public Integer consultarDNI() {
    	return dni;
    }
    public String consultarTelefono() {
    	return telefono;
    }

    public boolean guardarCodigoPasaje(Integer codPasaje, Boolean aOcupar, String codVuelo, Integer nroAsiento) {
    	boolean seLogro;
		if(codigosPasajes.containsKey(codPasaje))
			seLogro=false;
		else {
			codigosPasajes.put(codPasaje, aOcupar);
			pasajesConCodigosDeVuelos.put(codPasaje, codVuelo);
			asientosConPasaje.put(nroAsiento,codPasaje);
			seLogro=true;
		}
		return seLogro;
    }

    public HashSet<Integer> consultarCodigosPasajes() {
    	HashSet<Integer> codPasaje= new HashSet<>(codigosPasajes.keySet());
        return codPasaje;
    }
    
    public Integer consultarCodPasaje(Integer nroAsiento) {
    	return asientosConPasaje.get(nroAsiento);
    }
    
    public Integer consultarNroAsiento (Integer codPasaje) {
    	Integer nroAsiento=null;
    	for(Map.Entry<Integer, Integer> codigos:asientosConPasaje.entrySet()) {
    		if(codigos.getValue().equals(codPasaje))
    			nroAsiento = codigos.getKey();
    	}
    	return nroAsiento;
    }
    
    public boolean estaOcupado(Integer numAsiento) {
    	Integer pasaje=consultarCodPasaje(numAsiento);
    	if(codigosPasajes.containsKey(pasaje))
    		return codigosPasajes.get(pasaje);
    	throw new RuntimeException ("no tiene este pasaje.");
    }
 
    
    public void eliminarPasajeSegunAsiento(Integer nroAsiento) {
    	Integer codELiminar = asientosConPasaje.get(nroAsiento);
    	asientosConPasaje.remove(nroAsiento);
    	codigosPasajes.remove(codELiminar);
    	pasajesConCodigosDeVuelos.remove(codELiminar);
    	
    }

    public void eliminarPasaje(Integer codPasaje) {
    	Iterator <HashMap.Entry<Integer,Integer>> it = asientosConPasaje.entrySet().iterator();
		HashMap.Entry<Integer, Integer> entrada;
    	while(it.hasNext()) {
    		entrada = it.next();
    		if (entrada.getValue().equals(codPasaje))
    			it.remove();
    	}

    	codigosPasajes.remove(codPasaje);
    	pasajesConCodigosDeVuelos.remove(codPasaje);
    }
    
    public void eliminarPasajesDeVuelo(String codVuelo) {
    	ArrayList <Integer> pasajesEliminar = new ArrayList<>();
    	Iterator<HashMap.Entry<Integer, String>> it = pasajesConCodigosDeVuelos.entrySet().iterator();
    	
    	while(it.hasNext()) {
    		
    		HashMap.Entry<Integer, String> entrada=it.next();
    		if(entrada.getValue().equals(codVuelo)) {
    			pasajesEliminar.add(entrada.getKey());
    			it.remove();
    		}	
    	}
    	for(Integer codigo:pasajesEliminar) {
    		eliminarPasaje(codigo); //los elimina de asientosConPasaje y de codigosPasajes y de pasajesConCOdigosDeVuelos no los eliminina porque ya estan eliminados
    	}
    }
    
    public String consultarCodVuelo(Integer codPasaje) {
    	String codDeVuelo = pasajesConCodigosDeVuelos.get(codPasaje);
        return codDeVuelo;
    }
    @Override
    public String toString() {
    	String s= ""+dni+" - "+nombre+" - "+telefono;
    	return s;
    }
    

}