#include <TheThingsNetwork.h>
#include <TheThingsMessage.h>
#include <Wire.h>

#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>                
#include <ATT_LoRa_IOT.h>
#include "AirQuality2.h"

// Set your DevAddr, NwkSKey, AppSKey and the frequency plan
const char *devAddr = "26011D21";
const char *nwkSKey = "34B675577856DD20E0E53AAEE152A5CC";
const char *appSKey = "1F1FCC3DE7401D36DDDAB44E2A99EDB5";

#define loraSerial Serial1
#define debugSerial Serial

//#define LoudnessSensorPin A0
//#define AirqualitySensorPin A4
#define LightSensorPin A4   //light sensor

#define DigitalPin 20

// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

TheThingsNetwork ttn(loraSerial, debugSerial, freqPlan);
devicedata_t data = api_DeviceData_init_default;

Adafruit_BME280 bme;
AirQuality2 airqualitySensor;

int loudness;
int airQuality;
int light;
float temp;
float hum;
int pres;

bool btn = false;

void setup()
{
  loraSerial.begin(57600);
  debugSerial.begin(9600);

  tph.begin();                                        // connect TPH sensor to the I2C pin (SCL/SDA)
  
  setupDataStructure();

  pinMode(DigitalPin, INPUT);
  pinMode(LoudnessSensorPin,INPUT);               //init loudnessSensor on A4
  pinMode(GROVEPWR, OUTPUT);                  //define the pin which switches on and off the 3.3v power line to the switched Grove column.
  digitalWrite(GROVEPWR, HIGH);              // turn on the powersupply
  airqualitySensor.init(AirqualitySensorPin); //init airqualitysensor on A0

  bme.begin();
  
  // Wait a maximum of 10s for Serial Monitor
  while (!debugSerial && millis() < 10000)
    ;

  debugSerial.println("-- PERSONALIZE");
  ttn.personalize(devAddr, nwkSKey, appSKey);

  debugSerial.println("-- STATUS");
  ttn.showStatus();
}

void loop()
{
  byte payload[8];
  int tempdata;
  int nth1;
  int nth2;
  debugSerial.println("-- LOOP");
  /*
  loudness = analogRead(LoudnessSensorPin);
  debugSerial.print("loudness: ");
  debugSerial.println(loudness);
  */
  airQuality = airqualitySensor.getRawData();
  debugSerial.print("airquality: ");
  debugSerial.println(airQuality); 
  tempdata = airQuality * 10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[0]=nth1;
  payload[1]=nth2;

  /*
  light = readLight();
  debugSerial.print("light: ");
  debugSerial.println(light);
  */

  temp = bme.readTemperature();
  debugSerial.print("temperature: ");
  debugSerial.println(temp);
  tempdata = temp * 10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[2]=nth1;
  payload[3]=nth2;

  /*
  debugSerial.print("temperature bytes: ");
  debugSerial.print(nth1, HEX);
  debugSerial.print("  ");
  debugSerial.println(nth2, HEX);
  */
  hum = bme.readHumidity();
  debugSerial.print("humidity: ");
  debugSerial.println(hum);
  tempdata = hum * 10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[4]=nth1;
  payload[5]=nth2;

  
  pres = round(bme.readPressure()/100.0);
  debugSerial.print("pressure: ");
  debugSerial.println(pres);
  tempdata = pres * 10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[6]=nth1;
  payload[7]=nth2;
  /*
  payload[0]=loudness;
  payload[1]=airQuality;
  payload[2]=light;
  payload[3]=temp;
  payload[4]=hum;
  */
  ttn.sendBytes(payload, sizeof(payload), 1, true);
  delay(20000);
}

void setupDataStructure()
{
  data.has_motion = false;
  data.has_water = false;
  data.has_temperature_celcius = true;
  data.has_temperature_fahrenheit = false;
  data.has_humidity = true;
}

float readLight()
{
  int sensorValue = analogRead(LightSensorPin); 
  float Rsensor= sensorValue * 3.3 / 1023;
  Rsensor = pow(10, Rsensor);
  return Rsensor; 
}
/*
void encode(*byte payload, float data, int type)
{
  if(type == 1)
  {
    //int temp = data * 10;
    //payload[0]=(number >> (8*0)) & 0xff
  }
}
*/
