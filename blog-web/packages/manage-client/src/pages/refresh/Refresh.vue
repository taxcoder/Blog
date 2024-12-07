<script setup lang="ts">
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

//@ts-ignore
onMounted(() => router.replace(route.query.f));
</script>

<template></template>

<style scoped></style>
