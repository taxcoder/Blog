/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-13 03:32:34
 * @LastEditTime: 2025-01-13 07:52:31
 * @FilePath: /code/blog/client-web/src/mock/content.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export let content = `# K 线分析

股票的盘就是由不同的 K 线组合而成的，分析 K 线是技术分析的基础。

\`看多：\`表示大家都看好，都往里面充钱买，股票上涨。
\`看空：\`表示大家都不看好，都卖出手里的股票套现，股票下跌。

K 线分为阳线（红色）和阴线（绿色），阳线简单来说表示上涨，阴线表示下跌，==K 线的不同形态是表示多空力量转换的过程。==

**阳线：**

![阳线](https://note.recall.top/images/article-1703750674526.png)

**阴线：**

![阴线](https://note.recall.top/images/article-1703750779661.png)


## 阳线

### 大阳线

大阳线实体越长，说明收盘价和开盘价之间差距越大，涨的则越多，多方力量强劲。

![大阳线](https://note.recall.top/images/article-1703756461050.png)


**光头光脚大阳线：**&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

开盘价就是最低价，收盘价就是最高价 ，股价从开盘一路涨到收盘，后市继续看好。

![光头光脚大阳线](https://note.recall.top/images/article-1703753399576.png)

### 中阳线

开盘价和收盘价，在3%到6%之间的涨幅，就是中阳线。

![中阳线](https://note.recall.top/images/article-1703755832079.png)

### 小阳线

开盘和收盘价相差不大，实体和上下影线较短，一般是在3%的涨幅内，一般在震荡市场行情中容易出现，市场不活跃，多空力量相当。

![小阳线](https://note.recall.top/images/article-1703756239185.png)

## 阴线

### 大阴线

大阴线实体越长，说明收盘价和开盘价之间差距越大，跌的则越多，空方力量强劲。

![](https://note.recall.top/images/article-1703756497710.png)

**光头光脚大阴线：**

开盘就是最高价，收盘就是最低价。

![光头光脚大阴线](https://note.recall.top/images/article-1703754320537.png)


==**高位的大阴线是强烈看跌的信号，但如果是长期下跌后的大阴线则有可能是反转信号，空方完全战胜多方，跌无可跌，股价已经见底，接下来很可能会有一波上涨。**==

### 中阴线

开盘价和收盘价，在3%到6%之间的跌幅，就是中阴线。

![中阴线](https://note.recall.top/images/article-1703755970957.png)

### 小阴线

\`\`\`vue
<template>
  <MdEditor v-model="text" />
</template>

<script setup>
import { ref } from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

const text = ref('Hello Editor!');
</script>
\`\`\`

> [!warning]
> 警告文字

每条命令执行完成后会输出带有++in++和++out++字样的提示，备份可能会需要一些时间，此过程中不要断开++SSH++和电源

[超链接](https://github.com/imzbf)

> 开盘和收盘价相差不大，实体和上下影线较短，一般是在3%的跌幅内，一般在震荡市行情中容易出现，市场不活跃，多空力量相当。

![小阴线](https://note.recall.top/images/article-1703756298837.png)

## 长影K线

**<span style="color: red;">上影线越长，上方阻力越大，下影线越长，下方支撑越强。</span>**

![长影K线](https://note.recall.top/images/article-1703758570688.png)

**\`长上影K线：\`**
- 当天多方一度占优较大，但因空方打压或利空消息而在尾盘有所回落。
- 技术信号不明确，后市不一定看跌。
- 如果出现在高位，说明上方压力过大，后市看好。
- 在特定环境也可以叫做==仙人指路==（股价处于长期横盘震荡，尝试突破却被空方打压，在修整一段时间后成功突破大涨）。

![仙人指路](https://note.recall.top/images/article-1703759034835.png)


**\`长下影K线：\`**
- 当天空方一度占优较大，但因多方打压或利好消息而在尾盘有所上涨。
- 长下影K线处于高位更可能是空头试探的打压行为，特别是反复出现高位的长下影K线。
- 如果连续几天出现长下影K线，是看多信号，后市更加看好（<span style="color: red;">尤其是长期下跌后的长下影线，是强烈的看多信号</span>）。
  - 原因：投资者会认为已经跌无可跌，空方力量完全释放，而大幅买进股票，抬高股价，股价越高买进的人就越多，直到有人开始大幅卖出股票。
 
## 十字星

- 表示当天的多空力量相当。
- **<span style="color: red;">在高位和低位时是行情逆转的信号</span>**。
- **<span style="color: red;">中途出现一般是调整或者是反弹结束的信号</span>**。
- **<span style="color: red;">震荡行情中出现就是要突破震荡市的信号</span>**。

![十字星](https://note.recall.top/images/article-1703759722568.png)

### T字形和倒T形

开盘价和收盘价相同，但是当天的收盘价是最高价（T字形）或者是最低价（倒T形）。

**\`T字形：\`** 出现在股价低位是明显看多信号，T字形K线对后市多方有利。
**\`倒T形：\`** 股价冲高回落，收盘价和开盘价一样，是明显的看空信号。

![T字形和倒T形](https://note.recall.top/images/article-1703760398060.png)

#### 一字型

![一字型](https://note.recall.top/images/article-1703760754668.png)

**\`一字涨停:\`** 突然出现利好消息，买也买不到。
**\`一字跌停:\`** 突然出现利空消息，卖也卖不掉。

![](https://note.recall.top/images/article-1703760895578.png)
`;
