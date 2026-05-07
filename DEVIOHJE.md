# OHJEET VERSIONHALLINTAAN
Nämä ohjeet terminaalin kautta versionhallinnan kanssa toimimiseen

1. Ennen uuden työn aloittamista varmista, että main on ajan tasalla:


- git checkout main
- git pull origin main

2. Luo uusi haara main‑haarasta. Käytä kuvaavaa nimeä.

- git checkout -b "uuden-haaran-nimi"  
Koska vaihdoit main-haarasta uuteen haaraan, uusi haara pohjautuu mainiin, joten ei tarvetta pullata

TAI

2. Jatka työskentelyä edellisessä omassa haarassasi

- git checkout "edellisen-haaran-nimi"
- git pull origin main

3. Tee pieniä, selkeitä committeja

4. Käytä kuvaavia commit‑viestejä


- git add "tiedosto"
- git commit -m "message kirjoitetaan tähän"

5. Tarkista muutokset

- git status

6. Työnnä oma haara etätietosäiliöön

- git push origin "oman-haaran-nimi"

7. Päivitä haara tarvittaessa mainista jos mainiin tulee uusia muutoksia kesken työn


- git checkout "oman-haaran-nimi"
- git merge main

8. Ratkaise mahdolliset konfliktit ja jatka työtä.

# Ennen mergeä main-haaraan varmista aina ennen mergeä, että:  
Projekti kääntyy  
./mvnw clean test  
Et ole lisännyt versionhallintaan  
  target/  
  .idea/  
  muita IDE- tai build‑tiedostoja  

# MERGE MAIN-HAARAAN TEHDÄÄN VASTA KUN
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



