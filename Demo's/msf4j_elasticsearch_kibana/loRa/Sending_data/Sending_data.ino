/*
 * Dit programma verstuurd data over lora naar de EnCo cloud.
 * de data van de demo's zijn niet consequent en komen niet altijd aan en zijn niet voorzien van commentaar.
 * Tip: wanneer je in MicrochipLoRaModem.cpp FULLDEBUG definieerd wordt er vanuit de header extra info afgedrukt
 * tijdens het het gebruiken van de lora modulle
 */

#include <Wire.h>                
#include <ATT_LoRa_IOT.h>
#include "AirQuality2.h" 
#include "keys.h"               
#include <MicrochipLoRaModem.h>

#define SERIAL_BAUD 57600
#define LoudnessSensorPin A4
#define AirqualitySensorPin A0
#define LightSensorPin A2

#define debug Serial

#define DigitalPin 20
#define buttonDigiPin 4

MicrochipLoRaModem Modem(&Serial1, &debug);
ATTDevice Device(&Modem, &debug);
AirQuality2 airqualitySensor;

//init global variables
int aantal=0;

float loudness;
float airQuality;
float light;


void setup()
{                         
  pinMode(LoudnessSensorPin,INPUT);               //init loudnessSensor on A4
  pinMode(buttonDigiPin,INPUT);
  pinMode(GROVEPWR, OUTPUT);                  //define the pin which switches on and off the 3.3v power line to the switched Grove column.
  digitalWrite(GROVEPWR, HIGH);              // turn on the powersupply
  airqualitySensor.init(AirqualitySensorPin); //init airqualitysensor on A0
  while((!Serial) && (millis()) < 2000){}   //wait until serial bus is available, so we get the correct logging on screen. If no serial, then blocks for 2 seconds before run
  debug.begin(SERIAL_BAUD);                   
  Serial1.begin(Modem.getDefaultBaudRate()); 
  
  while(!Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY)); //try to connect
    Serial.println("retrying to connect");
  Serial.println("Ready to send data");
}

void loop()
{
    debug.println("---------------------------------------------");
    loudness = analogRead(LoudnessSensorPin);
    airQuality = airqualitySensor.getRawData();
    light = readLight();
    SendValue();
    delay(60000);
  
  

  //Modem.PrintModemStatus();

}

void SendValue()
{
  debug.println("data to send: ");
  debug.print("loudness: ");
  debug.println(loudness);
  debug.print("airQuality: ");
  debug.println(airQuality);
  debug.print("light: ");
  debug.println(light);
  debug.print("total attempts: ");
  debug.print(aantal);
  debug.print("\n");
  aantal = aantal +1;
  Device.Queue(loudness);
  Device.Queue(airQuality);
  Device.Queue(light);
  
  
  Device.Send(ACCELEROMETER, true);
  //Modem.PrintModemConfig();
}

float readLight()
{
  int sensorValue = analogRead(LightSensorPin); 
  float Rsensor= sensorValue * 3.3 / 1023;
  Rsensor = pow(10, Rsensor);
  return Rsensor; 
}

