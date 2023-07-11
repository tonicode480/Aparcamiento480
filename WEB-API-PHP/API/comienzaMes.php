<?php
include 'database.php';

// Eliminar las estancias de los coches oficiales
$query1 = "DELETE FROM estancias WHERE matricula IN (SELECT matricula FROM vehiculos WHERE tipo = 'oficial')";
$conn->query($query1);

// Poner a cero el tiempo estacionado de los vehículos de residentes
$query2 = "UPDATE vehiculos SET tiempoEstacionado = 0 WHERE tipo = 'residente'";
$conn->query($query2);

echo "Operación 'Comienza mes' realizada con éxito.";
?>
