# SIGA-RQ

Sistema Integral de Gestión de Alumnos, Accesos On-Demand y Newsletter para **R Quants**.
Proyecto de la materia **Seminario de Práctica de Informática** (Universidad Siglo 21) — Ivan Roman.

## Contenido del repositorio

| Carpeta | Contenido |
|---|---|
| `sql/` | Scripts MySQL: creación de tablas, inserción de datos, consultas y borrado (TP2) |
| `src/siga/` | Prototipo en Java de consola (TP3) |
| `docs/` | Diagramas UML y DER |

## Base de datos (MySQL)

Ejecutar los scripts en orden desde MySQL Workbench o desde la consola:

```
mysql -u root -p < sql/01_creacion_tablas.sql
mysql -u root -p < sql/02_insercion_datos.sql
mysql -u root -p < sql/03_consultas.sql
```

`04_borrado.sql` elimina los datos de prueba.

## Prototipo en Java (TP3)

Se puede abrir la carpeta en NetBeans, IntelliJ o Eclipse y ejecutar la clase `siga.SistemaSigaRQ`,
o compilar desde la consola (requiere JDK 17 o superior):

```
javac -encoding UTF-8 -d out src/siga/*.java
java -cp out siga.SistemaSigaRQ
```

Contraseña del administrador: `admin123`

En esta versión los datos se guardan en memoria (listas). La conexión con MySQL se incorpora en el TP4.

### Clases

- `Persona` (abstracta) → `Alumno`, `Administrador`
- `Pack` (abstracta) → `PackFundamentos`, `PackAvanzado`, `PackPremium`
- `Compra`, `EdicionNewsletter`, `Envio`
- `GestorAcademia`: lógica del sistema (listas, cola de envíos, ordenamiento y búsqueda)
- `SistemaSigaRQ`: menú principal (`main`)
- `DatoInvalidoException`: excepción propia
