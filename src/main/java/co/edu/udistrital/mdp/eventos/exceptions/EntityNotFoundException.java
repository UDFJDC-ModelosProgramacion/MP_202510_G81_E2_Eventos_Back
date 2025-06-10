package co.edu.udistrital.mdp.eventos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Excepción que se lanza cuando en el proceso de búsqueda no se encuentra una entidad
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String message) {
		super(message);
	}
}
