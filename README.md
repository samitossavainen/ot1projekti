# OT1-projekti: Mökkivarausjärjestelmä
JavaFX‑pohjainen mökkivarausjärjestelmä, jolla voidaan hallita mökkejä, asiakkaita, varauksia ja laskutusta.
Sovellus on toteutettu kurssiprojektina ja se käyttää paikallista SQLite‑tietokantaa valmiilla testidatalla.

# Toiminnallisuudet

- Mökkien hallinta (lisäys, muokkaus, listaus)
- Asiakkaiden hallinta
- Varausten tekeminen ja hallinta
- Laskutuksen käsittely
- Graafinen käyttöliittymä JavaFX:llä

# Teknologiat

- Java (JDK 21+)
- JavaFX
- Maven
- SQLite (tietokanta)
- FXML (käyttöliittymäkuvaukset)

# Tietokanta (SQLite)
Ohjelma käyttää SQLite‑tietokantaa, joka sijaitsee projektin juuressa

- Tietokanta sisältää valmiin testidatan
- Sovellus on heti toimintavalmis ilman erillistä alustusta

Tietokanta toimii täysin paikallisesti eikä vaadi erillistä palvelinta.

# Sovelluksen käynnistäminen
- Sovelluksen entry point on `MainApp.java`
- Sovellus käynnistyy JavaFX Application ‑luokasta `com.mokkikodit.MainApp`
- Maven-wrapperin myötä komentoriviltä ./mvnw clean javafx:run

- Vaihtoehtoisesti oman kehitysympäristön ajettavuutta (run config) voi muokata hakemaan entry pointin automaattisesti, 
tämä muutos menee .idea/runConfigurations/ -hakemistoon, eli EI kuulu versionhallintaan!

# Buildin tarkistus
Projektin voi kääntää ja testata komennolla
./mvnw clean test

tämä varmistaa että koodi kääntyy oikein

# Huomioita

target/‑hakemisto ja IDE‑kohtaiset tiedostot on jätetty versionhallinnan ulkopuolelle
Sovellus käyttää paikallista tiedostopohjaista tietokantaa
Testidata on tarkoitettu demonstraatiokäyttöön

