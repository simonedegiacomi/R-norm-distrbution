# R-norm-distrbution
_R-norm-distrbution_ è un programmino scritto in Java che calcola i valori della 
distribuzione normale appoggiandosi agli eseguibili di R.

È stato pensato per permettere ai poveri studenti di sopravvivere agli 
esercizi di Agostinelli sulla distribuzione normale senza doversi imparare
a memoria le formule di R.

Attualmente il programma supporta solo sistemi operativi Windows.

## Come eseguirlo?
Seguire questi passi:
1) Scaricare ed installare la versione più recente di Java JRE da 
   qui: https://www.java.com/it/download/
2) Scaricare ed installare R versione 3.3.3 (possibilmente 64 bit):
   - Windows: https://cran.r-project.org/bin/windows/base/
3) Scaricare da qui e szippare la versione più recente del programma:
   https://github.com/slavetto/R-norm-distrbution/releases
4) Fate doppio click sul file `R-norm-distribution.jar`. Se vi viene chiesto 
   con quale programma aprirlo, scegliete Java
5) Se il programma vi dice che non è riuscito a trovare R, allora 
aprite il file `config.json` presente nello zip ed incollate nel valore di
   `executable-path` il percorso in cui si trova il file `Rscript.exe` della 
   vostra installazione di R. In Windows il percorso dovrebbe essere: 
   
   `"C:\\Program Files\\R\\R-3.3.3\\bin\\x64\\Rscript.exe"`
   
   oppure
   
   `"C:\\Program Files\\R\\R-3.3.3\\bin\\x86\\Rscript.exe"`
   
   Notare che in JSON `\ ` è il carattere di escape, quindi su Windows dovete 
   ripetere tutti i backslash dell'indirizzo due volte per fare l'escape delle
   barre.