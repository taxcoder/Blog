/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 10:53:27
 * @LastEditTime: 2025-01-04 11:46:37
 * @FilePath: /code/blog/client-web/src/common/echart/echarts.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import * as echarts from 'echarts/core';

import { TooltipComponent } from 'echarts/components';
import { PieChart } from 'echarts/charts';
import { LabelLayout } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([TooltipComponent, PieChart, CanvasRenderer, LabelLayout]);

export default echarts;
