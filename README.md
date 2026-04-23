# ot1projekti

1. Kloonaa repo:
   git clone …

2. Avaa IntelliJssä projektin JUURI (kansio jossa pom.xml on)

3. Jos Maven ei tunnistu automaattisesti:
   File → Project Structure → Modules → + → Import Module
   → valitse pom.xml → Import as Maven

4. Tarkista:
   - Maven-paneeli näkyy
   - .mvn\wrapper kansio olemassa
   - .\mvnw clean compile    toimii
  
5. Huom! Maven wrapper käytössä, joten käytetään maven-komentoihin terminaalissa .\mvnw EIKÄ mvn
