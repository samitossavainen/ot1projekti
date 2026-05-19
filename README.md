## Projektista

Tämä projekti on toteutettu Itä-Suomen yliopiston Tietojenkäsittelytieteen laitoksen Ohjelmistotuotanto 1 -kurssilla.
Projektin tavoitteena oli rakentaa toimiva varausjärjestelmä sekä oppia ohjelmistokehityksen keskeisiä käytäntöjä, kuten versionhallintaa, arkkitehtuurisuunnittelua ja testausta sekä dokumentointia.

# OT1-projekti: Mökkivarausjärjestelmä
JavaFX‑pohjainen mökkivarausjärjestelmä, jolla voidaan hallita mökkejä, asiakkaita, varauksia ja laskutusta.
Sovellus on toteutettu kurssiprojektina ja se käyttää paikallista SQLite‑tietokantaa valmiilla testidatalla.

# Toiminnallisuudet

- Mökkien hallinta
- Asiakkaiden hallinta
- Varausten tekeminen ja hallinta
- Laskujen muodostuminen, käsittely ja maksutietojen hakeminen tietokannasta laskuille
- Graafinen käyttöliittymä JavaFX:llä

# Teknologiat

- Java (JDK 25)
- JavaFX
- Maven
- SQLite (tietokanta)
- FXML (käyttöliittymäkuvaukset)

# Tietokanta (SQLite)
Ohjelma käyttää SQLite‑tietokantaa, joka sijaitsee projektin juuressa

- Tietokanta sisältää valmiin testidatan
- Sovellus on heti toimintavalmis ilman erillistä alustusta

Tietokanta toimii täysin paikallisesti eikä vaadi erillistä palvelinta.


Huom:
Kehitysvaiheessa tietokanta pidettiin versionhallinnan ulkopuolella (.gitignore), jotta vältettiin konfliktilanteet usean kehittäjän muokatessa samaa dataa.  
Projektin lopullinen main-haaran versio sisältää yhden yhtenäisen tietokantatiedoston testidatalla.


# Sovelluksen käynnistäminen
- Sovelluksen entry point on `MainApp.java`
- Sovellus käynnistyy JavaFX Application ‑luokasta `com.mokkikodit.MainApp`
- Maven-wrapperin myötä komentoriviltä ./mvnw clean javafx:run

- Vaihtoehtoisesti oman kehitysympäristön ajettavuutta (run config) voi muokata hakemaan entry pointin automaattisesti, 
tämä muutos menee .idea/runConfigurations/ -hakemistoon, eli EI kuulu versionhallintaan!

# Buildin tarkistus
Projektin voi kääntää ja testata komennolla 
./mvnw clean compile TAI clean test

tämä varmistaa että koodi kääntyy oikein

# Huomioita

- target/‑hakemisto ja IDE‑kohtaiset tiedostot on jätetty versionhallinnan ulkopuolelle
- Sovellus käyttää paikallista tiedostopohjaista tietokantaa
- Testidata on tarkoitettu demonstraatiokäyttöön, ei vastaa todellista tuotantoympäristöä

