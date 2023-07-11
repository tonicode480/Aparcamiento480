<?php

$filename = 'pagosResidentes.csv';

if (file_exists($filename)) {
    // Enviar headers
    header('Content-Description: File Transfer');
    header('Content-Type: text/plain');
    header('Content-Disposition: attachment; filename="'.basename($filename).'"');
    header('Expires: 0');
    header('Cache-Control: must-revalidate');
    header('Pragma: public');
    header('Content-Length: ' . filesize($filename));

    // Limpiar el buffer de salida y leer el archivo al cliente
    flush();
    readfile($filename);
    exit;
} else {
    http_response_code(404);
    echo "Archivo no encontrado.";
}
?>
