byte n[12];
byte i, a;
boolean sendData = false;
String s = "";

void setup() {
  for(i = 0; i < sizeof(n); i++) n[i] = 1;
  for(i = 2; i < 13; i++) pinMode(i, INPUT_PULLUP);
  for(i = 14; i < 18; i++) pinMode(i, OUTPUT);
  Serial.begin(9600);
}

void working(){
  while(true){
    for(i = 2; i < 14; i++){
      a = digitalRead(i);
      if(a!=n[i-2]){
        s += (char) ((i - 1) * 2 - a + 64);
        sendData = true;
        n[i-2] = a; //0 = Pressed, 1 = unpressed
      }
    }
    if(sendData){
      Serial.println(s);
      s = "";
      sendData = false;
    }
  }
}

void loop() {
  digitalWrite(A0, 1);
  working();
}

