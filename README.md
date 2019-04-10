# 在&lt;百万级>数据库表中&lt;不放回>的&lt;随机>读取N条数据，并按格式生成excle
---
## 如何将ground_histor_meteo_data_label_bacterial_wilt.sql（[点击去百度网盘下载](https://pan.baidu.com/s/1v1dC0ErhLN-5LY8_z2Os-A)）导入MySQL数据库：
1，创建数据库wisdtobadb：
>> create database wisdtobadb;

2，进入wisdtobadb数据库：
>> use wisdtobadb;

3，如何将ground_histor_meteo_data_label_bacterial_wilt.sql导入wisdtobadb数据库：
>> source 你的路径\ground_histor_meteo_data_label_bacterial_wilt.sql;
---
### 实现思想解读（有顺序关联）：
> 1，将数据库表中的数据全部读取到list（由于数据量太大会造成list内存溢出，故使用limit分段查询），再将每一个list中的数据存入文件A（追加），按json格式写入；

> 2，在上一步生成的文件A中随机<按行>读取5000（按自己需求定）条数据，将其存入文件B，同时将除开这5000条数据之外的数据存入临时文件C；

> 3，删除源文件A，重命名文件C为A（这样就实现了<不放回的读取数据>，下次读取5000条数据时就是在剩下的数据中读取）；

> 4，按行读取文件B（为json格式）到实体类（Gson转换），再将每一个实体类按格式写入excle（大功告成）。
### 注意！
1，本工程项目以Maven导入。

2，将jdbc.properties中的数据库密码改成自己密码。
### 效果展示：
>>#### 1，原始数据库表中的数据
>>![image](https://github.com/maliangnansheng/database_excle/blob/master/picture/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8.png)
>>#### 2，导入excle后
>>![image](https://github.com/maliangnansheng/database_excle/blob/master/picture/excle.png)
