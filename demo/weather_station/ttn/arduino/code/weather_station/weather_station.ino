#include <TheThingsNetwork.h>
//#include <TheThingsMessage.h>
#include <Wire.h>
#include "TPH_Library/Sodaq_TPH.h"
#include "ATTLibs/ATT_LoRa_IOT.h"
#include <Sodaq_TPH.h>

#define loraSerial Serial1
#define debugSerial Serial


#define SERIAL_BAUD 57600
#define LightSensor A4

// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

const char *devAddr = "2601189F";
const char *nwkSKey = "8A81408FC0C1B3F05D21037B100B48AA";
const char *appSKey = "385C57764A46C6E06910E13853962D06";

TheThingsNetwork ttn(loraSerial, debugSerial, freqPlan);

void setup() 
{

  loraSerial.begin(SERIAL_BAUD);
  debugSerial.begin(SERIAL_BAUD);
  
  tph.begin();                                        // connect TPH sensor to the I2C pin (SCL/SDA)  
   
  debugSerial.println("-- PERSONALIZE");
  ttn.personalize(devAddr, nwkSKey, appSKey);

  debugSerial.println("-- STATUS");
  ttn.showStatus();
  
  Serial.println("weather station is ready to send data"); 
}

  byte payload[8];
  int tempdata;
  int nth1;
  int nth2;

void loop() {
  
  int SensorValue = analogRead(LightSensor);
  float Rsensor = SensorValue * 3.3 / 1023;
  float value = pow(10, Rsensor);
  
  float temp = tph.readTemperature();
  float hum = tph.readHumidity();
  float pres = tph.readPressure()/100.0;

  debugSerial.print(value,HEX);
  debugSerial.println("\n");
  tempdata = temp *10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[0]=nth1;
  payload[1]=nth2;

  tempdata = hum *10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[2]=nth1;
  payload[3]=nth2;

  tempdata = pres *10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[4]=nth1;
  payload[5]=nth2;

  tempdata = value*10;
  nth1 = (tempdata >> (1 * 8)) & 0xFF;
  nth2 = (tempdata >> (0 * 8)) & 0xFF;
  payload[6]=nth1;
  payload[7]=nth2;


  ttn.sendBytes(payload, sizeof(payload), 1, true);
  delay(30000);

  /*
  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY);
  Device.Send(temp, TEMPERATURE_SENSOR);
  delay(20000);
  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY);
  Device.Send(value, LIGHT_SENSOR);
  delay(20000);
  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY);
  Device.Send(hum, HUMIDITY_SENSOR);
  delay(20000);*/

}
