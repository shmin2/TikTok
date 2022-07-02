package BunkerApi.Validations;

public class RequestValidations {
    public static boolean checkTokenFormat(String str){
        int validLength = 1; // нет требований
        return str != null && str.length() == validLength;
    }
}
