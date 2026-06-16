# Sistema de Gestión Parroquial y Administración de la Arquidiócesis
Materia: Bases de Datos | Periodo: 2025-2 | Estado: Completado

## Equipo de trabajo
- David Motoche ([GdavidM](https://github.com/GdavidM))
- Eddy Lima([EddyLimaL](https://github.com/EddyLimaL))
- José Troya([JoseTroya-coder](https://github.com/JoseTroya-coder))
- José Viteri([jvit04](https://github.com/jvit04))

## Capturas / Demo
![Interfaz Principal](docs/screenshots/interfazPrincipal.gif)

![Demo App](docs/screenshots/05.gif)




## Funcionalidad
- [x] Arquitectura de Persistencia Relacional: Conexión robusta a motor de base de datos gestionada dinámicamente mediante JDBC a través de la clase unificada `ConexionBD`. [Commit](https://github.com/jvit04/Proyecto_bd_Arquidiocesis/commit/3327acdf1d76a959ca4ca5c77257e025294c0246)
- [x] Motor de Migración e Importación Masiva: Set completo de clases utilitarias de importación automatizada que parsean, validan con expresiones regulares (`RegexPatterns`) y cargan masivamente archivos planos (.csv) correspondientes a Clérigos, Parroquias, Vicarías, Sacramentos, Convenios y Pastorales. [Commit](https://github.com/jvit04/Proyecto_bd_Arquidiocesis/commit/4e34b8ed8059c8a908ad16b241368f29c134c7ad)
- [x] Módulo Transaccional SQL: Lógica de negocio encapsulada para inserciones seguras en caliente, controlando excepciones de manera amigable mediante el componente especializado `ExcepcionAmigable`. [Commit](https://github.com/jvit04/Proyecto_bd_Arquidiocesis/commit/9798df0b77ec6e8d2673e196a9018ff1e34ac176)
- [x] Capa de Visualización e Interfaces Gráficas: Sistema de vistas parametrizadas construidas sobre JavaFX (`Arquidiocesis.fxml`), estilizadas dinámicamente con selectores CSS avanzados (`botonesMenu.css` y `tablaDisegno.css`) y tipografías corporativas de alta definición. [Commit](https://github.com/jvit04/Proyecto_bd_Arquidiocesis/commit/84e3b74a50a86bdadbf9987a7dee118218a60ef5)
- [x] Subsistema de Reportería Analítica: Capacidad nativa implementada a nivel de software para procesar conjuntos de resultados relacionales complejos y estructurar salidas formateadas a través de `VistaReporte`. [Commit](https://github.com/jvit04/Proyecto_bd_Arquidiocesis/commit/adca6438abe4cffb55dd8e0c0ff4579b5480da2c)

## Tecnologías
`Java 17` `JavaFX 21` `Maven` `SQL` `CSS3`

## Ejecución
### Instrucciones paso a paso

#### Prerrequisitos
* Java Development Kit (JDK) 17 o superior.
* Servidor de Base de Datos [Completar motor, ej: MySQL / PostgreSQL] activo.
* Apache Maven instalado.

#### Pasos de Despliegue
1. Clonar el repositorio del proyecto en su máquina local:
```bash
    git clone [https://github.com/jvit04/Proyecto_bd_Arquidiocesis.git](https://github.com/jvit04/Proyecto_bd_Arquidiocesis.git)
    cd Proyecto_bd_Arquidiocesis
```
2. Configurar la cadena de conexión en el archivo `src/main/java/utilities/ConexionBD.java` con las credenciales correspondientes a su servidor local de Base de Datos.
3. Compilar el proyecto y descargar las dependencias gestionadas por Maven:
```bash
    mvn clean install
```
4. Ejecutar la aplicación:
```bash
    mvn javafx:run
```

## Métricas de Progreso
| Indicador             | Valor  |
|-----------------------|--------|
| Commits totales       | 42     |
| Issues/PRs fusionados | 0/2    |
| Cobertura de pruebas  | N/A    |
| Última actualización  | 2025-12-17 |

## Reflexión y Aprendizajes
- **Habilidades desarrolladas:** Diseño de interfaces gráficas enriquecidas desacopladas de la persistencia, normalización e integridad referencial en bases de datos relacionales, y programación avanzada de flujos I/O para procesamiento masivo de datos.
- **Qué funcionó bien:** La segregación estricta del patrón Controlador-Vista (`ArquidiocesisController` y archivos FXML) junto con la centralización de utilitarios de persistencia permitió escalar la carga de más de 10 entidades distintas de datos de manera uniforme y sin duplicación de código.
- **Qué se podría mejorar:** Se podría parametrizar las rutas absolutas de entrada de archivos mediante propiedades dinámicas configurables externamente en lugar de depender de rutas predefinidas internas en el software, mejorando la portabilidad del entorno de ejecución.
- **Conceptos clave aplicados de la materia:** Modelado físico de datos, transaccionalidad, manejo de conexiones concurrentes en entornos gráficos multi-hilo, validación sintáctica previa al almacenamiento mediante regex y abstracción de consultas relacionales.