# restaurant_my
基于Java Servlet/DBUtils和BootStrap的餐厅（餐桌）管理系统部分模块

1. 关系表(见src下的table.sql)
   1. 管理员表
   2. 餐桌表
   
2.包括的功能：
   1. 管理员
      1. **新增管理员（基于jQuery的自制表单验证，基于SmartUpload同步上传文件、基于Servlet的验证码）**
      2. 修改管理员信息（只针对手机号、邮箱修改，同上的表单验证、基于Servlet的验证码功能）
   2. 餐桌
      1. 新增餐桌（同上的表单验证）
      2. **分页显示餐桌（分页工具自制）**
      3. 查询餐桌（输入桌号查询对应的餐桌）
   3. **过滤器（J2EE中自带的Filter的使用）**
   4. 界面使用BootStrap搭建，点击选项卡切换页面使用iframe实现
