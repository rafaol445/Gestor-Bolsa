# PE1 - Gestor de Patrimonio

Proyecto ClientPE1: aplicacion cliente que permite conectar con el servidor
y lanza las peticiones al servidor.

Proyecto PE1: aplicacion servidor que recibe peticiones del cliente y las gestiona

Proyecto EmailSender: Libreria encargada de lanzar los correos a los usuarios
del servicio.

## Comentarios 📖
* PE1 (Server): para la parte de la base de datos de los clientes esta trabajado
con sistema Json con la libreria Gson que se guardan en el fichero "users.json"
en el repositorio tambien esta dicho archivo con mi usuario para poder realizar pruebas.
la contraseña se guarda cifrada en el archivo "password.txt".

* ClientPE1 (Cliente) Para la parte del cliente se implementa un bucle infinito
para lanzar peticiones al servidor hasta que se introduzca la palabra salir la cual
no finalizara la conexion con el servidor
para que se pueda salir he consultado esta documentacion de oracle https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html 

## Comenzando 🚀

Cuando se descargue el repositorio con la solucion, se debe hacer un
clean install para descargar las librerias con maven.

## Construido con 🛠️

* [EmailSender] - Libreria propia.
* [Maven](https://maven.apache.org/) - Manejador de dependencias.
* [Gson](https://sites.google.com/site/gson/gson-user-guide) - Dependencia para Json.
* [Jasypt](http://www.jasypt.org/) - Libreria para encriptar.
* [Apache commons](https://commons.apache.org/) - Libreria para realizar el envio de mails.

## Autores ✒️

* **Rafael Olaya** - *Trabajo Inicial* - [Rafaol445](https://github.com/rafaol445/2PSP.git)

## Licencia 📄

Este proyecto está bajo la Licencia (OSS).
