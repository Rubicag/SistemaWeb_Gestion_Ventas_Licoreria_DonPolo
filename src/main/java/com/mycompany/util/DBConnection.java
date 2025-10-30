
// Esta clase ya no es necesaria si usas Spring Boot.
// Usa la inyección de dependencias de Spring para obtener un DataSource:
//
// @Autowired
// private DataSource dataSource;
//
// Y luego obtén conexiones con:
// Connection conn = dataSource.getConnection();
//
// La configuración de la base de datos debe estar en application.properties:
// spring.datasource.url=jdbc:mysql://localhost:3306/licoreria
// spring.datasource.username=root
// spring.datasource.password=
// spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

