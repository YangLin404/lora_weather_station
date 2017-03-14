/*
 * Dit programma verstuurd data over lora naar de EnCo cloud.
 * de data van de demo's zijn niet consequent en komen niet altijd aan en zijn niet voorzien van commentaar.
 * Tip: wanneer je in MicrochipLoRaModem.cpp FULLDEBUG definieerd wordt er vanuit de header extra info afgedrukt
 * tijdens het het gebruiken van de lora modulle
 */

#include <Wire.h>               //header van arduino
//#include <Sodaq_TPH.h>          //header voor de temperatuur, druk en vochtigheids sensor
#include <ATT_LoRa_IOT.h>       //header die gebruikt wordt om de sensors te nummeren en te connecteren van de modem en het sturen van data
#include "keys.h"               //header met de keys in voor het gebruiken van het lora netwerk
#include <MicrochipLoRaModem.h> // header die gebruikt word om de modem te initializeren en om commando's van de modem uit te voeren

#define SERIAL_BAUD 57600
#define AnalogSensor A4
#define debug Serial

MicrochipLoRaModem Modem(&Serial1, &debug);  //maken een modem aan en geven twee seriele lijnen mee
//eentje om te schrijven naar de serial monitor en een om commandos te sturen naar de lora module

ATTDevice Device(&Modem, &debug);

void setup()
{
  pinMode(AnalogSensor,INPUT);                //we initializeren poort A4 als imput
    while((!Serial) && (millis()) < 2000){}   //wait until serial bus is available, so we get the correct logging on screen. If no serial, then blocks for 2 seconds before run
  debug.begin(SERIAL_BAUD);                   //we stellen de baudrate in
  Serial1.begin(Modem.getDefaultBaudRate());  //instellen van de baudrate van de serielle lijn met de standaard waarde van in de header
  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY); //initialiseren van de keys
  
  Modem.SetAutomaticReply(true);              //zet de ar aan -> test om acknowladges te krijgen
  
  Modem.PrintModemConfig();                   //drukt alle configuraties af van de modem
  
  Serial.println("Ready to send data");       //in de seriel monitor printen dat we klaar zijn om data te versturen.
}

float value = 0;
int aantal = 0;
bool ack = true; 

void loop()
{  
  debug.print("\n");
  debug.print("\n");
  
  int SensorValue = analogRead(AnalogSensor);
  float Rsensor = SensorValue * 3.3 / 1023;
  value = pow(10, Rsensor);
  SendValue();
  delay(50000);


  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY);
  //Modem.PrintModemStatus();
//  Modem.PrintModemConfig();
}

void SendValue()
{
  debug.print("Versturen data: ");
  debug.print(value);
  debug.print("\n");
  debug.print("aantal keren verzonden: ");
  debug.print(aantal);
  debug.print("\n");
  aantal = aantal +1;
  Device.Send(value, NUMBER_SENSOR, ack);
  //ack = !ack;
}

