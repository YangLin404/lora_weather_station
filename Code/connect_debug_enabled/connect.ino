#include <MicrochipLoRaModem.h>
#include "keys.h"
#include <ATT_LoRa_IOT.h>

#define SERIAL_BAUD 57600
#define FULLDEBUG

MicrochipLoRaModem Modem(&Serial1);
ATTDevice Device(&Modem);
boolean connected;

int DigitalSensor = 20;

void setup() {
  // put your setup code here, to run once:
  // Serial is the USB connection and Serial1 the connection to the Bee module (Microchip). 
  Serial.begin(SERIAL_BAUD);                   // set baud rate of the default serial debug connection
  Serial.println("Setup started");
  Serial1.begin(Modem.getDefaultBaudRate());
  connected = connect();
  Serial1.write("radio get rxbw\r\n");
  delay(10000);
  Serial.write("get rxbw: ");
  while(Serial1.available()) Serial.write(Serial1.read());
  Serial.println("Setup complete");
}

void loop() {
  // put your main code here, to run repeatedly:
  if (!connected)
    connected = connect();

  bool sensorRead = digitalRead(DigitalSensor);

  if (sensorRead){
    Serial.println("sensor value = true");
    Device.Send(sensorRead, BINARY_SENSOR);
  } else
    Serial.println("sensor value = false");

    
  delay(5000);
}

boolean connect(){
  Serial.println("trying to connect...");
  boolean conn = Device.Connect(DEV_ADDR,APPSKEY,NWKSKEY,true);
  if(conn)
    Serial.println("connection success");
  else
    Serial.println("connection failed");
}

