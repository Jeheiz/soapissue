
package es.tecnalia.ittxartela.ws.server.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author dominguez
 */
@Slf4j
public class Utils {

    private static Map dfMap = new HashMap();
    int dni;
    char letra;
    static final char tabla[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y',
        'F', 'P', 'D', 'X', 'B', 'N', 'J',
        'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'
    };

    public static boolean comprobarDni(int ndni, char nletra) throws Exception {

        boolean correcto = false;
        
        if (Character.toUpperCase(nletra) == tabla[ndni % 23]) {
            //dni = ndni;
            //letra = Character.toUpperCase(nletra);

            correcto = true;
        }
            
        return correcto;

    }

    public static boolean esDNIValido(String dni) {

   
        boolean correcto=false;
        if((dni.startsWith("X") || dni.startsWith("Y") || dni.startsWith("Z") || dni.startsWith("x") || dni.startsWith("y") || dni.startsWith("z"))&&(dni.length()==9)){
            
            String numero="";
            
            if(dni.startsWith("X") || dni.startsWith("x")){
                numero="0"+dni.substring(1, dni.length()-1);
            }
            if(dni.startsWith("Y") || dni.startsWith("y")){
                numero="1"+dni.substring(1, dni.length()-1);
                
            }
            if(dni.startsWith("Z") || dni.startsWith("z")){
                numero="2"+dni.substring(1, dni.length()-1);
            }
            
            String letra=dni.substring(dni.length()-1, dni.length());
            char let=letra.charAt(0);
            
            try{
                
                correcto=comprobarDni(Integer.parseInt(numero),let);
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
            return correcto;

        }else{
        
        
            if (!esPatronDNIValido(dni)) {
                return false;
            }
            try {
                Integer.parseInt(dni.substring(0, 8));
                if (Character.toUpperCase(dni.charAt(8)) != tabla[Integer.parseInt(dni.substring(0, 8)) % 23]) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
    }

    private static boolean esPatronDNIValido(String dni) {
        Pattern patron = Pattern.compile("[0-9]{7,8}([a-z,A-Z])?");
        Matcher encaja = patron.matcher(dni);

        return encaja.matches();
    }

    public static boolean esPatronFechaValido(String fecha) {
        //Pattern patron=Pattern.compile("[0-9]{2}(-[0,9]{2}) (-[0,9]{4})?");
        Pattern patron = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher encaja = patron.matcher(fecha);

        if (encaja.matches()) {
            if (Integer.parseInt(fecha.substring(0, 2)) > 31) {
                return false;
            } else if (Integer.parseInt(fecha.substring(3, 5)) > 12) {
                return false;
            } else if (Integer.parseInt(fecha.substring(6, 10)) < 1900) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }

    }

    public static boolean esPatronFechaValidoMetaposta(String fecha) {
        //Pattern patron=Pattern.compile("[0-9]{2}(-[0,9]{2}) (-[0,9]{4})?");
        Pattern patron = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher encaja = patron.matcher(fecha);

        if (encaja.matches()) {
            if (Integer.parseInt(fecha.substring(0, 4)) < 1900) {
                return false;
            } else if (Integer.parseInt(fecha.substring(5, 7)) > 12) {
                return false;
            } else if (Integer.parseInt(fecha.substring(8, 10)) > 31) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }

    }


    /*Método que valida el formato de las fechas
     *@param fecha valor de fecha introducido
     *@param cadena error que envia si no es correcto
     *@param obligatorio indica si el campo es o no obligatorio
     *@return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
     */
    public static boolean validaFecha(String fecha) {
        String campo = fecha.trim();

        if (campo.length() > 0) {
            if (campo.length() != 10) {
                return false;
            }
            //saca de la fecha dia, mes y año
            String[] fech1 = campo.split("[/]");
            //comprueba que haya introducido el formato dd/mm/yyyy
            if (fech1.length < 3) {
                return false;
            }
            int dia = new Integer(fech1[0]).intValue();
            int mes = new Integer(fech1[1]).intValue();
            int anio = new Integer(fech1[2]).intValue();
            //el mes debe estar entre 1 y 12
            if (mes < 1 || mes > 12) {
                return false;
            }
            //comprueba que el año este entre 1900 y 2099
            if (anio < 1900 || anio > 2099) {
                return false;
            }
            //comprueba el numero de dias dependiendo del mes
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                if (dia <= 0 || dia > 31) {
                    return false;
                }
            }
            if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                if (dia <= 0 || dia > 30) {
                    return false;
                }
            }
            if (mes == 2) {
                if (anio % 4 > 0) {
                    if (dia > 28) {
                        return false;
                    }
                } else if (anio % 100 == 0 && anio % 400 > 0) {
                    if (dia > 28) {
                        return false;
                    }
                } else {
                    if (dia > 29) {
                        return false;
                    }
                }
            }
        }
        return true;
    }//Fin del método validaFecha()
    /**
     * Validate e-mail adderss
     *
     * @param email E-mail address
     *
     * @return true if e-mail address is valid, false in othercase
     */
    public static boolean emailValidation(String email) {
        if (email.matches(".+@.+\\..+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validate e-mail adderss
     *
     * @param email E-mail address
     *
     * @return true if e-mail address is valid, false in othercase
     */
    public static boolean emailValidation2(String args) {
        Pattern p = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
        Matcher m = p.matcher(args);

        if (m.find()) {
            log.debug("E-mail correct");

            return true;
        } else {
            log.debug("E-mail no correct");

            return false;
        }
    }

    /**
     * Validate e-mail adderss
     *
     * @param email E-mail address
     *
     * @return true if e-mail address is valid, false in othercase
     */
    public static boolean isEmail(String email) {
        boolean isValid = true;
        try {

            String patternEmail = "^[\\w-]+(\\.[\\w-]+)*@[a-zA-Z\\d-]+(\\.[a-zA-Z\\d-]+)*$";
            isValid = email.matches(patternEmail);
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean isNumeric(String cadena) {
        try {
            int intNumero = Integer.parseInt(cadena);
            if (intNumero>0){
               return true;
            }else{
               return false; 
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    private static Random m_randomGenerator = new Random();

    /** Función que mediante un random, genera el password del usuario que se acaba de registrar.
    Se devuelve un string de 8 caracteres. */
    public static String generatePass() {
        return Integer.toString(m_randomGenerator.nextInt(999999999) + 1000000000);
    }

    public static String generateUsername(String name, String apellido1) {
        //--------
        // NOMBRE:
        //--------

        String normalizedName = name.toLowerCase();
        // Eliminamos los caracteres especiales en general:
        normalizedName = remSpecialChar(normalizedName);
        // Eliminamos los caracteres especiales que se pueden dar en los dni:
        normalizedName = remDash(normalizedName);
        // Eliminamos finalmente los espacios en blanco:
        normalizedName = remSpaces(normalizedName);

        //-----------
        // APELLIDO1:
        //-----------

        String normalizedSurname = apellido1.toLowerCase();

        // Eliminamos los caracteres especiales en general:
        normalizedSurname = remSpecialChar(normalizedSurname);
        // Eliminamos los caracteres especiales que se pueden dar en los dni:
        normalizedSurname = remDash(normalizedSurname);
        // Eliminamos finalmente los espacios en blanco:
        normalizedSurname = remSpaces(normalizedSurname);

        return normalizedName.substring(0, 1) + normalizedSurname;
    }
    private static final String BAD_SYMBOLS = "ýÝáàãâäÁÀÃÂÄéêèëÉÊÈËíìïîÍÌÏÎóõôòöÓÔÕÒÖúüùûÚÜÙÛçÇñÑ'\\ºª^`´¨";
    private static final String DASH_SYMBOLS = "-_.";
    private static final String GOOD_SYMBOLS = "yYaaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnN        ";

    /** Función que sirve para sustituir determinados caracteres especiales que se introducen
    por teclado por el usuario, y que por características de la bd no se pueden insertar en
    la misma. */
    private static String remSpecialChar(String string) {
        for (int i = BAD_SYMBOLS.length() - 1; i > -1; i--) {
            if (string.indexOf(BAD_SYMBOLS.charAt(i)) > -1) {
                string = string.replace(BAD_SYMBOLS.charAt(i), GOOD_SYMBOLS.charAt(i));
            }
        }
        return string;
    }

    /** Función que sirve para sustituir determinados caracteres especiales que se introducen
    normalmente en los dni, para que todos los dni tengan el mismo formato. */
    private static String remDash(String string) {
        for (int i = DASH_SYMBOLS.length() - 1; i > -1; i--) {
            if (string.indexOf(DASH_SYMBOLS.charAt(i)) > -1) {
                string = string.replace(DASH_SYMBOLS.charAt(i), ' ');
            }
        }
        return string;
    }

    /**
     * Función que sirve para eliminar los espacios en blanco.
     */
    private static String remSpaces(String string) {
        StringTokenizer tokenizer = new StringTokenizer(string);
        String newString = "";
        while (tokenizer.hasMoreTokens()) {
            newString += tokenizer.nextToken();
        }
        return newString;
    }
    
    /**
     * Map has single msisdn and msisdn range separated
     *
     * @return The result of the process in a Map
     *
     * @throws SimpayException If any exception happens
     * @throws IOException If file does not exist
     */
    public List readFilesFromTemporaryDirectory(String rutaConsultaDnisInput) throws Exception {
        
        List dniAlumnos = new ArrayList();
        
        try {
            
            String fileName = null;
            File dniFilePath = new File(rutaConsultaDnisInput);                       
            
            if (dniFilePath.exists()) {
                log.debug("Folder exist");
            } else {
                log.debug("Folder does NOT exist");
            }
            
            String[] fileList;
            
            if (dniFilePath.isDirectory()) {
                fileList =dniFilePath.list();                
                
                /*for (int i = 0; i < fileList.length; i++) {
                    fileName = fileList[i];                    
                }*/
                
                if ((fileList != null) && (fileList.length > 0)) {                    
                    for (int i = 0; i < fileList.length; i++) {
                        fileName = fileList[i];
                        //File outputFile = new File(getDniFileOutputPath(fileName));
                        dniAlumnos.addAll(fileToList(rutaConsultaDnisInput + File.separator + fileName));                                                        
                    }
                }
            } else {
                throw new Exception("readMsisdnFile: There is not exists msisdn file");
            }
        
        } catch (Exception e) {
            log.error("", e);
            
            throw e;
        }
                
        return dniAlumnos;
    }

    /**
     * Map has single msisdn and msisdn range separated
     *
     * @return The result of the process in a Map
     *
     * @throws SimpayException If any exception happens
     * @throws IOException If file does not exist
     */
    public List readFilesFromReplicacion(String rutaConsultaDnisInput) throws Exception {
        
        List dniAlumnos = new ArrayList();
        
        try {
            
            String fileName = null;
            File dniFilePath = new File(rutaConsultaDnisInput);                       
            
            if (dniFilePath.exists()) {
                log.debug("Folder exist");
            } else {
                log.debug("Folder does NOT exist");
            }
            
            String[] fileList;
            
            if (dniFilePath.isDirectory()) {
                dniAlumnos.addAll(fileToList(rutaConsultaDnisInput + File.separator + "replicacion.txt"));                                                        
            } else {
                throw new Exception("No existe el fichero replicacion.txt en el directorio: " + rutaConsultaDnisInput);
            }
        
        } catch (Exception e) {
            log.error("", e);
            
            throw e;
        }
                
        return dniAlumnos;
    }
    
    
    /**
     * Gets the output msisdn file path
     *
     * @param fileName File name
     *
     * @return Msisdn File path
     */
    private String getDniFileOutputPath(String fileName) {
        StringTokenizer st = new StringTokenizer(fileName, ".");
        
        StringBuffer sb = new StringBuffer();
        
        //File Path
        sb.append(getFileOutputPath());
        
        sb.append(File.separator);
        
        //File name
        sb.append(st.nextToken());
        
        //name and date separator
        sb.append("_");
        
        //Save date
        //sb.append(new Date());
        sb.append(format(new Date(), "yyyy-MM-dd"));

        
        //add point
        sb.append(".");
        
        //file extension
        sb.append(st.nextToken());
        
        log.debug("getMsisdnFileOutputPath: " + sb.toString());
        
        return sb.toString();
    }
    
    public static String format(Date date, String pattern) {
		return getDateFormat(pattern).format(date);
	}
    
    private static DateFormat getDateFormat(String pattern) {

		DateFormat df = null;

		if(!dfMap.containsKey(pattern)) {
			dfMap.put(pattern, new SimpleDateFormat(pattern));
	        }

		return (DateFormat) dfMap.get(pattern);
	}

    
    private String getFileOutputPath() {
        String fileOutputPath = "e:/my documents/desktop/procesado";
        
        log.debug("getFileOutputPath: " + fileOutputPath);
        
        return fileOutputPath;
    }
    
    /**
     * Read msisdn input file and put this elements in a List
     *
     * @param inputFilePath path
     *
     * @return Msisdn string list
     *
     * @throws IOException If file does not found
     * @throws NoSuchElementException In range is not well formed
     */
    private List fileToList(String inputFilePath) throws Exception, IOException {
        log.debug("Begining readMsisdnFile....");
        
        List dniList = new ArrayList();
        
        log.debug("DNI file path: " + inputFilePath);
        
        String buffer = FileUtils.readFileToString(new File(inputFilePath), "UTF-8");

        StringTokenizer strTk2 = new StringTokenizer(buffer);//,// new String(p));//this.msisdnSeparator);

        while (strTk2.hasMoreTokens()) {
          //   msisdnList.add(schemeParticipant.getString(SimpayCoreConstants.SV_SPAIN_PREFIX)+strTk2.nextToken());
             dniList.add(strTk2.nextToken());           
        }
        
        log.debug("Finished readMsisdnFile....");
        
        return dniList;
    }
    
    /**
     * Gets the input msisdn file path
     *
     * @param fileName File name
     *
     * @return Msisdn File path
     */
    private String getFileInputPath(String fileName) {
        return (getFileInputPath() +File.separator+ fileName);
    }
    
    /**
     * Gets the msisdn input directory path
     *
     * @return Input directory path
     */
    private String getFileInputPath() {
        String inputFilePath = "E:/My Documents/NetBeansProjects/ITCard/ProcesoInput";
        
        log.debug("getFileInputPath: " + inputFilePath);
        
        return inputFilePath;
    }

    
    public void deleteFilesFromDirectory(File filePath) throws Exception {
        log.debug("deleteFilesFromTemporalDirectory started");
        
        try {
            if (filePath.isDirectory()) {
                String[] fileList = filePath.list();
                
                if ((fileList != null) && (fileList.length > 0)) {
                    String fileName = null;
                    for (int i = 0; i < fileList.length; i++) {
                        fileName = fileList[i];
                        
                        boolean isDeleted = deleteFile(filePath);
                        
                    }
                }
            } else {
                throw new Exception("readMsisdnFile: There is not exists msisdn file");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("IOException: " + e.getMessage(), e);
            
            throw new Exception(e.getMessage());
        }
    }
    
    /**
     * Delete file
     *
     * @param filePath File path
     *
     * @return true If file has been deleted
     */
    public boolean deleteFile(File filePath) {
        boolean isDeleted = false;
        
        try {
            FileUtils.forceDelete(filePath);
            isDeleted = true;
        } catch (IOException ioex) {
            log.error("", ioex);
        }
        
        return isDeleted;
    }


/**
     * Copy msisdn input file in Process directory
     *
     * @throws SimpayException If file not found
     * @throws IOException If an I/O error happened
     */
    public void copyFilesToProcessedDir() throws Exception {
        try {
            
            String fileName = null;
            File filePath = new File(getFileInputPath());
            String[] fileList;
            
            if (filePath.isDirectory()) {
                fileList = filePath.list();
                
                if ((fileList != null) && (fileList.length > 0)) {
                    for (int i = 0; i < fileList.length; i++) {
                        fileName = fileList[i];
                        FileUtils.copyFile(new File(getFileInputPath(fileName)), new File(getDniFileOutputPath(fileName)));
                    }
                }
            } else {
                throw new IOException("readMsisdnFile: There is not exists msisdn file");
            }
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage(), e);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    /**
     * Compara que la fecha1 sea igual que la fecha2
     *
     * @param String fecha1,String fecha2
     *
     * @return true Si la fecha es la misma
     */
    
    /*public boolean compararFechas(String fecha1,String fecha2){
    
        boolean resultado;
        
       if( fecha1!=null && fecha2!=null && !fecha1.equals("") && !fecha2.equals("")){
           try {
               
                Date dateFin = dfPantalla.parse(getF_fin());
                GregorianCalendar gc = new GregorianCalendar();
                Date dateHoy = dfPantalla.parse(gc.getTime().toString());
                long dif = dateFin.getTime() - dateHoy.getTime();
                
                if (dif<0){
                   resultado=false;
                }else{
                   resultado=true;
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                ex.printStackTrace();
            } 
           
           return resultado;
       }
        
    }*/
    
    //FUNCIONES AÑADIDAS EL 10 DE DICIEMBRE PARA LA VALIDACIÓN DE NIFS
    
    /**
        * MÃ©todo que valida el formato de un NIF y lo formatea rellenandolo con ceros y guion
        * @param nif valor del campo del formulario
        * @param mensaje string que se muestra en caso de error
        * @return true o false, dependiendo de si el campo cumple todos los requisitos o no
        */
        public static boolean validaNif(String nif)
        {
                
            
                // quito el guion si lo hay
                String nif_sin_guion = nif.replaceAll("-","");
                //quito espacios en blanco
                nif = nif_sin_guion.trim();
                //calculo la longitud
                int longitud_nif = nif.length();
                String expresion="[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]";
                Pattern patron = null;
                Matcher coincidencias_inicio = null;
                Matcher coincidencias_fin = null;
                patron = Pattern.compile(expresion);
                String inicio=nif.substring(0,1);
                String fin=nif.substring(longitud_nif - 1);
                coincidencias_inicio = patron.matcher(inicio);
                coincidencias_fin = patron.matcher(fin);
                
                
                
                /**************** nif x9999999-x y empieza por X o L ***************/
                if ((nif.startsWith("X") || nif.startsWith("x") || nif.startsWith("L") || nif.startsWith("l") ) && coincidencias_fin.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //saco la letra izquerda
                        String primera_letra = nif.substring(0,1);
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 primera_letra = primera_letra + "0";
                        }
                        //quito la primera letra
                        String nif_sin_letra_primera = nif.substring(1);
                        //quito la ultima letra
                        String nif_sin_letra_ultima = nif_sin_letra_primera.substring(0,longitud_nif-2);
                        String nif_final = primera_letra.toUpperCase() + nif_sin_letra_ultima + "-" + nif.substring(longitud_nif-1).toUpperCase();

                        // si llamas al mÃ©todo set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        
                        return true;
                }//cierra if primer caso
                /************** nif x-9999999 ************************/
                else if (coincidencias_inicio.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //saco la letra izquerda
                        String primera_letra = nif.substring(0,1);
                        //quito la primera letra
                        String nif_sin_letra_primera = nif.substring(1);
                        String nif_con_ceros="";
                        //rellena ceros
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 nif_sin_letra_primera =  "0"+nif_sin_letra_primera;
                        }
                        String nif_final = primera_letra.toUpperCase() +"-"+ nif_sin_letra_primera;
                        // si llamas al mÃ©todo set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        
                        return true;
                }
                /************** nif 9999999-x ************************/
                else if (coincidencias_fin.find())
                {
                        //calcula numero de ceros a incluir
                        int num_ceros_relleno = 9 - longitud_nif;
                        //quito la ultima letra
                        String nif_sin_letra_ultima = nif.substring(0,longitud_nif-1);
                        //saco la ultima letra
                        String ultima_letra = nif.substring(longitud_nif-1);
                        for (int i=0;i<num_ceros_relleno ;i++ )
                        {
                                 nif_sin_letra_ultima =  "0"+nif_sin_letra_ultima;
                        }
                        String nif_final =  nif_sin_letra_ultima +"-" +ultima_letra.toUpperCase();
                        // si llamas al mÃ©todo set del atributo del nif original lo sustituye  por el nuevo nif formateado
                        //Ej: setDocOriginal(nif_final);
                        
                        return true;
                }
                else
                        //setMensajeError(mensaje+".formato");
                        
                        return 	false;
        }//fin metodo valida nif


        /**
        * MÃ©todo que comprueba que el nif exista
        * @param nif dato recogido por el formulario en campo nif
        * @param string que se va a devolver en caso de error
        * @return boolean En funciÃ³n del resultado, retorna true si se cumple o false si la comprobaciÃ³n es incorrecta
        */
        public static boolean compruebaNif(String nif) throws NumberFormatException{
            
                
                //letras correctas para un nif
                String caracteres = "TRWAGMYFPDXBNJZSQVHLCKE";
                // quito el guion si lo hay
                String nif_sin_guion = nif.replaceAll("-","");
                //quito espacios en blanco
                nif = nif_sin_guion.trim();
                //calculo la longitud
                int longitud_nif = nif.length();
                String expresion="[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]";
                Pattern patron = null;
                Matcher coincidencias_inicio = null;
                Matcher coincidencias_fin = null;
                patron = Pattern.compile(expresion);
                String inicio=nif.substring(0,1);
                String fin=nif.substring(longitud_nif - 1);
                coincidencias_inicio = patron.matcher(inicio);
                coincidencias_fin = patron.matcher(fin);
                
        try{
                
                /**************** nif x9999999-x y empieza por X o L ***************/
        if ((nif.startsWith("X") || nif.startsWith("x") || nif.startsWith("L") || nif.startsWith("l") ) && coincidencias_fin.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(1,longitud_nif-1);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(fin) == 0){
                      
                      return true;
                }else{
                //setMensajeError(mensaje+".error");
                    
                return false;}
        }
        /************** nif x-9999999 ************************/
        else if (coincidencias_inicio.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(1,longitud_nif);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(inicio) == 0){
                       
                    return true;
                }else{
                //setMensajeError(mensaje+".error");
                 
                return false;}
        }
        /************** nif 9999999-x ************************/
        else if (coincidencias_fin.find()){
                //guardo el nif sin la letra
                String nif1 = nif.substring(0,longitud_nif-1);
                //algoritmo
                int nif2 =  Integer.parseInt(nif1);
                int posicion = (nif2 % 23)+1;
                String caracter = caracteres.substring(posicion-1,posicion);
                if (caracter.compareToIgnoreCase(fin) == 0){
                     
                    return true;
                }else{
                    
                //setMensajeError(mensaje+".error");
                return false;}
        }
                //setMensajeError(mensaje+".error");
                
                return false;
                
        }catch(Exception e){
            return false;
        }
                
                
        }//fin metodo comprueba nif

        
    public static String sustituirSimbolos(String cadena){
        String cadenaCambiada=cadena;
        
        String acentoA="á";//&aacute;
        String acentoE="é";//&eacute;
        String acentoI="í";//&iacute;
        String acentoO="ó";//&oacute;
        String acentoU="ú";//&uacute;
        
        String acentoAMay="Á";//&Aacute;
        String acentoEMay="É";//&Eacute;
        String acentoIMay="Í";//&Iacute;
        String acentoOMay="Ó";//&Oacute;
        String acentoUMay="Ú";//&Uacute;
        
        String enie="ñ";//&ntilde;
        String enie2="Ñ";//&amp;ntilde;
        String enie2May="ñ";//&amp;Ntilde;
        String enieMay="Ñ";//&Ntilde;
        
        String orden2="º";//&deg;
        String ordenmasc="ª";//&ordm;
        String ordenfem="º";//&ordf;
        
        String dobleComilla="'";//&quot;
        String nulo="nulo";//nulo
        
        if(cadenaCambiada.indexOf(acentoA)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoA, "&aacute;");
        }
        if(cadenaCambiada.indexOf(acentoE)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoE, "&eacute;");
        }
        if(cadenaCambiada.indexOf(acentoI)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoI, "&iacute;");
        }
        if(cadenaCambiada.indexOf(acentoO)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoO, "&oacute;");
        }
        if(cadenaCambiada.indexOf(acentoU)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoU, "&uacute;");
        }

        if(cadenaCambiada.indexOf(acentoAMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoAMay, "&Aacute;");
        }
        if(cadenaCambiada.indexOf(acentoEMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoEMay, "&Eacute;");
        }
        if(cadenaCambiada.indexOf(acentoIMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoIMay, "&Iacute;");
        }
        if(cadenaCambiada.indexOf(acentoOMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoOMay, "&Oacute;");
        }
        if(cadenaCambiada.indexOf(acentoUMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(acentoUMay, "&Uacute;");
        }

        if(cadenaCambiada.indexOf(enie)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(enie, "&ntilde;");
        }
        if(cadenaCambiada.indexOf(enieMay)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(enieMay, "&amp;ntilde;");
        }
        if(cadenaCambiada.indexOf(enie2)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(enie2, "&amp;Ntilde;");
        }
        if(cadenaCambiada.indexOf(enie2May)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(enie2May, "&Ntilde;");
        }

        if(cadenaCambiada.indexOf(ordenmasc)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(ordenmasc, "&deg;");
        }
        if(cadenaCambiada.indexOf(ordenfem)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(ordenfem, "&ordm;");
        }
        if(cadenaCambiada.indexOf(orden2)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(orden2, "&ordf;");
        }

        if(cadenaCambiada.indexOf(dobleComilla)!=-1){
            cadenaCambiada=cadenaCambiada.replaceAll(dobleComilla, "&quot;");
        }

        if(cadenaCambiada.equals(nulo)){
            cadenaCambiada="";
        }
        
        
        return cadenaCambiada;
    }

       /*Método que valida el formato de las fechas
     *@param fecha valor de fecha introducido
     *@param cadena error que envia si no es correcto
     *@param obligatorio indica si el campo es o no obligatorio
     *@return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
     */
    public static boolean validaFechaEuskera(String fecha) {
        String campo = fecha.trim();

        if (campo.length() > 0) {
            if (campo.length() != 10) {
                return false;
            }
            //saca de la fecha dia, mes y año
            String[] fech1 = campo.split("[/]");
            //comprueba que haya introducido el formato dd/mm/yyyy
            if (fech1.length < 3) {
                return false;
            }
            int dia = new Integer(fech1[2]).intValue();
            int mes = new Integer(fech1[1]).intValue();
            int anio = new Integer(fech1[0]).intValue();
            //el mes debe estar entre 1 y 12
            if (mes < 1 || mes > 12) {
                return false;
            }
            //comprueba que el año este entre 1900 y 2099
            if (anio < 1900 || anio > 2099) {
                return false;
            }
            //comprueba el numero de dias dependiendo del mes
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                if (dia <= 0 || dia > 31) {
                    return false;
                }
            }
            if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                if (dia <= 0 || dia > 30) {
                    return false;
                }
            }
            if (mes == 2) {
                if (anio % 4 > 0) {
                    if (dia > 28) {
                        return false;
                    }
                } else if (anio % 100 == 0 && anio % 400 > 0) {
                    if (dia > 28) {
                        return false;
                    }
                } else {
                    if (dia > 29) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

}
