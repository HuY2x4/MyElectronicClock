#include "countDown.h"
#include "Arduino.h"


countDown::countDown()
{
}

void countDown::setCDTime(String cd_time){
  Serial.println("cdtime:"+cd_time);
  int commaPosition=-1;
  int j=0;
  int kaiguan=0;
  int arr_hou=0;
  int arr_min=0;
  int arr_sec=0;
  Serial.println(cd_time.substring(cd_time.lastIndexOf(":")+1));
  arr_hou=cd_time.substring(0,cd_time.indexOf(":")).toInt();
  arr_min=cd_time.substring(cd_time.indexOf(":")+1,cd_time.lastIndexOf(":")).toInt();
  arr_sec=cd_time.substring(cd_time.lastIndexOf(":")+1).toInt();
  Serial.println("arr:"+String(arr_hou)+"-"+String(arr_min)+"-"+String(arr_sec));
  cd_hou=arr_hou;
  Serial.println("cd_hou:"+String(cd_hou));
  cd_min=arr_min;
  cd_sec=arr_sec;
  Serial.println("cd:"+String(cd_hou)+"-"+String(cd_min)+"-"+String(cd_sec));
  /*do{
      commaPosition = cd_time.indexOf(":");
      Serial.println("commaPosition:"+String(commaPosition));
      Serial.println("j:"+String(j));
      if(commaPosition != -1||kaiguan==1)
      {
        
        if(j==0){
          Serial.println("cd_hou:"+cd_time.substring(0,commaPosition));
          cd_hou==cd_time.substring(0,commaPosition).toInt();
          Serial.println("cd_hou2:"+String(cd_hou));
        }
        else if(j==1){
          Serial.println("cd_min:"+cd_time.substring(0,commaPosition));
          cd_min==cd_time.substring(0,commaPosition).toInt();
          Serial.println("cd_min2:"+String(cd_min));
          kaiguan=1;
        }
        else if(j==2){
          Serial.println("cd_sec:"+String(cd_time.toInt()));
          cd_sec==cd_time.toInt();//现在遇到的问题就是cd_sec等变量读不进去
          Serial.println("cd_sec:"+String(cd_sec));
          kaiguan=0;
        }
         cd_time=cd_time.substring(commaPosition+1,cd_time.length()); 
       //  Serial.println(message);
      }
      else{
        if(cd_time.length()>0){
           
        }
      }
      j=j+1;
   }
   while(commaPosition>=0);*/
   Serial.println("peizhiing");
  
}



         
boolean countDown::timeDown()
{
    if(cd_hou==0&&cd_min==0&&cd_sec==0){
      Serial.println("countD is over");
      return false;//倒计时结束
    }
    else if(cd_sec!=0){
      cd_sec-=1;
    }
    else if(cd_sec==0&&cd_min!=0){
      cd_sec=59;
      cd_min-=1;
    }
    else if(cd_sec==0&&cd_min==0&&cd_hou!=0){
      cd_hou-=1;
      cd_min=59;
      cd_sec=59;
    }
    return true;
    
}

int  countDown::getHou()
{
  Serial.println("getSEC:"+String(cd_hou));
   return cd_hou;
}

int  countDown::getMin()
{
  Serial.println("getSEC:"+String(cd_min));
   return cd_min;
}

int  countDown::getSec()
{
  Serial.println("getSEC:"+String(cd_sec));
   return cd_sec;
}

void  countDown::setHou(int Hou)
{
   cd_hou=Hou;
}

void  countDown::setMin(int Min)
{
   cd_min=Min;
}

void  countDown::setSec(int Sec)
{
   cd_sec=Sec;
}
