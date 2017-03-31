

#include <Wire.h>
//#include <Sodaq_TPH.h>
#include "libs/Adafruit_Unified_Sensor/Adafruit_Sensor.h"
#include "libs/Adafruit_BME280_Library/Adafruit_BME280.cpp"                
#include "libs/ATT_Lora_IOT/ATT_LoRa_IOT.cpp"
#include "libs/AirQuality2/AirQuality2.cpp" 
#include "keys.h"               
#include "libs/ATT_Lora_IOT/MicrochipLoRaModem.cpp"

#define SERIAL_BAUD 57600
#define LoudnessSensorPin A4
#define AirqualitySensorPin A0
#define LightSensorPin A2

#define debug Serial

#define DigitalPin 20
#define buttonDigiPin 4

Adafruit_BME280 bme;

MicrochipLoRaModem Modem(&Serial1, &debug);
ATTDevice Device(&Modem, &debug);
AirQuality2 airqualitySensor;

//init global variables
int aantal=0;
int whichData=0;

float loudness;
float airQuality;
float light;
float temp;
float hum;
float pres;


void setup()
{                         
  pinMode(LoudnessSensorPin,INPUT);               //init loudnessSensor on A4
  pinMode(buttonDigiPin,INPUT);
  pinMode(GROVEPWR, OUTPUT);                  //define the pin which switches on and off the 3.3v power line to the switched Grove column.
  digitalWrite(GROVEPWR, HIGH);              // turn on the powersupply
  airqualitySensor.init(AirqualitySensorPin); //init airqualitysensor on A0

  bme.begin();
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
    /*
    loudness = analogRead(LoudnessSensorPin);
    debug.print("sending loudness: ");
    debug.println(loudness);
    Device.Send(loudness,LOUDNESS_SENSOR, true);
    resetModem();
    */
    airQuality = airqualitySensor.getRawData();
    debug.print("sending airquality: ");
    debug.println(airQuality);
    Device.Send(airQuality, AIR_QUALITY_SENSOR, true);
    resetModem();
    /*
    light = readLight();
    debug.print("sending light: ");
    debug.println(light);
    Device.Send(light, LIGHT_SENSOR, true);
    resetModem();
    */
    temp = bme.readTemperature();
    debug.print("sending temperature: ");
    debug.println(temp);
    Device.Send(temp, TEMPERATURE_SENSOR, true);
    resetModem();
    /*
    hum = bme.readHumidity();
    debug.print("sending humidity: ");
    debug.println(hum);
    Device.Send(hum, HUMIDITY_SENSOR, true);
    resetModem();
    
    pres = bme.readPressure()/100.0;
    debug.print("sending pressure: ");
    debug.println(pres);
    Device.Send(pres, PRESSURE_SENSOR, true);
    resetModem();
    */
  

  //Modem.PrintModemStatus();

}

float readLight()
{
  int sensorValue = analogRead(LightSensorPin); 
  float Rsensor= sensorValue * 3.3 / 1023;
  Rsensor = pow(10, Rsensor);
  return Rsensor; 
}

void resetModem()
{
  delay(20000);
  while(!Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY)); //try to connect
      Serial.println("retrying to connect");
  Serial.println("Ready to send data");
    
}

