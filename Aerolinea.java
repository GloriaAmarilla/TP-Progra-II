package trabPracFinalPII;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Aerolinea {
	private HashMap<Integer,Cliente> clientes;

	private HashMap<String,Aeropuerto> aeropuertos;

	private HashMap<String, Vuelo> vuelos;
	
	
	private String nombre;
	
	private String cuit;
	
	
	/**
	* 1 constructor*/
	public Aerolinea(String nombre, String CUIT) {
		if(controlDeValides(nombre))
			this.nombre = nombre;
		else
			throw new RuntimeException("Nombre no valido");
		if(controlDeValides(nombre))
			this.cuit = CUIT;
		else
			throw new RuntimeException("CUIT no valido");
		aeropuertos=new HashMap<>();
		clientes =new HashMap<>();
		vuelos = new HashMap<>();
	}
	
	
	private boolean controlDeValides(String palabra) {
		if(palabra.equals(null))
			return false;
		if(palabra.equals(""))
			return false;
		return true;
	}

	/**
	* - 2
	* Se registran los clientes de la Aerolínea, compren o no pasaje. 
	* Cuando un cliente compre un pasaje es un Cliente (pasajero) y queda registrado en el vuelo correspondiente.
    */
	void registrarCliente(Integer dni, String nombre, String telefono) {
		if(!clientes.containsKey(dni))
			clientes.put(dni,new Cliente(dni, nombre, telefono));
		else
			throw new RuntimeException("Error, Cliente ya existe");
	}
	
	/**
	* - 3 
	* Se ingresa un aeropuerto con los datos que lo identifican. Estos aeropuertos son los que deberán corresponder
	* al origen y destino de los vuelos.
	* El nombre es único por aeropuerto en todo el mundo.
	*/
	void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {
		if(!aeropuertos.containsKey(nombre))
			aeropuertos.put(nombre,new Aeropuerto(nombre, pais, provincia, direccion));
		else
			throw new RuntimeException("Error, Aeropuerto ya exite");
	}
	/** - 4
	* El origen y destino deben ser aeropuertos con país=”Argentina” y ya registrados en la aerolinea. 
	* La fecha es la fecha de salida del vuelo.
	* Los asientos se considerarán numerados correlativamente empezando con clase Turista y terminando con la clase Ejecutivo.
	* Se cumple que precios.length == cantAsientos.length == 2
	* -  cantAsientos[0] = cantidad total de asientos en clase Turista.
	* -  cantAsientos[1] = cantidad total de asientos en clase Ejecutivo
	*   idem precios.
	* Tripulantes es la cantidad de tripulantes del vuelo.
	* valorRefrigerio es el valor del unico refrigerio que se sirve en el vuelo.
	* 
	* Devuelve el código del Vuelo con el formato: {Nro_vuelo_publico}-PUB. Por ejemplo--> 103-PUB
	* Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	*/
	
	private String generarCodigoVuelo(String tipo) {
		int numCod = 1;
		boolean bandera = false;
		String codigo="";
		while(bandera != true) {
			StringBuilder cod = new StringBuilder (""+numCod);
			cod.append("-");
			cod.append(tipo);
			if(codigoValido(""+cod)) {
				bandera=true;
				codigo=cod+"";
			}
			numCod+=1;
		}
		return codigo;
	}
	
	private boolean codigoValido(String cod) {
		if(vuelos.containsKey(cod))
			return false;
		return true;
	}
	
	String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, 
			double[] precios, int[] cantAsientos) {
		String codiVuelo = generarCodigoVuelo("PUB");
		if(destinoYorigenValidosParaNacional(origen,destino,'n')) {
			vuelos.put(codiVuelo, new VueloNacional(codiVuelo,origen,destino,fecha,tripulantes,valorRefrigerio,precios,cantAsientos));
		}
		else
			throw new RuntimeException("Origen o destino no valido");
		return codiVuelo;
	}
		
	private boolean destinoYorigenValidosParaNacional(String origen,String destino, char nacional) {
		return destinoOorigenValido(destino) && destinoOorigenValido(origen) && esNacionaloInternacional(origen,nacional) && esNacionaloInternacional(destino,nacional);
	}
	
	private boolean destinoOorigenValido(String lugar) {
		if(aeropuertos.containsKey(lugar))
			return true;
		return false;
	}
	
	private boolean esNacionaloInternacional(String lugar, char nacionalOinternacional) {
		boolean lugarValido=false;
		for(Map.Entry<String, Aeropuerto> aeropuerto: aeropuertos.entrySet()) {
			if(aeropuerto.getKey().equals(lugar))
				lugarValido|=esVueloNacionaloInternacional(aeropuerto.getValue(),nacionalOinternacional);
		}
		return lugarValido;
	}
	
	private boolean esVueloNacionaloInternacional(Aeropuerto aeropuerto, char nacionalOinternacional) {
		if(aeropuerto.consultarNacionalOInternacional()==nacionalOinternacional)
			return true;
		return false;
	}
	

	/** - 5
	* Pueden ser vuelos con escalas o sin escalas. 
	* La fecha es la de salida y debe ser posterior a la actual.  
	* Los asientos se considerarán numerados correlativamente empezando con clase Turista, siguiendo por Ejecutiva 
	* y terminando con Primera clase.
	* 
	* precios.length == cantAsientos.llength == 3
	*    - cantAsientos[0]  = cantidad total de asientos en clase Turista.
	*    - cantAsientos[1]  = cantidad total de asientos en clase Ejecutiva.
	*    - cantAsientos[2]  = cantidad total de asientos en Primera clase.
	*	 idem precios.
	*	 - escalas = nombre del aeropuerto donde hace escala. Si no tiene escalas, esto es un arreglo vacío.
	* Tripulantes es la cantidad de tripulantes del vuelo. 
	* valorRefrigerio es el valor del refrigerio que se sirve en el vuelo.
	* cantRefrigerios es la cantidad de refrigerio que se sirven en el vuelo.
	*
	* Devuelve el código del vuelo.  Con el formato: {Nro_vuelo_publico}-PUB, por ejemplo--> 103-PUB
	* Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	*/
	String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, 
		int cantRefrigerios, double[] precios,  int[] cantAsientos,  String[] escalas) {
		String codiVuelo= generarCodigoVuelo("PUB");
		HashSet<String> escalasNuevo = transformaraSet(escalas);
		if(destinoYorigenValidosParaInternacional(origen,destino,'i')&&escalasValidas(escalas))
			vuelos.put(codiVuelo, new VueloInternacional(codiVuelo,origen,destino,fecha,tripulantes,valorRefrigerio,cantRefrigerios,precios,cantAsientos,escalasNuevo));
		else
			throw new RuntimeException("Origen o destino no valido");
		return codiVuelo;
	}
	
	private HashSet<String> transformaraSet(String[] escalas){
		HashSet<String> escalasNuevo=new HashSet<>();
		for(String elem:escalas) {
			escalasNuevo.add(elem);
		}
		return escalasNuevo;
	}
	
	private boolean destinoYorigenValidosParaInternacional(String origen,String destino,char internacional) {
		return destinoOorigenValido(destino) && destinoOorigenValido(origen) && esNacionaloInternacional(destino,internacional);
	}
	
	private boolean escalasValidas(String[] escalas) {
		boolean lugarValido= true;
		for(String lugar:escalas) {
			lugarValido&=destinoOorigenValido(lugar);
		}
		return lugarValido;
	}
	
	

	
	/** 6 y 10 **** Se reune en esta firma ambos puntos de la especificación.
	 * 
	* Origen y destino son los Aeropuertos de donde parte y al que llega el jet. 
	* Fecha es la fecha de salida y debe ser posterior a la fecha actual.
	* Tripulantes es la cantidad de tripulantes del vuelo. 
	* Precio es el de un(1) jet. 
	* Se supone que se cuenta con todos los jets necesarios para trasladar todos los acompañantes. 
	* Se usara la cantidad de jets (necesarios) para el calculo del costo total del Vuelo.
	* IMPORTANTE; Se toma un sólo código para la compra aunque se necesiten mas de un jet. 
	* No se sirven refrigerios
	* 
	* Devuelve el código del vuelo. Con el formato: {Nro_vuelo_privado}-PRI, por ejemplo: 103-PRI
	*/
	String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,  int dniComprador, int[] acompaniantes) {
		String cod = generarCodigoVuelo("PRI");
		HashSet <Integer> acomPasajeros= new HashSet<>();
		if (controlValidesVueloPrivado(dniComprador,destino,origen,acompaniantes)) {//si cliente registrado 
					acomPasajeros= cargarAcompaniantes(acompaniantes);
					VueloPrivado pri = new VueloPrivado(cod,origen,destino,fecha,tripulantes,precio,dniComprador,acomPasajeros);
					vuelos.put(cod, pri);
					double precioVenta=pri.devolverPrecioDeVenta();
					Aeropuerto ae = aeropuertos.get(destino);
					ae.incrementarRecaudacion(precioVenta);
					return cod;	
		}
		else 
			throw new RuntimeException ("Datos Invalidos.");
	}
	
	private boolean dniRegistrado(int dni) {
		return clientes.containsKey(dni);
	}
	private boolean controlValidesVueloPrivado(Integer dniComprador,String destino,String origen,int[] acompaniantes) {
		return dniRegistrado(dniComprador)&&destinoYorigenValidosParaNacional(destino,origen,'n')&& acompaniantesRegistrados(acompaniantes);
	}

	private HashSet<Integer> cargarAcompaniantes(int[] acompaniantes) {
		HashSet<Integer> lista = new HashSet<>();
		for (int acom : acompaniantes) {
			lista.add(acom);
		}
		return lista;
	}


	private boolean acompaniantesRegistrados(int [] acompaniantes) {
		boolean estanRegistrados= true;
		for(int acom: acompaniantes) {
			estanRegistrados&= dniRegistrado(acom);
		}
		return estanRegistrados;
	}

	/** - 7 
	*  Dado el código del vuelo, devuelve un diccionario con los asientos aún disponibles para la venta 
	*  --> clave:  el número de asiento
	*  --> valor:  la sección a la que pertenecen los asientos.
	*  
	*/
	HashMap<Integer, String> asientosDisponibles(String codVuelo){
		if(!vuelos.containsKey(codVuelo))
			throw new RuntimeException("Codigo de vuelo no valido");
		Vuelo vuelo = queVueloEs(codVuelo);
		HashMap<Integer, String> asientos= null;
		asientos=vuelo.consultarAsientosDisponibles();
		return asientos;
	}
	
	private Vuelo queVueloEs(String codVuelo) {
		return vuelos.get(codVuelo);
	}
		
	/**
	* 8 y 9 devuelve el codigo del pasaje comprado.
	* Los pasajeros que compran pasajes deben estar registrados como clientes, con todos sus datos, antes de realizar la compra. Devuelve el codigo del pasaje y lanza una excepción si no puede venderlo.
	* aOcupar indica si el asiento que será ocupado por el cliente, o si solo lo compro para viajar más cómodo.
	* Devuelve un código de pasaje único que se genera incrementalmente sin distinguir entre tipos de vuelos.
	*/
	int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
		int codPasaje=0;
		if(dniRegistrado(dni)&&vuelos.containsKey(codVuelo)) {
			Cliente cl = buscarCliente(dni);
			codPasaje= generarCodigoPasaje(codVuelo,nroAsiento,dni);
			cl.guardarCodigoPasaje(codPasaje, aOcupar, codVuelo,nroAsiento);
			Vuelo vuelo = queVueloEs(codVuelo);
			vuelo.reservarAsiento(dni, nroAsiento);
			String sec= vuelo.consultarSeccion(nroAsiento);
			double recaudacion=vuelo.devolverPrecioDeVenta(sec);
			String a = vuelo.consultarDestino();
			Aeropuerto aero = buscarAeropuerto(a);
			aero.incrementarRecaudacion(recaudacion);
			
		}	
		else
			throw new RuntimeException("Datonos no validos");
		return codPasaje;
	}

	private Aeropuerto buscarAeropuerto(String origen) {
		return aeropuertos.get(origen);
	}


	private Cliente buscarCliente(int dni) {
		return clientes.get(dni);
	}

	private Integer generarCodigoPasaje(String codVuelo, Integer nroAsiento, Integer dni) {
		String codNum = codVuelo.substring(0,codVuelo.length()-4);
		Integer num = Integer.parseInt(codNum);
		Integer suma = num+nroAsiento+dni;
		return suma;
	}


	/** - 11. 
	 * devuelve una lista de códigos de vuelos. que estén entre fecha dada y hasta una semana despues. La lista estará vacía si no se encuentran vuelos similares. La Fecha es la fecha de salida.
	*/
	ArrayList<String> consultarVuelosSimilares(String origen, String destino, String fecha){
		ArrayList<String> vuelos = vueloPorFecha(origen,destino,fecha);
		String[] fechas = dameFechasPorDias(fecha,7);
//		System.out.println(fechas);
		for(int i=0;i<7;i++) {
			vuelos.addAll(vueloPorFecha(origen,destino,fechas[i]));
		}
		return vuelos;
	}
	
	private ArrayList<String> vueloPorFecha(String origen,String destino,String fecha){
		ArrayList<String> vuelosEnFecha = new ArrayList<>();
		for(Map.Entry<String, Vuelo> vuelo: vuelos.entrySet()) {
			if(destinoYorigenCorrectos(origen,destino,vuelo.getValue())&&vuelo.getValue().consultarFecha().equals(fecha))
				vuelosEnFecha.add(vuelo.getKey());
		}
		return vuelosEnFecha;
	}
	
	private boolean destinoYorigenCorrectos(String origen,String destino, Vuelo vuelo) {
		return vuelo.consultarDestino().equals(destino) && vuelo.consultarOrigen().equals(origen);
	}
	private String[] dameFechasPorDias(String fecha,int dias) {
		String[] fechasPorDias = new String[dias];
		String dia, mes, anio;
		dia = fecha.substring(0,2);
		mes = fecha.substring(3, 5);
		anio = fecha.substring(6);
		int d =Integer.parseInt(dia);
		int m =Integer.parseInt(mes);
		int a =Integer.parseInt(anio);
		for(int i=0; i<dias; i++) {
			if(d<=30)
				d+=1;
			else {
				d=1;
				if(m==12) {
					m=1;
					a+=1;
				}
				else
					m+=1;
			}
			
			fechasPorDias[i]=""+d+"/"+m+"/"+a;
		}
		
		return fechasPorDias;
	}
		
	/** - 12 
	 * Hay 2 posibles firmas para implementar esto: 12A  Y  12B
	 */
	
	/** - 12-A 
	* Se borra el pasaje y se libera el lugar para que pueda comprarlo otro cliente.
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
		Cliente cl = buscarCliente(dni);
		cl.eliminarPasajeSegunAsiento(nroAsiento);
		Vuelo vu = queVueloEs(codVuelo);
		vu.cancelarPasaje(nroAsiento);
	}
	
	/** 12-B
	* Se cancela un pasaje dado el codigo de pasaje. 
	* NO es necesario que se resuelva en O(1).
	*/
	void cancelarPasaje(int dni, int codPasaje) {
		Cliente cl = buscarCliente(dni);
		String cod= cl.consultarCodVuelo(codPasaje);
		int nroAsiento=cl.consultarNroAsiento(codPasaje);
		cl.eliminarPasaje(codPasaje);
		Vuelo vu= queVueloEs(cod);
		vu.cancelarPasaje(nroAsiento);
	}

	/** - 13
	* Cancela un vuelo completo conociendo su codigo.
	* Los pasajes se reprograman a vuelos con igual destino, no importa el numero del asiento pero 
	* si a igual seccion o a una mejor, y no importan las escalas.
	* Devuelve los codigos de los pasajes que no se pudieron reprogramar.
	* Los pasajes no reprogramados se eliminan. Y se devuelven los datos de la cancelación, indicando 
	* los pasajeros que se reprogramaron y a qué vuelo,  y los que se cancelaron por no tener lugar.
	* Devuelve una lista de Strings con este formato : “dni - nombre - telefono - [Codigo nuevo vuelo|CANCELADO]”
	* --> Ejemplo: 
	*   . 11111111 - Juan - 33333333 - CANCELADO
	*   . 11234126 - Jonathan - 33333311 - 545-PUB
	*/
	ArrayList<String> cancelarVuelo(String codVuelo){
		ArrayList<String> cambios=new ArrayList<>();
		Vuelo vu = queVueloEs(codVuelo); //duplico el vuelo a eliminar
		vuelos.remove(codVuelo); //quito el vuelo de lista de vuelos
		HashMap <Integer,Integer> asientosConPasajeros= vu.consultarAsientosYPasajeros();
		String origen = vu.consultarOrigen();
		String destino = vu.consultarDestino();
		String fecha = vu.consultarFecha();
		ArrayList<String> vuelosCompatibles =  consultarVuelosSimilares(origen,destino,fecha);
		HashMap <Integer,Integer> copia=new HashMap<>();
		
		
		for(int i=0;i<vuelosCompatibles.size();i++) {
			
			copia=duplicarMapa(asientosConPasajeros);
//			System.out.println(copia+"c");
			
			asientosConPasajeros= llenarVuelo(asientosConPasajeros,vuelosCompatibles.get(i),vu);
//			System.out.println(asientosConPasajeros+"a");
//			System.out.println(copia+"c2");
			
			cambios.addAll(agregarPasajerosConAsientoNuevo(copia,vuelosCompatibles.get(i)));
//			System.out.println(cambios+"cam");
		}
		
		if (asientosConPasajeros.size()!=0)
			cambios.addAll(agregarCancelados(asientosConPasajeros));
		
		return cambios;
	}
		
	private HashMap<Integer,Integer> duplicarMapa(HashMap<Integer,Integer> map){
		HashMap<Integer,Integer> dupli=new HashMap<>();
		
		for(Map.Entry<Integer, Integer> valores:map.entrySet()) {
			dupli.put(valores.getKey(), valores.getValue());
		}
		
		return dupli;
	}


	private ArrayList<String> agregarCancelados(HashMap<Integer, Integer> asientosConPasajeros) {
		ArrayList<String> cancelados=new ArrayList<>();
		Cliente cl;
		for (Map.Entry<Integer, Integer> entrada: asientosConPasajeros.entrySet()) {
			cl = buscarCliente(entrada.getValue());
			cancelados.add(cl.toString()+"CANCELADO");
		}
		return cancelados;
	}


	private Integer consultarElPrimero(HashSet<Integer> asientosDisponiblesEnSeccion) {
		Iterator<Integer> it= asientosDisponiblesEnSeccion.iterator();
		if(it.hasNext())
			return it.next();
		return null;
	}

	private HashMap<Integer,Integer> llenarVuelo(HashMap<Integer,Integer> asiCPas, String codVueloNuevo, Vuelo vueloViejo){
		HashMap<Integer,Integer> noAsignados=asiCPas;
		Vuelo vuelo = queVueloEs(codVueloNuevo);
		Iterator<HashMap.Entry<Integer, Integer>> it = noAsignados.entrySet().iterator();
    	
    	while(it.hasNext()) {
    		
    		HashMap.Entry<Integer, Integer> entrada=it.next();
    		Cliente cl= buscarCliente(entrada.getValue());
    		Integer asiento = entrada.getKey();
    		String sec = vueloViejo.consultarSeccion(asiento);
    		if (vuelo.hayAsientosDisponiblesEnSeccion(sec)) {
    			if(cl.estaOcupado(asiento)) {
    				venderPasaje(entrada.getValue(),codVueloNuevo,consultarElPrimero(vuelo.consultarAsientosDisponiblesEnSeccion(sec)),true);
    				it.remove();
    			}
    			cl.eliminarPasajesDeVuelo(vueloViejo.consultarCodVuelo());
    		}
    	}
		return noAsignados;
	}
	
	

	private ArrayList <String> agregarPasajerosConAsientoNuevo(HashMap<Integer,Integer> asiCPas, String codVuelo){
		ArrayList <String> asignados= new ArrayList<>();
		Vuelo vuelo = queVueloEs(codVuelo);
		for(Map.Entry<Integer, Integer> entrada: asiCPas.entrySet()) {
			if(vuelo.estaEstePasajero(entrada.getValue())) {
				Cliente cl= buscarCliente(entrada.getValue());
				asignados.add(cl.toString()+" - "+codVuelo);
			}
		}
		return asignados;
	}

	/** - 14
	* devuelve el total recaudado por todos los viajes al destino pasado por parámetro. 
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	double totalRecaudado(String destino) {
		return aeropuertos.get(destino).consultarRecaudacion();
	}
	
	/** - 15 
	* Detalle de un vuelo
	* devuelve un texto con el detalle un vuelo en particular.
	* Formato del String: CodigoVuelo - Nombre Aeropuerto de salida - Nombre Aeropuerto de llegada - 
	*                     fecha de salida - [NACIONAL /INTERNACIONAL / PRIVADO + cantidad de jets necesarios].
	* --> Ejemplo:
	*   . 545-PUB - Bariloche - Jujuy - 10/11/2024 - NACIONAL
	*   . 103-PUB - Ezeiza  - Madrid -  15/11/2024 - INTERNACIONAL
	*   . 222-PRI - Ezeiza - Tierra del Fuego - 3/12/2024 - PRIVADO (3)
	*/
	String detalleDeVuelo(String codVuelo) {
		return queVueloEs(codVuelo).toString();
	}

}
