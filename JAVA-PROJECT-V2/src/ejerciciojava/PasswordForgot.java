package ejerciciojava;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

public class PasswordForgot {

    public static void requestPasswordReset(String username) {
        try {
            String urlString = "https://aparcamiento480.com/API/checkPhone.php";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            String urlParameters = "user=" + username;
            conn.getOutputStream().write(urlParameters.getBytes("UTF-8"));

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
            	JOptionPane.showMessageDialog(null, "Usted ha solicitado cambiar su contraseña. El proceso es sencillo.\n\n1- Le llamaremos a su número desde +34 964 80 02 12.\n2- Conteste la llamada y el robot le dirá una nueva contraseña.\n3- Apúntatela y no olvides cambiarla por una contraseña más fuerte.\n\nCuando estés listo apreta el botón de Aceptar y te llamaremos en unos segundos.");

                String urlStringTwilio = "http://212.227.227.56:7777/llamada";

                URL urlTwilio = new URL(urlStringTwilio);
                HttpURLConnection connTwilio = (HttpURLConnection) urlTwilio.openConnection();

                connTwilio.setDoOutput(true);
                connTwilio.setRequestMethod("POST");

                String phoneNumber = new String(conn.getInputStream().readAllBytes());
                String urlParametersTwilio = "numero=" + phoneNumber;
                connTwilio.getOutputStream().write(urlParametersTwilio.getBytes("UTF-8"));
                
                if (connTwilio.getResponseCode() == 200) {
                    System.out.println("¡Iniciamos el proceso del cambio de contraseña! En breves segundos te llamaremos al número personal que tienes dentro de nuestra base de datos. Te diremos la nueva contraseña por llamada.");
                } else {
                    System.out.println("Error al realizar la llamada: " + connTwilio.getResponseMessage());
                }
            } else {
                System.out.println("Error: " + conn.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
