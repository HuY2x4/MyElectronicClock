#ifndef _CD_H__
#define _CD_H__

//导入Arduino核心头文件
#include "Arduino.h"  


class countDown
{
     private:
          int cd_hou;
          int cd_min;
          int cd_sec;     
     
     public:
          
          countDown();   //构造函数
          void setCDTime(String cd_time);
          int getHou();          
          int getMin();          
          int getSec();
          void setHou(int Hou);          
          void setMin(int Min);
          void setSec(int Sec);
          boolean timeDown();   //倒计时下降一秒
          
          

};


#endif
