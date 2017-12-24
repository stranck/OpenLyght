#include "Conceptinetics.h"

#define DMX_MASTER_CHANNELS 512 
#define RXEN_PIN 2

DMX_Master dmx_master(DMX_MASTER_CHANNELS, RXEN_PIN);

int readStatus;

void setup() {
  dmx_master.enable();
  Serial.begin(76800);
}

void loop() {
  //Serial.println("Read a");
  int a = getInput();
  //Serial.println("Read b");
  int b = getInput();
  dmx_master.setChannelValue(a, b);
}

int getInput(){
  int value = 0;
  readStatus = 1;
  while(readStatus < 3){
    //Serial.println(readStatus);
    if(Serial.available()){
      switch(readStatus){
        case 1: {
          value += (Serial.read() - 65) * 16;
          readStatus++;
          break;
        }
        case 2: {
          value += Serial.read() - 65;
          readStatus++;
        }
      }
    }
  }
  return value;
}

