package exceptions;

@SuppressWarnings("serial")
public class UpdateException extends Exception {
    public UpdateException(String mensagem) {
        super(mensagem);
    }
}
