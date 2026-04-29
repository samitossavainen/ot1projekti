# OHJEET VERSIONHALLINTAAN
Nämä ohjeet terminaalin kautta versionhallinnan kanssa toimimiseen

- Ennen uuden työn aloittamista varmista, että main on ajan tasalla:


git checkout main
git pull origin main


- Luo uusi haara main‑haarasta. Käytä kuvaavaa nimeä.

git checkout -b <oman-haaran-nimi>

- Tee pieniä, selkeitä committeja
- Käytä kuvaavia commit‑viestejä


git add <tiedosto>
git commit -m "message kirjoitetaan tähän"

- Tarkista muutokset

git status

- Työnnä oma haara etätietosäiliöön

git push origin <oman-haaran-nimi>

- Päivitä haara tarvittaessa mainista jos mainiin tulee uusia muutoksia kesken työn


git checkout <oman-haaran-nimi>
git merge main

- Ratkaise mahdolliset konfliktit ja jatka työtä.

- Ennen mergeä main-haaraan varmista aina ennen mergeä, että:
Projekti kääntyy
./mvnw clean test
Et ole lisännyt versionhallintaan
  target/
  .idea/
  muita IDE- tai build‑tiedostoja

MERGE MAIN-HAARAAN TEHDÄÄN VASTA KUN
toiminnallisuus on valmis
build onnistuu
koodi on siisti ja ymmärrettävä (lol)
MERGE VOIDAAN TEHDÄ
paikallisesti (git merge)
TAI
GitHubin pull requestin kautta (koitetaan tästä pitää)

# Yleiset pelisäännöt

main pidetään aina ajettavassa kunnossa
Kaikki uusi kehitys tapahtuu omissa haaroissa
Rakennustuotokset (target/) ja IDE‑asetukset eivät kuulu Git‑repoon
SQLite‑tietokanta (*.db) kuuluu projektiin ja sisältää testidataa



