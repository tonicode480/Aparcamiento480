from flask import Flask, request
from twilio.twiml.voice_response import VoiceResponse
from twilio.rest import Client
import requests
import urllib.parse
import urllib.request

# Credenciales de Twilio | Quitadas por seguridad
account_sid = ''
auth_token = ''
twilio_phone_number = '+34'  

client = Client(account_sid, auth_token)

app = Flask(__name__)
nueva_contrasena = ''  # Variable global para almacenar la nueva contraseña

@app.route('/llamada', methods=['POST'])
def llamada():
    global nueva_contrasena  # Acceder a la variable global

    numero = request.form.get('numero')

    # Cambiar la contraseña del usuario
    password_changer_url = "http://212.227.227.56/API/passwordChanger.php?numero=" + urllib.parse.quote(numero)
    print("URL de cambio de contraseña:", password_changer_url)
    password_changer_request = urllib.request.Request(password_changer_url)
    password_changer_response = urllib.request.urlopen(password_changer_request)
    print("Respuesta de cambio de contraseña:", password_changer_response.read().decode('utf-8'))

    # Obtén la nueva contraseña
    get_password_url = "http://212.227.227.56/API/getNewPassword.php?numero=" + urllib.parse.quote(numero)
    print("URL de obtención de contraseña:", get_password_url)
    get_password_request = urllib.request.Request(get_password_url)
    get_password_response = urllib.request.urlopen(get_password_request)
    nueva_contrasena = get_password_response.read().decode('utf-8')
    print("Nueva contraseña:", nueva_contrasena)

    # Genera la respuesta TwiML
    response = VoiceResponse()
    response.say('Hola, soy la robot de CuatroOchenta. Te ayudaré a restablecer tu contraseña... Coge un boli y un papel porque en 5 segundos te diré la nueva contraseña.', voice='Polly.Lucia-Neural')
    response.pause(length=3)
    response.say('Tu nueva contraseña es ' + nueva_contrasena + '. Te la volveré a repetir por última vez en 5 segundos.', voice='Polly.Lucia-Neural')
    response.pause(length=2)
    response.say('Tu nueva contraseña es ' + nueva_contrasena, voice='Polly.Lucia-Neural')
    response.pause(length=1)
    response.say('Jano, recuerda que esta contraseña es temporal, desde Sofístic te recomendamos que generes una nueva con Keeper. Un saludo.', voice='Polly.Lucia-Neural')

    call = client.calls.create(
        twiml=str(response),
        to=numero,
        from_=twilio_phone_number
    )

    return 'Llamada realizada con éxito', 200

if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0', port=7777)
