<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 10:16:57
 * @LastEditTime: 2025-01-03 12:40:43
 * @FilePath: /code/blog/client-web/src/common/animation-button/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<script setup lang="ts">
const props = defineProps({
  fn: {
    type: Function,
    default: () => {}
  },
  content: {
    type: String,
    default: '回到首页'
  }
});

const toPage = () => {
  props.fn();
};
</script>

<template>
  <div class="buttons">
    <a href="#" class="button" @click="toPage">
      <span class="button__text"> {{ props.content }} </span>
      <img src="@/assets/img/button/cone.png" alt="" class="button__cone" />
      <img src="@/assets/img/button/torus.png" alt="" class="button__torus" />
      <img src="@/assets/img/button/icosahedron.png" alt="" class="button__icosahedron" />
      <img src="@/assets/img/button/sphere.png" alt="" class="button__sphere" />
    </a>
  </div>
</template>

<style lang="css" scoped>
.button {
  --first-color: #5596ff;
}

.buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

.button {
  position: relative;
  background-color: var(--first-color);
  color: #fff;
  padding: 0.9rem 2.2rem;
  border-radius: 3rem;
  transition: 0.4s;
}

.button::after {
  content: '';
  width: 80%;
  height: 40%;
  background: linear-gradient(80deg, hsl(217, 80%, 80%) 10%, hsl(217, 85%, 70%) 48%);
  position: absolute;
  left: 0;
  right: 0;
  bottom: -4px;
  margin: 0 auto;
  border-radius: 3rem;
  filter: blur(12px);
  z-index: -1;
  opacity: 0;
  transition: opacity 0.4s;
}

.button__text {
  position: relative;
  z-index: 10;
}

.button img {
  position: absolute;
  inset: 0;
  margin: auto;
  pointer-events: none;
  transition: 0.6s;
  opacity: 0;
}

/* Move 3D geometric elements */
.button__cone {
  width: 18px;
  transform: translate(-25px, -6px) rotate(55deg);
  filter: blur(0.5px);
}

.button__torus {
  width: 38px;
  transform: translate(7px, -14px) rotate(80deg);
}

.button__icosahedron {
  width: 36px;
  transform: translate(34px, -4px) rotate(-45deg);
  filter: blur(0.9px);
}

.button__sphere {
  width: 30px;
  transform: translate(-5px, 15px) rotate(40deg);
}

/* View shadow gradient */
.button:hover::after {
  opacity: 1;
}

/* Button scale */
.button:hover {
  transform: scale(1.3);
}

/* View 3D geometric elements */
.button:hover img {
  opacity: 1;
}

.button:hover .button__cone {
  transform: translate(-38px, -10px) scale(1.2);
}

.button:hover .button__torus {
  transform: translate(7px, -32px) scale(1.1);
}

.button:hover .button__icosahedron {
  transform: translate(50px, -20px) scale(1.1);
}

.button:hover .button__sphere {
  transform: translate(-14px, 20px) scale(1.1);
}
</style>
