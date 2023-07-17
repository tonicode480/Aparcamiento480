package ejerciciojava;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.*;

public class Login {
    private JFrame frame;
    private JTextField userText;
    private JPasswordField passwordText;
    private JButton loginButton;
    private int failedAttempts = 0;
    private boolean userBlocked = false;

    public Login() {
        frame = new JFrame("APP Aparcamiento - Designed and Programmed by Toni");
        frame.setSize(600, 800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(createLoginPanel());
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(null);

        // Carga la imagen de fondo
        ImageIcon backgroundIcon = new ImageIcon("src/img/LoginBackground.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        loginPanel.add(backgroundLabel);

        JLabel usernameLabel = new JLabel("Usuario");
        userText = new JTextField(20);
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBounds(200, 265, 200, 30);
        userText.setBounds(200, 295, 200, 30);
        backgroundLabel.add(usernameLabel);
        backgroundLabel.add(userText);

        JLabel passwordLabel = new JLabel("Contraseña");
        passwordText = new JPasswordField(20);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(200, 330, 200, 30);
        passwordText.setBounds(200, 360, 200, 30);
        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(passwordText);
        
        JButton forgotPasswordButton = new JButton(" ¡He olvidado mi contraseña!");
        ImageIcon forgotIcon = new ImageIcon("src/img/forgotIcon.png");
        forgotPasswordButton.setIcon(forgotIcon);
        forgotPasswordButton.setBounds(290, 410, 250, 34);
        backgroundLabel.add(forgotPasswordButton);

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = JOptionPane.showInputDialog(frame, "Introduce tu nombre de usuario:");
                if (usuario != null && !usuario.isEmpty()) {
                    PasswordForgot.requestPasswordReset(usuario);
                }
            }
        });


        loginButton = new JButton(" Iniciar sesión");
        ImageIcon loginIcon = new ImageIcon("src/img/loginIcon.png");
        loginButton.setIcon(loginIcon);
        loginButton.setBounds(73, 410, 220, 34);
        backgroundLabel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = userText.getText();
                String contraseña = new String(passwordText.getPassword());
                performLogin(usuario, contraseña);
            }
        });

        return loginPanel;
    }

    private void performLogin(String usuario, String contraseña) {
        try {
            // Primero, verificamos si el usuario está bloqueado
            URL checkStatusUrl = new URL("https://aparcamiento480.com/API/checkStatus.php");
            HttpURLConnection checkStatusConn = (HttpURLConnection) checkStatusUrl.openConnection();
            checkStatusConn.setRequestMethod("POST");
            checkStatusConn.setDoOutput(true);
            checkStatusConn.setDoInput(true);

            // Pasamos los datos POST
            OutputStream checkStatusOs = checkStatusConn.getOutputStream();
            BufferedWriter checkStatusWriter = new BufferedWriter(new OutputStreamWriter(checkStatusOs, "UTF-8"));
            String checkStatusData = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8");
            checkStatusWriter.write(checkStatusData);
            checkStatusWriter.flush();
            checkStatusWriter.close();
            checkStatusOs.close();

            // Si la respuesta es HTTP_OK y el usuario está bloqueado
            if (checkStatusConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(checkStatusConn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.getString("status").equals("blocked")) {
                    JOptionPane.showMessageDialog(frame, "Sofistic Cyber Security Team - REF 70\n \nHemos bloqueado su aplicación por seguridad. \nContacte con nosotros en info@sofistic.com para concertar una cita física en nuestras oficinas.");
                    return;
                }
            }

            // Después de comprobar el estado del usuario, procedemos a verificar las credenciales de inicio de sesión
            URL url = new URL("https://aparcamiento480.com/API/checkLogin.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Pasamos los datos POST
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(contraseña, "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

            // Si la respuesta es HTTP_OK
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Acceso concedido
                frame.dispose();
                new Main();
            } else {
                // Acceso denegado
                BufferedReader br;
                if (conn.getResponseCode() == 401 || conn.getResponseCode() == 400 || conn.getResponseCode() == 500) {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                String line;
                StringBuilder responseError = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseError.append(line);
                }
                JSONObject jsonResponse = new JSONObject(responseError.toString());
                JOptionPane.showMessageDialog(frame, jsonResponse.getString("message"));
                
                // Incrementar el número de intentos fallidos
                failedAttempts++;
                if (failedAttempts >= 3) {
                    // Bloquear al usuario y enviar mail de aviso a Sofistic
                	SecurityLogin.sendEmail(usuario);
                    blockUser(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void blockUser(String usuario) {
        try {
            String blockUserUrl = "https://aparcamiento480.com/API/blockUser.php?user=" + URLEncoder.encode(usuario, "UTF-8");
            HttpURLConnection conn = (HttpURLConnection) new URL(blockUserUrl).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(frame, "Sofistic Cyber Security Team - ERROR CODE 70\nHemos detectado una actividad inusual y hemos bloqueado su aplicación por seguridad.\nTambién hemos enviado un correo a Sofistic para avisar de la incidencia.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error al bloquear el usuario");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

