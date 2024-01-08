package exceptions;

@SuppressWarnings("serial")
public class DeleteException extends Exception {
    public DeleteException(String mensagem) {
        super(mensagem);
    }
}
