package redhawk.driver.exceptions;

public class AllocationException extends Exception{
	public AllocationException(){
		super();
	}
	
    public AllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
