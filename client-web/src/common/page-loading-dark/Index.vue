<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 04:09:49
 * @LastEditTime: 2024-12-31 09:27:29
 * @FilePath: /code/blog/client-web/src/common/page-loading/Index.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div id="loading">
    <div class="wrapper">
      <div class="sofa">
        <div class="sofa-cushion"></div>
        <div class="sofa-bottom-l"></div>
        <div class="sofa-bottom-r"></div>
      </div>
      <div class="cat">
        <div class="cat-arm-shoulder"></div>
        <div class="cat-head">
          <div class="cat-ear-l"></div>
          <div class="cat-ear-r"></div>
          <div class="cat-eye-l"></div>
          <div class="cat-eye-r"></div>
        </div>
        <div class="cat-arm-l"></div>
        <div class="cat-arm-r"></div>
        <div class="cat-tale"></div>
      </div>
      <div class="boy">
        <div class="arm-l"></div>
        <div class="foot-l"></div>
        <div class="head">
          <div class="face">
            <div class="hair-1"></div>
            <div class="hair-2"></div>
            <div class="eye-l"></div>
            <div class="eye-r"></div>
          </div>
          <div class="hair-3"></div>
        </div>
        <div class="foot-r"></div>
        <div class="arm-r"></div>
        <div class="laptop">
          <div class="light"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="css" scoped>
*,
*:before,
*:after {
  box-sizing: border-box;
}

#loading {
  width: 100%;
  display: -webkit-box;
  display: flex;
  -webkit-box-pack: center;
  justify-content: center;
  -webkit-box-align: center;
  align-items: center;
  height: 100vh;
  background: #121317;
}

.wrapper {
  position: relative;
  width: 300px;
}
.wrapper:before {
  content: '';
  background: rgba(255, 255, 255, 0.04);
  width: 350px;
  height: 350px;
  border-radius: 50%;
  position: absolute;
  left: 50%;
  top: -95%;
  -webkit-transform: translate(-50%);
  transform: translate(-50%);
  box-shadow: 0 0 0 50px rgba(255, 255, 255, 0.01);
}

.sofa {
  position: relative;
  width: 265px;
  height: 95px;
  background: #ffd100;
  border-radius: 35px 35px 0 0;
  border: 1px solid #180c0b;
  box-shadow: inset 30px 0 #efbb02;
  -webkit-filter: brightness(0.8);
  filter: brightness(0.8);
}
.sofa:after {
  content: '';
  width: 130px;
  height: 70px;
  background: #efbb02;
  border-radius: 25px;
  position: absolute;
  bottom: -40px;
  right: 0;
  -webkit-transform: translatex(40%);
  transform: translatex(40%);
  border: 1px solid #180c0b;
  box-shadow: inset -30px 0 0 3px #ffd100;
}
.sofa .sofa-cushion {
  width: 300px;
  height: 40px;
  border-radius: 30px;
  background: #ffd100;
  border: 1px solid #180c0b;
  position: absolute;
  z-index: 1;
  bottom: -40px;
  left: 50%;
  -webkit-transform: translatex(-50%);
  transform: translatex(-50%);
  box-shadow: inset -150px 0 #efbb02;
}
.sofa .sofa-cushion:after {
  content: '';
  width: 130px;
  height: 70px;
  background: #efbb02;
  border-radius: 25px;
  position: absolute;
  bottom: -1px;
  left: -10px;
  border: 1px solid #180c0b;
  box-shadow: inset -30px 0 0 3px #ffd100;
}
.sofa .sofa-bottom-l,
.sofa .sofa-bottom-r {
  width: 20px;
  height: 40px;
  background: #cea77a;
  position: absolute;
  bottom: -79px;
  border: 1px solid #180c0b;
  -webkit-transform: skew(-30deg);
  transform: skew(-30deg);
  box-shadow: inset 5px 10px #b2906a;
}
.sofa .sofa-bottom-l:after,
.sofa .sofa-bottom-r:after {
  content: '';
  width: 20px;
  height: 40px;
  background: #cea77a;
  position: absolute;
  bottom: -1px;
  border: 1px solid #180c0b;
  -webkit-transform: skew(50deg);
  transform: skew(50deg);
  box-shadow: inset -5px 10px #b2906a;
}
.sofa .sofa-bottom-r {
  right: -20px;
  -webkit-transform: skew(30deg);
  transform: skew(30deg);
}
.sofa .sofa-bottom-r:after {
  right: 70px;
  -webkit-transform: skew(-50deg);
  transform: skew(-50deg);
}
.sofa .sofa-bottom-l:after {
  left: 70px;
}

.cat {
  position: absolute;
  z-index: 1;
  width: 75px;
  height: 50px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #d3d2d4;
  bottom: -15px;
  right: 93px;
  box-shadow: inset 14px -33px #fff;
}
.cat:after {
  content: '';
  background: #ffd100;
  border: 1px solid #180c0b;
  width: 150px;
  height: 40px;
  position: absolute;
  bottom: -25px;
  border-radius: 0 50px 50px 100px;
}
.cat .cat-head {
  position: absolute;
  width: 45px;
  height: 45px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fff;
  bottom: 5px;
  right: -10px;
  -webkit-transform: rotate(-14deg);
  transform: rotate(-14deg);
  -webkit-animation: sleep 2s infinite alternate linear;
  animation: sleep 2s infinite alternate linear;
}
.cat .cat-head .cat-ear-l,
.cat .cat-head .cat-ear-r {
  position: absolute;
  width: 25px;
  height: 13px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fff;
  top: -5px;
}
.cat .cat-head .cat-ear-l:after,
.cat .cat-head .cat-ear-r:after {
  content: '';
  width: 9px;
  height: 19px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fe8aa5;
  position: absolute;
  top: -2px;
  -webkit-transform: rotate(98deg);
  transform: rotate(98deg);
  left: 4px;
}
.cat .cat-head .cat-ear-l {
  left: 0;
  -webkit-transform: rotate(35deg);
  transform: rotate(35deg);
}
.cat .cat-head .cat-ear-l:before {
  content: '';
  background: #fff;
  width: 33px;
  height: 21px;
  border-radius: 50%;
  position: absolute;
  -webkit-transform: rotate(-35deg);
  transform: rotate(-35deg);
  left: 7px;
  top: -3px;
  z-index: 1;
}
.cat .cat-head .cat-ear-r {
  background: #d3d2d4;
  right: 3px;
  top: -5px;
  -webkit-transform: rotate(55deg);
  transform: rotate(55deg);
}
.cat .cat-head .cat-ear-r:before {
  content: '';
  background: #d3d2d4;
  width: 26px;
  height: 21px;
  border-radius: 50%;
  position: absolute;
  -webkit-transform: rotate(-55deg);
  transform: rotate(-55deg);
  left: 4px;
  top: 3px;
  z-index: 1;
}
.cat .cat-head .cat-eye-l,
.cat .cat-head .cat-eye-r {
  width: 7px;
  height: 5px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #d3d2d4;
  position: absolute;
  top: 12px;
  border-top: 0;
  border-right: 0;
  border-left: 0;
}
.cat .cat-head .cat-eye-l {
  left: 11px;
}
.cat .cat-head .cat-eye-r {
  right: 11px;
}
.cat .cat-arm-shoulder {
  position: absolute;
  width: 26px;
  height: 15px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fff;
  bottom: 20px;
  right: -22px;
  -webkit-transform: rotate(-122deg);
  transform: rotate(-122deg);
}
.cat .cat-arm-l,
.cat .cat-arm-r {
  position: absolute;
  width: 35px;
  height: 13px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fff;
  bottom: 10px;
}
.cat .cat-arm-l {
  -webkit-transform: rotate(-10deg);
  transform: rotate(-10deg);
  right: -18px;
}
.cat .cat-arm-l:after {
  content: '';
  background: #fff;
  width: 8px;
  height: 11px;
  position: absolute;
  border-radius: 50%;
  right: -1px;
  top: -3px;
}
.cat .cat-arm-r {
  -webkit-transform: rotate(10deg);
  transform: rotate(10deg);
  right: 15px;
}
.cat .cat-tale {
  position: absolute;
  width: 25px;
  height: 13px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #fff;
  bottom: 10px;
  left: 10px;
  -webkit-transform: rotate(-45deg);
  transform: rotate(-45deg);
}

.boy {
  position: absolute;
  width: 65px;
  height: 100px;
  border-radius: 150px 25px 25px 80px;
  border: 1px solid #180c0b;
  background: #d8d8d8;
  bottom: -80px;
  left: 80px;
  z-index: 1;
  box-shadow:
    inset -28px -10px #40505e,
    inset 8px 1px #c6c6c6;
}
.boy .arm-l,
.boy .arm-r {
  position: absolute;
  width: 75px;
  height: 25px;
  border-radius: 12px;
  border: 1px solid #180c0b;
  background: #d8d8d8;
  bottom: 40px;
  left: 30px;
  -webkit-transform: rotate(30deg);
  transform: rotate(30deg);
  box-shadow: inset -9px -7px #c6c6c6;
}
.boy .arm-l {
  -webkit-animation: type 0.1s 0.1s infinite alternate;
  animation: type 0.1s 0.1s infinite alternate;
}
.boy .arm-r {
  width: 85px;
  height: 25px;
  bottom: 35px;
  left: 5px;
  z-index: 1;
  border-radius: 12px 0 0 12px;
  border-left: 3px solid #d8d8d8;
  -webkit-animation: type 0.1s infinite alternate;
  animation: type 0.1s infinite alternate;
}
.boy .arm-r:before {
  content: '';
  width: 20px;
  height: 18px;
  border: 1px solid #180c0b;
  background: #e8c6a9;
  bottom: 3px;
  right: -20px;
  position: absolute;
}
.boy .foot-l,
.boy .foot-r {
  position: absolute;
  width: 30px;
  height: 85px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #526576;
  bottom: 0;
  right: -20px;
  box-shadow: inset 10px 1px rgba(47, 59, 70, 0.6);
}
.boy .foot-l:before,
.boy .foot-r:before {
  content: '';
  width: 35px;
  height: 15px;
  border: 1px solid #180c0b;
  background: #526576;
  position: absolute;
  bottom: 10px;
  left: 50%;
  -webkit-transform: translate(-50%);
  transform: translate(-50%);
  z-index: 1;
  box-shadow: inset 15px 1px rgba(47, 59, 70, 0.6);
}
.boy .foot-l:after,
.boy .foot-r:after {
  content: '';
  width: 50px;
  height: 15px;
  border: 1px solid #180c0b;
  border-radius: 0 100px 0 0;
  background: #266780;
  position: absolute;
  bottom: -2px;
  left: 0;
}
.boy .foot-r {
  right: 0;
}
.boy .head {
  position: absolute;
  width: 65px;
  height: 60px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #d8d8d8;
  top: -33px;
  left: 6px;
  -webkit-transform: rotate(-20deg);
  transform: rotate(-20deg);
  box-shadow: inset 6px 8px #c6c6c6;
  -webkit-animation: head 2s linear infinite alternate;
  animation: head 2s linear infinite alternate;
}
.boy .face {
  position: absolute;
  width: 40px;
  height: 60px;
  border-radius: 50%;
  border: 1px solid #180c0b;
  background: #e8c6a9;
  bottom: -11px;
  right: 6px;
  -webkit-transform: rotate(52deg);
  transform: rotate(52deg);
  overflow: hidden;
  box-shadow: inset 14px 8px rgba(201, 158, 121, 0.6);
}
.boy .hair-1 {
  position: absolute;
  border-left: 50px solid transparent;
  border-right: 50px solid transparent;
  border-bottom: 100px solid #000;
  bottom: 3px;
  right: 13px;
  -webkit-transform: rotate(7deg);
  transform: rotate(7deg);
}
.boy .hair-2 {
  position: absolute;
  border-left: 50px solid transparent;
  border-right: 50px solid transparent;
  border-bottom: 100px solid #000;
  bottom: 24px;
  right: 23px;
  -webkit-transform: rotate(-15deg);
  transform: rotate(-15deg);
}
.boy .hair-3 {
  position: absolute;
  border-left: 20px solid transparent;
  border-right: 4px solid transparent;
  border-bottom: 34px solid #000;
  bottom: 19px;
  right: -12px;
  -webkit-transform: rotate(-66deg) skew(11deg) scale(-1);
  transform: rotate(-66deg) skew(11deg) scale(-1);
}
.boy .eye-l,
.boy .eye-r {
  position: absolute;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #000;
  bottom: 30px;
  right: 5px;
}
.boy .eye-r {
  right: 13px;
  bottom: 26px;
}

.laptop {
  position: absolute;
  width: 75px;
  height: 7px;
  border-radius: 5px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: #000000;
  bottom: -1px;
  right: -67px;
}
.laptop:after {
  content: '';
  position: absolute;
  width: 70px;
  height: 55px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: #000000;
  bottom: -1px;
  right: -30px;
  z-index: 1;
  -webkit-transform: skew(-25deg);
  transform: skew(-25deg);
}
.laptop .light {
  width: 127px;
  height: 300px;
  opacity: 0.3;
  background: -webkit-gradient(
    linear,
    left top,
    left bottom,
    from(rgba(255, 255, 255, 0.8)),
    to(rgba(255, 255, 255, 0))
  );
  background: linear-gradient(rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0));
  -webkit-clip-path: polygon(20% 0%, 80% 0%, 100% 100%, 0% 100%);
  clip-path: polygon(20% 0%, 80% 0%, 100% 100%, 0% 100%);
  position: absolute;
  z-index: 1;
  -webkit-transform: rotate(133deg) translate(-130px, 263px);
  transform: rotate(133deg) translate(-130px, 263px);
  -webkit-animation: light 1s infinite alternate cubic-bezier(1, -0.93, 0.08, 0.18);
  animation: light 1s infinite alternate cubic-bezier(1, -0.93, 0.08, 0.18);
}

@-webkit-keyframes type {
  to {
    -webkit-transform: rotate(35deg);
    transform: rotate(35deg);
  }
}

@keyframes type {
  to {
    -webkit-transform: rotate(35deg);
    transform: rotate(35deg);
  }
}
@-webkit-keyframes light {
  to {
    opacity: 0.5;
  }
}
@keyframes light {
  to {
    opacity: 0.5;
  }
}
@-webkit-keyframes sleep {
  to {
    -webkit-transform: rotate(14deg);
    transform: rotate(14deg);
  }
}
@keyframes sleep {
  to {
    -webkit-transform: rotate(14deg);
    transform: rotate(14deg);
  }
}
@-webkit-keyframes head {
  from {
    -webkit-transform: rotate(-20deg);
    transform: rotate(-20deg);
  }
  to {
    -webkit-transform: rotate(-20deg) translatey(3px);
    transform: rotate(-20deg) translatey(3px);
  }
}
@keyframes head {
  from {
    -webkit-transform: rotate(-20deg);
    transform: rotate(-20deg);
  }
  to {
    -webkit-transform: rotate(-20deg) translatey(3px);
    transform: rotate(-20deg) translatey(3px);
  }
}
</style>
