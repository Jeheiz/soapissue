package es.tecnalia.ittxartela.ws.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoEspecifico {

	OK("0000", "Datos encontrados para los parámetros de entrada indicados"),
	MAS_DE_UNO("0001","Más de una coincidencia para los parámetros de entrada indicados"),
	MAS_DE_CIEN("0002","Más de 100 coincidencias. Acote su búsqueda"),
	NIF_INVALIDO("0003","No se ha especificado un NIF/NIE válido. Formato 99999999X"),
	DATA_NOT_FOUND("0004","No se han encontrado datos para los parámetros de entrada indicados"),
	FECHA_INVALIDA("0005","No se ha especificado una fecha límite válida. Formato dd/MM/yyyy"),
	
	;

	private String resultado;
	private String descripcion;

}
