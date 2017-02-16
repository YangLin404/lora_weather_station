##Gebruikte Hardware
Tijdens de stage ga ik gebruik maken van een AllThingsTalk [starter kit](http://shop.allthingstalk.com/product/lora-rapid-development-kit/). Deze kit is voorzien van een SODAQ Mbili bordje met een lora module ( Microchip RN2483) en enkele sensoren.De gebruikte micrcontroller van het bodje is een ATmega1284P.


##14 feb
1. installeren van Aduino IDE
2. downloaden van liberary's 
3. volgen van tutorial
4. spelen met demo

##15 feb
1. programma's testen andere sensoren SODAQ Mili
2. opmaken opstartverslag
3. testen connectie met cloud server
	* data lijkt enkel te versturen bij het initiliseren van de modem
	* meestal komt enkel de data aan die gelijk is met de initialisatie waarde
	* bij het initialiseren van de modem lijkt alles inorde, bij de eerste keer versturen van data komt er een time-out error. De volgende keren altijd een failed error
4. doorbraak :)
	* Achterhaald hoe er een device moet aangemaakt worden via [enco website](https://devs.enco.io/dashboard/seaas/devices/1C8779C00000002E/overview)
	* Plaats waar activiteit nagekeken kan worden
5. site om firm ware [uptedaten](http://docs.enco.io/docs/faq-upgrade-allthingstalk-devkit-firmware)

* probleem blijft dat sommige waardes niet kloppen. De waardes blijven dan hetzelfde als de eerste waarde bij start up.



##16 feb

1. doorzoeken header files en bestuderen protocol zodat we de error's uit de code kunnen hallen/ de oorzaak achterhalen.
2. er voor zorgen dat we betroubaarder kunnen uploaden.


