package ejerciciojava;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecurityLogin {

    public static void sendEmail(String usuario) {
        // CREDENCIALES PARA ENVIAR MAILS A TRAVES DE SMTP
        final String username = "gestion@asadorsanchez.com";
        final String password = "0qkZ4ynGT1Mw5BQK";

        //CONFIG BREVO.COM
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp-relay.brevo.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        // Obtener la información de la IP del cliente
        String ipinfo = "";

        try {
            // Obtener la IP del cliente
            String clientIP = ""; // Aquí debes proporcionar la IP del cliente

            // Crear un objeto URL para la dirección del API de IPinfo
            URL url = new URL("https://ipinfo.io/" + clientIP + "?token=c56a587c06b950");

            // Crear una conexión HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configurar método de solicitud a GET
            conn.setRequestMethod("GET");

            // Obtener la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Cerrar el lector
            reader.close();

            // Obtener la información de la IP del cliente
            ipinfo = response.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("security@aparcamiento480.com")); //mail que aparecerá como remitente
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("antonio.soare@cuatroochenta.com") //mail del destinatario
            );
            message.setSubject("Alerta de seguridad user: " + usuario + " - APP Aparcamiento");

            // Formatear la información del cliente
            String formattedIpInfo = formatIpInfo(ipinfo);

            // Agregar el contenido al mensaje
            String content = "El usuario " + usuario + " ha sido bloqueado después de 3 intentos fallidos de inicio de sesión." +
                    "\nInformación del cliente:\n" + formattedIpInfo;
            message.setText(content);

            Transport.send(message);

            System.out.println("Sistema de seguridad activado. Hemos avisado a Sofistic para que tomen medidas.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static String formatIpInfo(String ipinfo) {
        StringBuilder sb = new StringBuilder();

        // Parsear el JSON de la información de la IP
        JSONObject json = new JSONObject(ipinfo);

        // Obtener los detalles de la IP
        String ip = json.getString("ip");
        String hostname = json.getString("hostname");
        String city = json.getString("city");
        String region = json.getString("region");
        String country = json.getString("country");
        String loc = json.getString("loc");
        String postal = json.getString("postal");
        String timezone = json.getString("timezone");

        // Obtener la información de la compañía ISP
        JSONObject companyJson = json.getJSONObject("company");
        String ispName = companyJson.getString("name");
        String ispDomain = companyJson.getString("domain");

        // Obtener la información de privacidad
        JSONObject privacyJson = json.getJSONObject("privacy");
        boolean isVPN = privacyJson.getBoolean("vpn");
        boolean isProxy = privacyJson.getBoolean("proxy");
        boolean isTor = privacyJson.getBoolean("tor");
        boolean isRelay = privacyJson.getBoolean("relay");
        boolean isHosting = privacyJson.getBoolean("hosting");

        // Agregar los detalles formateados al StringBuilder
        sb.append("IP: ").append(ip).append("\n")
                .append("Servidor: ").append(hostname).append("\n")
                .append("País: ").append(country).append("\n")
                .append("Provincia: ").append(region).append("\n")
                .append("Ciudad: ").append(city).append("\n")
                .append("Código Postal: ").append(postal).append("\n")
                .append("Coordenadas: ").append(loc).append("\n")
                .append("Link de Google Maps: ").append(getGoogleMapsLink(loc)).append("\n")
                .append("Nombre ISP: ").append(ispName).append("\n")
                .append("Link directo al ISP: ").append(ispDomain).append("\n")
                .append("¿Es un proxy? ").append(isEmoji(isProxy)).append("\n")
                .append("¿Es una VPN? ").append(isEmoji(isVPN)).append("\n")
                .append("¿Es Tor? ").append(isEmoji(isTor)).append("\n")
                .append("¿Es un Relay? ").append(isEmoji(isRelay)).append("\n")
                .append("¿Es Hosting? ").append(isEmoji(isHosting)).append("\n")
                .append("Tiempo Horario: ").append(timezone);

        return sb.toString();
    }

    private static String getGoogleMapsLink(String loc) {
        String[] coordinates = loc.split(",");
        String lat = coordinates[0];
        String lon = coordinates[1];
        return "https://maps.google.com/?q=" + lat + "," + lon;
    }

    private static String isEmoji(boolean value) {
        return value ? "✔️" : "❌";
    }
}
