package es.tecnalia.ittxartela.ws.server.util;

public class DocumentoValidator {

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String LETRAS_CONTROL_NIF = "JABCDEFGHI";

    public static boolean esDocumentoValido(String doc) {
        if (doc == null || doc.isEmpty()) return false;

        doc = doc.toUpperCase().trim();

        if (doc.matches("^[0-9]{8}[A-Z]$")) {
            return validarDNI(doc);
        } else if (doc.matches("^[XYZ][0-9]{7}[A-Z]$")) {
            return validarNIE(doc);
        } else if (doc.matches("^[A-HJ-NP-SUVW][0-9]{7}[0-9A-J]$")) {
            return validarNIF(doc);
        }

        return false;
    }

    private static boolean validarDNI(String dni) {
        try {
            int numero = Integer.parseInt(dni.substring(0, 8));
            char letraEsperada = LETRAS_DNI.charAt(numero % 23);
            return dni.charAt(8) == letraEsperada;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validarNIE(String nie) {
        char letraInicial = nie.charAt(0);
        String numeroStr;

        if (letraInicial == 'X') {
            numeroStr = "0" + nie.substring(1, 8);
        } else if (letraInicial == 'Y') {
            numeroStr = "1" + nie.substring(1, 8);
        } else if (letraInicial == 'Z') {
            numeroStr = "2" + nie.substring(1, 8);
        } else {
            return false;
        }

        try {
            int numero = Integer.parseInt(numeroStr);
            char letraEsperada = LETRAS_DNI.charAt(numero % 23);
            return nie.charAt(8) == letraEsperada;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validarNIF(String nif) {
        char letraInicial = nif.charAt(0);
        String numeros = nif.substring(1, 8);
        char digitoControl = nif.charAt(8);

        try {
            int sumaPar = 0;
            int sumaImpar = 0;

            for (int i = 0; i < numeros.length(); i++) {
                int n = Character.getNumericValue(numeros.charAt(i));
                if ((i + 1) % 2 == 0) {
                    sumaPar += n;
                } else {
                    int doble = n * 2;
                    sumaImpar += (doble > 9) ? doble - 9 : doble;
                }
            }

            int total = sumaPar + sumaImpar;
            int unidad = 10 - (total % 10);
            if (unidad == 10) unidad = 0;

            // Tipos de NIF con letra como dígito de control
            if ("KPQRSNW".indexOf(letraInicial) >= 0) {
                return digitoControl == LETRAS_CONTROL_NIF.charAt(unidad);
            }
            // Tipos de NIF con número como dígito de control
            else if ("ABEH".indexOf(letraInicial) >= 0) {
                return digitoControl == Character.forDigit(unidad, 10);
            }
            // Tipos de NIF que pueden tener letra o número
            else {
                return digitoControl == LETRAS_CONTROL_NIF.charAt(unidad) ||
                       digitoControl == Character.forDigit(unidad, 10);
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(esDocumentoValido("12345678Z")); // true
        System.out.println(esDocumentoValido("X1234567L")); // true
        System.out.println(esDocumentoValido("B12345678")); // true o false según el dígito
        System.out.println(esDocumentoValido("Q1234567B")); // true o false según el dígito
    }
}
