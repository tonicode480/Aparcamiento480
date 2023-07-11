package ejerciciojava;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
    private static Aparcamiento aparcamiento = new Aparcamiento();
    private static JFrame frame;

    public Main() {
        frame = new JFrame("Sistema de Aparcamiento by 480");
        frame.setSize(600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageUrl = new ImageIcon("src/img/AdminBackground.png");
                Image image = imageUrl.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);
        
        //ICONOS BOTONES
        ImageIcon entradaIcon = new ImageIcon("src/img/entradaIcon.png");
        ImageIcon salidaIcon = new ImageIcon("src/img/salidaIcon.png");
        ImageIcon altaOficialIcon = new ImageIcon("src/img/altaOficialIcon.png");
        ImageIcon altaResidenteIcon = new ImageIcon("src/img/altaResidenteIcon.png");
        ImageIcon comienzaMesIcon = new ImageIcon("src/img/comienzoMesIcon.png");
        ImageIcon pagosResidentesIcon = new ImageIcon("src/img/pagosResidentesIcon.png");
      

        JButton registrarEntradaButton = new JButton("Registrar entrada");
        registrarEntradaButton.setBounds(40, 85, 245, 67);
        registrarEntradaButton.setIcon(entradaIcon);
        registrarEntradaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricula = JOptionPane.showInputDialog("Introduce la matrícula del vehículo que entra:");

                try {
                    URL url = new URL("https://aparcamiento480.com/API/registrarEntrada.php");

                    HttpURLConnection http = (HttpURLConnection) url.openConnection();

                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String data = "matricula=" + URLEncoder.encode(matricula, "UTF-8");
                    try (OutputStream out = http.getOutputStream()) {
                        out.write(data.getBytes());
                    }

                    int responseCode = http.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Obtiene la respuesta del servidor
                        BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        // Cierra las conexiones
                        in.close();
                        http.disconnect();
                        
                        // Muestra la respuesta en la consola o donde prefieras
                        System.out.println("Respuesta del servidor: " + content.toString());
                        
                        JOptionPane.showMessageDialog(null, "Entrada registrada con éxito!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar la entrada: " + responseCode);
                    }

                    http.disconnect();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        panel.add(registrarEntradaButton);



        
        JButton registrarSalidaButton = new JButton("Registrar salida");
        registrarSalidaButton.setBounds(40, 155, 245, 67);
        registrarSalidaButton.setIcon(salidaIcon);
        registrarSalidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricula = JOptionPane.showInputDialog("Introduce la matrícula del vehículo que sale:");

                try {
                    // Crear un objeto URL para la dirección del API
                    URL url = new URL("https://aparcamiento480.com/API/consultarTipoVehiculo.php");

                    // Crear una conexión HttpURLConnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // Configurar método de solicitud a POST
                    conn.setRequestMethod("POST");

                    // Permitir entrada y salida de datos
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Definir parámetros para el POST
                    String data = "matricula=" + URLEncoder.encode(matricula, StandardCharsets.UTF_8);

                    // Escribir los datos en la salida de la conexión
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());

                    // Obtener código de respuesta del servidor
                    int responseCode = conn.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String tipoVehiculo = reader.readLine();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String apiURL;
                        switch (tipoVehiculo) {
                            case "oficial":
                                apiURL = "https://aparcamiento480.com/API/registrarSalidaOficial.php";
                                break;
                            case "residente":
                                apiURL = "https://aparcamiento480.com/API/registrarSalidaResidente.php";
                                break;
                            case "no residente":
                                apiURL = "https://aparcamiento480.com/API/registrarSalidaNoResidente.php";
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Tipo de vehículo desconocido: " + tipoVehiculo);
                                return;
                        }

                        URL salidaUrl = new URL(apiURL);
                        HttpURLConnection salidaConn = (HttpURLConnection) salidaUrl.openConnection();
                        salidaConn.setRequestMethod("POST");
                        salidaConn.setDoInput(true);
                        salidaConn.setDoOutput(true);

                        OutputStream salidaOut = salidaConn.getOutputStream();
                        salidaOut.write(data.getBytes());

                        int salidaResponseCode = salidaConn.getResponseCode();

                        BufferedReader readerSalida = new BufferedReader(new InputStreamReader(salidaConn.getInputStream()));
                        StringBuilder respuestaSalida = new StringBuilder();
                        String linea;
                        while ((linea = readerSalida.readLine()) != null) {
                            respuestaSalida.append(linea);
                        }
                        JOptionPane.showMessageDialog(null, respuestaSalida.toString());
                        System.out.println(respuestaSalida.toString());


                    } else {
                        JOptionPane.showMessageDialog(null, "Error al consultar el tipo de vehículo: " + conn.getResponseMessage());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(registrarSalidaButton);


        
        
        JButton altaOficial = new JButton(" Alta vehiculo oficial");
        altaOficial.setBounds(300, 85, 245, 67);
        altaOficial.setIcon(altaOficialIcon);
        altaOficial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricula = JOptionPane.showInputDialog("Introduce la matrícula del vehículo oficial:");

                // Crear un objeto URL para la dirección del API
                URL url = null;
                try {
                    url = new URL("https://aparcamiento480.com/API/insertarVehiculo.php");
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                }

                // Crear una conexión HttpURLConnection
                HttpURLConnection conn = null;
                try {
                    assert url != null;
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // Configurar método de solicitud a POST
                try {
                    assert conn != null;
                    conn.setRequestMethod("POST");
                } catch (ProtocolException protocolException) {
                    protocolException.printStackTrace();
                }

                // Permitir entrada y salida de datos
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Definir parámetros para el POST
                String data = "matricula=" + URLEncoder.encode(matricula, StandardCharsets.UTF_8) + "&tipo=oficial";

                // Escribir los datos en la salida de la conexión
                try {
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // Obtener código de respuesta del servidor
                try {
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Vehículo oficial registrado con éxito!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar el vehículo: " + conn.getResponseMessage());
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        panel.add(altaOficial);

        
        
        JButton altaResidente = new JButton(" Alta vehiculo residente");
        altaResidente.setBounds(300, 155, 245, 67);
        altaResidente.setIcon(altaResidenteIcon);
        altaResidente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricula = JOptionPane.showInputDialog("Introduce la matrícula del vehículo que sale:");

                // Crear un objeto URL para la dirección del API
                URL url = null;
                try {
                    url = new URL("https://aparcamiento480.com/API/insertarVehiculo.php");
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                }

                // Crear una conexión HttpURLConnection
                HttpURLConnection conn = null;
                try {
                    assert url != null;
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // Configurar método de solicitud a POST
                try {
                    assert conn != null;
                    conn.setRequestMethod("POST");
                } catch (ProtocolException protocolException) {
                    protocolException.printStackTrace();
                }

                // Permitir entrada y salida de datos
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Definir parámetros para el POST
                String data = "matricula=" + URLEncoder.encode(matricula, StandardCharsets.UTF_8) + "&tipo=residente";

                // Escribir los datos en la salida de la conexión
                try {
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // Obtener código de respuesta del servidor
                try {
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Vehículo residente registrado con éxito!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar el vehículo: " + conn.getResponseMessage());
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        panel.add(altaResidente);

        
        
        
        JButton comienzaMes = new JButton(" Comienza mes");
        comienzaMes.setBounds(40, 225, 245, 67);
        comienzaMes.setIcon(comienzaMesIcon);
        comienzaMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URL url = new URL("https://aparcamiento480.com/API/comienzaMes.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Operación 'Comienza mes' realizada con éxito!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al realizar la operación 'Comienza mes'. Código de error: " + responseCode);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(comienzaMes);

        
        
        
        JButton pagosResidentes = new JButton(" Pagos de residentes");
        pagosResidentes.setBounds(300, 225, 245, 67);
        pagosResidentes.setIcon(pagosResidentesIcon);
        pagosResidentes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("Introduce el nombre del fichero en el que quieres generar el informe:");

                try {
                    // Generar el informe
                    URL urlGenerate = new URL("https://aparcamiento480.com/API/generarInformePagos.php");
                    HttpURLConnection connGenerate = (HttpURLConnection) urlGenerate.openConnection();
                    connGenerate.setRequestMethod("GET");

                    int responseCodeGenerate = connGenerate.getResponseCode();
                    if (responseCodeGenerate == HttpURLConnection.HTTP_OK) {
                        // Descargar el informe
                        URL urlDownload = new URL("https://aparcamiento480.com/API/downloadInformePagos.php");
                        HttpURLConnection connDownload = (HttpURLConnection) urlDownload.openConnection();
                        connDownload.setRequestMethod("GET");

                        // Leer la respuesta en un stream
                        InputStream in = connDownload.getInputStream();

                        // Escribir el stream en un archivo
                        FileOutputStream fileOutputStream = new FileOutputStream(fileName + ".csv");

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }

                        fileOutputStream.close();
                        in.close();

                        JOptionPane.showMessageDialog(null, "Informe generado con éxito!");

                    } else {
                        JOptionPane.showMessageDialog(null, "Error al generar el informe. Código de error: " + responseCodeGenerate);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(pagosResidentes);

        
        
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login(); // Login primero
            }
        });
    }
}

