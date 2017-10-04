/**
 * Created by Tob on 04/10/2017.
 */
public class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException() { super(); }
    public InvalidFileFormatException(String message) { super(message); }
    public InvalidFileFormatException(String message, Throwable cause) { super(message, cause); }
    public InvalidFileFormatException(Throwable cause) { super(cause); }
}
