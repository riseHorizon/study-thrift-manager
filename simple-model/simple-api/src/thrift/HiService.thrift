/*命名空间限制*/
namespace java com.horizon.simple.service
/*可以加一个版本号的常量方便后面编译文件接口的版本管理*/
const string VERSION = "1.0.1"
/*所有接口函数的描述*/
service SimpleThriftService
{
   /**
	* value 中存放两个字符串拼接之后的字符串
	*/
	string getStr(1:string srcStr1, 2:string srcStr2),

    /**
    * 将value拼接后返回字符串
    */
	string getInt(1:i32 val)

}
