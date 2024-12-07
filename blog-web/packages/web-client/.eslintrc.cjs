//@ts-i
module.exports = {
  root: true,
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 2020,
    sourceType: 'module',
  },
  ignore: ['node_modules', 'dist', 'build', 'public', 'lib', 'es', 'lib-es'],
  ignorePatterns: ['@tanxiang/utils', '@tanxiang/apis', '@tanxiang/common'],
  env: {
    browser: true,
    es6: true,
  },
  extends: ['eslint:recommended', 'plugin:@typescript-eslint/recommended'],
  rules: {
    // 在这里添加其他的规则
  },
};
