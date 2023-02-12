/*命名空间限制*/
namespace java com.horizon.simple.service
/*可以加一个版本号的常量方便后面编译文件接口的版本管理*/
# const string VERSION = "1.0.1"

exception RequestException {
    1:i32 code;
	2:string reason;
}