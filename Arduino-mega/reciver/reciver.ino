#include "Conceptinetics.h"

byte values[10];
char out[5];
boolean sendData = false;
String s = "";
int i, val;

#define DMX_SLAVE_CHANNELS 10 
//#define RXEN_PIN 13
DMX_Slave dmx_slave(DMX_SLAVE_CHANNELS);
//DMX_Slave dmx_slave(DMX_SLAVE_CHANNELS, RXEN_PIN);

void setup() {             
  
  dmx_slave.enable();  
  dmx_slave.setStartAddress(176);
  dmx_slave.onReceiveComplete(OnFrameReceiveComplete);

  for(i = 0; i < 10; i++)
    values[i] = 0;
  out[4] = 0x00;
  Serial.begin(76800);
}

void loop() {}

void OnFrameReceiveComplete(void) {
  for(i = 9; i >= 0; i--){
    val = dmx_slave.getChannelValue(i + 1);
    if(val != values[i]){
      getOutput((i + 1) & 0xFF, out);
      getOutput(val & 0xFF, out + 2);
      s += out;
      sendData = true;
      values[i] = val;
    }
  }
  if(sendData){
    Serial.println(s);
    s = "";
    sendData = false;
  }
}

void getOutput(int value, char out[]){
  out[0] = value / 16 + 65;
  out[1] = value % 16 + 65;
}

