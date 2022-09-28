# Parquímetros 2020

## Workflow 

Recuerde que los pasos para trabajar desde consola con git son los siguientes:

1) Ir a la carpeta donde está el repositorio, la misma se encusentra dentro de la carpeta donde realizó el **git clone**
2) **git pull origin**: Actualizar el respositorio con la versión más reciente del repositorio remoto.
3) Realizar los cambios que considera necesario. En este caso simplemente modifique el archivo README.md y cambie una de las lineas que dicen Apellido y nombre con sus datos personales.
4) **git add .**: Para agregar los cambios realizados y preparar el commit. Este paso puede repetirse tantas veces como lo necesite antes de realizar el commit. Observe que luego de add hay un punto.
5) **git commit -m "MENSAJE"**: Actualizar el repositorio local con todo lo que fue modificado. Reemplace MENSAJE con una descripción de lo que representan esos cambios para el proyecto, por ejemplo, si soluciona un bug podría decir "Arreglo del error mencionado en el ticket #132 que permitia entrar al sistema sin login".
6) **git push origin**: Para subir los cambios al repositorio remoto. 

Recuerde que si realiza con frecuencia una actualización del repositorio remoto, es menos probable que tenga que solucionar conflictos en archivos ya que tanto el repositorio local como el remoto estarán sincronizados.

## Para realizar una Entrega (Release)

1) Hacer click en Releases que se encuentra en la parte derecha de la ventana del repositorio en github
2) Si no hay releases creados, apretar el botón verde "Create a new release"
   en el caso que ya existan releases asignar un nuevo tag (etiqueta) para la nueva entrega.
3) En el campo "tag version" identificar la entrega con la siguiente nomenclatura pXeY donde X corresponde al número del proyecto e Y corresponde al número de entrega.         Por ejemplo, p2e1 es la identificación de la entrega 1 para el proyecto 2.
4) Completar los otros campos a gusto y presionar el botón verde "Publish release"
