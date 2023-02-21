/*命名空间限制*/
namespace java com.horizon.demo.service

/*所有接口函数的描述*/
service DemoStrThriftService
{
   /**
	* value 中存放两个字符串拼接之后的字符串
	*/
	string getStr(1:string srcStr1, 2:string srcStr2),

}

/*所有接口函数的描述*/
service DemoIntThriftService
{
   /**
    * 将value计算后返回字符串
    */
	i64 getInt(1:i32 val1, 2:i32 val2)

}