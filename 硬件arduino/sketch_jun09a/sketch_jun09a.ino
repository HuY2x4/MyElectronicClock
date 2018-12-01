/*
DS1307和1602 LCD制作电子时钟
*/
// 库文件
//#include <DS1307RTC.h>
#include <Time.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <aJSON.h>
#include "countDown.h"
#include <RTClib.h>


// 实例化一个名为lcd的LiquidCrysta类型对象，并初始化相关引脚
LiquidCrystal_I2C lcd(0x27,16,2);
 //=============  连接用的============


unsigned long lastCheckInTime = 0; //记录上次报到时间
unsigned long lastUpdateTime = 0;//记录上次上传数据时间
const unsigned long postingInterval = 40000; // 每隔40秒向服务器报到一次
const unsigned long updateInterval = 1000; // 数据上传间隔时间5秒

String outputString = "";
String inputString = "";//串口读取到的内容
boolean stringComplete = false;//串口是否读取完毕
boolean CONNECT = true; //连接状态
boolean isCheckIn = false; //是否已经登录服务器
char* parseJson(char *jsonString);//定义aJson字符串
boolean ifCD=false;//是否开启倒计时模式
countDown countD;//倒计时
String led_year="0";
String led_month="0";
String led_day="0";
String led_hou="0";
String led_min="0";
String led_sec="0";
RTC_DS1307 RTC;

//int i=1;//测试用的 冲突了
void setup()
{
   lcd.init();
   lcd.backlight();
   // 设置LCD有几列几行，1602LCD为16列2行
   lcd.begin(16, 2);
   // 打印一段信息到LCD上
   lcd.print("This is a Clock");
   Serial.begin(115200);
   delay(2000);
   lcd.clear();
   RTC.begin();
}
 
void loop() {


//if(millis() - lastCheckInTime > postingInterval || lastCheckInTime==0) {//每隔一段时间重新连接
//checkIn();
//}

  // tmElements_t tm;
  
  DateTime now;
   // 读出DS1307中的时间数据，并存入tm中
   
    if(ifCD){//倒计时
        // 清除屏幕显示内容
         lcd.clear();
         //显示两位数
         if(countD.getHou()<10){
            led_hou="0"+String(countD.getHou());
         }
         else{
            led_hou=String(countD.getHou());
         }

         if(countD.getMin()<10){
            led_min="0"+String(countD.getMin());
         }
         else{
            led_min=String(countD.getMin());
         }

         if(countD.getSec()<10){
            led_sec="0"+String(countD.getSec());
         }
         else{
            led_sec=String(countD.getSec());
         }
         
         //在LCD第一行输出日期信息
         lcd.setCursor(0, 0);
         lcd.print(led_hou);
         lcd.print(":");
         lcd.print(led_min);
         lcd.print(":");
         lcd.print(led_sec);
        
         if(!countD.timeDown()){//如果倒计时结束  *注意1s的延时
            ifCD=false;
         }
         
   }
   else//正常时间//RTC.read(tm)
   {
     now=RTC.now();
     //显示两位数
     if(now.hour()<10){
            led_hou="0"+String(now.hour());
         }
         else{
            led_hou=String(now.hour());
         }

         if(now.minute()<10){
            led_min="0"+String(now.minute());
         }
         else{
            led_min=String(now.minute());
         }

         if(now.second()<10){
            led_sec="0"+String(now.second());
         }
         else{
            led_sec=String(now.second());
         }
     // 清除屏幕显示内容
     lcd.clear();
     //在LCD第一行输出日期信息
     lcd.setCursor(0, 0);
     lcd.print(now.year());//tmYearToCalendar(tm.Year)
     lcd.print("-");
     lcd.print(now.month());//tm.Month
     lcd.print("-");
     lcd.print(now.day());//tm.Day
     //在LCD第二行输出时间信息
     lcd.setCursor(8, 1);
     lcd.print(led_hou);//tm.Hour
     lcd.print(":");
     lcd.print(led_min);//tm.Minute
     lcd.print(":");
     lcd.print(led_sec);//tm.Second

   }
  
   //每秒钟更新一次显示内容
   delay(1000);


checkIn();//检查是否与服务器进行连接
serialEvent();//接收服务器的数据
if (stringComplete) { //如果数据全部接收（遇到\n）
       inputString.trim();
       //Serial.println(inputString);
       if(inputString=="CLOSED"){
         Serial.println("connect closed!");
         //发送断开连接

         CONNECT=false;
       }
     else{//解析服务器传过来的值
         int len = inputString.length()+1;   
         if(inputString.startsWith("{") && inputString.endsWith("}")){//服务器端传json过来
           char jsonString[len];
           inputString.toCharArray(jsonString,len);
           aJsonObject *msg = aJson.parse(jsonString);
           processMessage(msg);
           aJson.deleteItem(msg);
           Serial.println(inputString);         
         }
       }     
       // clear the string:
       inputString = "";
       stringComplete = false;   
   }
update1(now);



}







void checkIn() {//检查是否处于连接状态    *时钟那边要把esp8266设成自动连接网站
if (!CONNECT) {
Serial.print("+++");
delay(500);
Serial.print("\r\n");
delay(1000);
Serial.print("AT+RST\r\n");
delay(6000);

CONNECT=true;
lastCheckInTime==0;
}/*
else{
Serial.print("{\"method\":\"update\",\"gatewayNo\":\"");
Serial.print(DEVICEID);
Serial.print("\",\"userkey\":\"");
Serial.print(APIKEY);
Serial.print("\"}&^!\r\n");
lastCheckInTime = millis();
}*/
}



void update1(DateTime now){//发送当前的时间tmElements_t tm
     String timedata=String(now.year())+"-"+String(now.month())+"-"+String(now.day())+"+"+String(now.hour())+":"+String(now.minute())+":"+String(now.second());
     //String timedata=String(tm.Year)+"-"+String(tm.Month)+"-"+String(tm.Day)+"+"+String(tm.Hour)+":"+String(tm.Minute)+":"+String(tm.Second);
   Serial.print("{\"data\":\"");
  Serial.print(timedata);
  Serial.print("\"}\r\n");
 
lastCheckInTime = millis();
lastUpdateTime= millis();
}

void serialEvent() {//接收数据
   while (Serial.available()) {
     char inChar = (char)Serial.read();
     if (inChar == '#') {
       stringComplete = true;
        Serial.println(inputString);
     }
     else{
       inputString += inChar;
      // Serial.println(inputString);
     }
     
     
   }
}

void processMessage(aJsonObject *msg){//解析服务器传过来的值
   aJsonObject* state = aJson.getObjectItem(msg, "state");    
     String s=state->valuestring;
       if(s.equals("updtime")){//修改时间
         aJsonObject* time = aJson.getObjectItem(msg, "data");   
          String t=time->valuestring;
         //修改时间的函数
         int timeAr[6];
         int commaPosition=0;
         String StringT =t;
         for(int j=0;j<6;j++){//把String类型的时间转换为数组的形式
            if(j==0||j==1){
            commaPosition=StringT.indexOf("-");
            timeAr[j]=StringT.substring(0,commaPosition).toInt();
            }
            else if(j==2){
            commaPosition=StringT.indexOf("+");
            timeAr[j]=StringT.substring(0,commaPosition).toInt();
            }
            else if(j==3||j==4){
            commaPosition=StringT.indexOf(":");
            timeAr[j]=StringT.substring(0,commaPosition).toInt();
            }
            else if(j==5){
            timeAr[j]=StringT.toInt();
            break;
            }
            StringT=StringT.substring(commaPosition+1,StringT.length()); 
  
         }
         
        timeAr[0]=timeAr[0]-48;//很奇怪，只有year会有48年的误差
        RTC.set(RTC_YEAR, timeAr[0]);  
        RTC.set(RTC_MONTH, timeAr[1]);  
        RTC.set(RTC_DAY, timeAr[2]);  
        RTC.set(RTC_HOUR, timeAr[3]); 
        RTC.set(RTC_MINUTE, timeAr[4]);  
        RTC.set(RTC_SECOND, timeAr[5]);  
         
        //  Serial.print("{\"method\":\"response\",\"result\":{\"successful\":true,\"message\":\"Write serial successful 0\"}}&^!\r\n");
       }
       else if(s.equals("countdown")){//倒计时模式
        aJsonObject* time = aJson.getObjectItem(msg, "data");   
          String t=time->valuestring;
           Serial.println("t:"+t);
           countD.setCDTime(t);//倒计时的函数
           ifCD=true;
       }
       
       
}


 
 
  



