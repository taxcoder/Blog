<template>
  <div id="not-found">
    <a-result status="404" title="404" sub-title="Sorry, the page you visited does not exist.">
      <template #extra>
        <a-button type="primary">Back Home</a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
#not-found {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: center;
}
</style>
