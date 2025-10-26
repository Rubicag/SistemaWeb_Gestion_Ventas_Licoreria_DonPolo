# Carpeta `lib` para dependencias locales

Uso:

- Coloca aquí JARs que no estén en un repositorio Maven público.
- En el `pom.xml` puedes referenciar dependencias locales usando el scope `system` o, preferible, instala el JAR en tu repositorio local con:

  mvn install:install-file -Dfile=lib/nombre.jar -DgroupId=com.local -DartifactId=mi-lib -Dversion=1.0 -Dpackaging=jar

- Si VS Code muestra referencias rotas a `C:\Users\...\.m2\repository\mysql\mysql-connector-java\8.2.0`, puedes eliminar esa carpeta para forzar la re-descarga:

  Remove-Item -Recurse -Force "$env:USERPROFILE\.m2\repository\mysql\mysql-connector-java\8.2.0"

- Luego forzar una limpieza/descarga de dependencias:

  mvn dependency:purge-local-repository -DreResolve=false
  mvn clean install -U -DskipTests

Notas:

- Prefiere mantener las dependencias en `pom.xml` y dejar que Maven las descargue desde Maven Central.
- Evita usar `system` scope salvo que sea estrictamente necesario.
