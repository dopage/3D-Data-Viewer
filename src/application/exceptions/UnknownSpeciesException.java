package application.exceptions;

public class UnknownSpeciesException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownSpeciesException() {
		super();
	}
	
	public UnknownSpeciesException(String message) {
		super(message);
	}
}
