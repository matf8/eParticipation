## Información
#### Grupo 18 - Taller de Sistemas de Información Java EE
  - Manuel Biurrun
  - Mathias Fernández
  - Mauricio Iglesias 
  - https://gitlab.fing.edu.uy/mathias.fernandez.bo/eParticipation.uy
---

## Requerimientos
- Componente central
  - Backend (Jakarta EE 8, entorno Java 11)
    - WildFly Full 26.0.0.Final
    - Mongodb (Atlas cluster-db "eParticipation")
    - Postgresql (db "eParticipation", schema "laboratorio")   
	- Maven 3.8.5
---
  - Frontend (ReactJS 18.0.1)
    - NGINX 1.22
    - npm 8.5.0       
---
- Componente Mobile 
    - Mobile (Kotlin 1.6.21)
      - Android Studio
---
- Componentes Perifericos
  - Evaluadores (Java 11, Springboot 2.5.6)    
  - Organizadores (Java 11, Springboot 2.5.6)
---
- Componentes Externo
   - Plataforma de interoperabilidad (Jakarta EE 8, entorno Java 11)     
      - WildFly Full 26.0.0.Final
	  - Maven 3.8.5

---

## Configuraciones 
  - En Wildfly, se debe configurar el datasource PostgreSQL a nombre de java:/eParticipationDB.
  - En Wildfly, se debe configrar el CORS.
  - En NGINX, se debe configurar el CORS.


