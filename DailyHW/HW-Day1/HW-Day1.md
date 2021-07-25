<div class="cover" style="page-break-after:always;font-family:方正公文仿宋;width:100%;height:100%;border:none;margin: 0 auto;text-align:center;">
    <div style="width:60%;margin: 0 auto;height:0;padding-bottom:10%;">
        </br>
        <img src="https://oss.ydjsir.com.cn/NJU-Black.png" alt="校名" style="width:86%;"/>
    </div>
    </br></br></br></br></br>
    <div style="width:60%;margin: 0 auto;height:0;padding-bottom:40%;">
        <img src="https://oss.ydjsir.com.cn/NJU-LOGO-Black.png" alt="校徽" style="width:45%;"/>
	</div>
    </br></br></br>
<span style="font-family:华文黑体Bold;text-align:center;font-size:24pt;margin: 10pt auto;line-height:30pt;">Android 应用开发：7月13日作业</span>
    </br>
	</br>
    <table style="border:none;text-align:center;width:90%;font-family:仿宋;font-size:14px; margin: 0 auto;line-height:1.5">
    <tbody style="font-family:方正公文仿宋;font-size:16pt;">
        <tr style="font-weight:normal;"> 
    		<td style="width:20%;text-align:right;">姓　　名</td>
    		<td style="width:2%">：</td> 
    		<td style="width:40%;font-weight:normal;border-bottom: 1px solid;text-align:center;font-family:华文仿宋"> </td>     </tr>
        <tr style="font-weight:normal;"> 
    		<td style="width:20%;text-align:right;">学　　号</td>
    		<td style="width:2%">：</td> 
    		<td style="width:40%;font-weight:normal;border-bottom: 1px solid;text-align:center;font-family:华文仿宋"> </td>     </tr>
    	<tr style="font-weight:normal;"> 
    		<td style="width:20%;text-align:right;">指导教师</td>
    		<td style="width:2%">：</td> 
    		<td style="width:40%;font-weight:normal;border-bottom: 1px solid;text-align:center;font-family:华文仿宋"> 高超 </td>     </tr>
    	<tr style="font-weight:normal;"> 
    		<td style="width:20%;text-align:right;">日　　期</td>
    		<td style="width:2%">：</td> 
    		<td style="width:40%;font-weight:normal;border-bottom: 1px solid;text-align:center;font-family:华文仿宋">2021年7月15日</td>     </tr>
    </tbody>              
    </table>
</div>

<div STYLE="page-break-after: always;">

[toc]

## 题目要求

为“极简相册”开发以下功能：

**基础部分（60%）**
登陆页面，相册页面
**加分部分（40%）**
注册页面，密码找回页面



## 已实现内容

> 完整项目均在`AlbumByYDJSIR`目录下。
>
> 所有内容均实现了中英双语适配。中英双语下的界面配色亦不相同。下面均以中文界面演示。

### 登陆页面

如果登陆成功，则跳转至相册页面，在模拟的200ms网络延迟过程中，屏幕会出现进度条。

否则会利用`toast`提示`您输入的账号与密码不匹配`。

<img src="HW-Day1.assets\image-20210717133428566.png" alt="image-20210717133428566" style="zoom: 50%;" />

### 注册页面

<img src="HW-Day1.assets\image-20210717132316426.png" alt="image-20210717132316426" style="zoom: 50%;" />

如果用户输入的信息不正确（目前只有漏填），系统会利用`toast`提示`注册失败，数据不对`；

如果用户没有勾选用户条款选项，系统会利用`toast`提示`注册需要同意用户条款`；

其余情况下，用户注册成功，系统提示`注册成功，请登录`。

<img src="HW-Day1.assets\image-20210717132645216.png" alt="image-20210717132645216" style="zoom: 50%;" />

### 用户政策与隐私政策页面

在注册界面点击`注册表示同意用户条款`后进入用户政策与隐私政策页面。

<img src="HW-Day1.assets\image-20210717132333381.png" alt="image-20210717132333381" style="zoom: 50%;" />

<img src="HW-Day1.assets\image-20210717132351669.png" alt="image-20210717132351669" style="zoom: 50%;" />

用户可以自由地在最终用户许可协议与隐私政策之间切换。隐私保护政策页面左上角的返回箭头指向的是最终用户许可协议页面。如果要结束查看并返回政策，则可以点击右下角的`我知道了，请继续`按钮。

### 找回密码页面

在登陆页面点击`忘记密码`，进入找回页码页面。输入姓名与邮箱，屏幕下方会显示找回的密码。如果邮箱无法匹配到一个已注册用户，则会提示`此用户未注册`。如果邮箱对应已注册用户姓名与输入姓名不匹配，则会提示`用户名不匹配！`。

<img src="HW-Day1.assets\image-20210717132715536.png" alt="image-20210717132715536" style="zoom: 50%;" />

### 相册页面



<img src="HW-Day1.assets\image-20210717133026892.png" alt="image-20210717133026892" style="zoom: 50%;" />

登陆成功后，进入相册页面，点击`前一张`向前翻页，点击`后一张`向后翻页，点击`登出`则是退出当前账号并回退到登陆页面。如果翻页到了边界，则会提示`已经是最后一张了`或`已经是第一张了`。

<img src="HW-Day1.assets\image-20210717133155227.png" alt="image-20210717133155227" style="zoom: 50%;" />