
#include <Wire.h>
#include <ATT_LoRa_IOT.h>
#include "keys.h"
#include <MicrochipLoRaModem.h>

#define SERIAL_BAUD 57600

#define AnalogSensor A4


MicrochipLoRaModem Modem(&Serial1, &Serial);
ATTDevice Device(&Modem, &Serial);

void setup() 
{
  pinMode(AnalogSensor,INPUT);
  while((!Serial) && (millis()) < 2000){}            //wait until serial bus is available, so we get the correct logging on screen. If no serial, then blocks for 2 seconds before run
  Serial.begin(SERIAL_BAUD);
  Serial1.begin(Modem.getDefaultBaudRate());          // init the baud rate of the serial connection so that it's ok for the modem
  Device.Connect(DEV_ADDR, APPSKEY, NWKSKEY);
  //Modem.PrintModemConfig();                       //drukt alle configuraties af van de modem
  Serial.println("Ready to send data");
}

void loop()
{
  
}

