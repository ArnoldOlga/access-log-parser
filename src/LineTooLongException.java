public class LineTooLongException extends RuntimeException {

    public LineTooLongException(int lineLength, int maxAllowedLength) {
        super("Длина строки: " + lineLength + ". Максимально допустимая длина: " + maxAllowedLength);
    }

}
