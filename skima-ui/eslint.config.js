// @ts-check
const eslint = require('@eslint/js');
const tseslint = require('typescript-eslint');
const angular = require('angular-eslint');

module.exports = tseslint.config(
  {
    files: ['**/*.ts'],
    ignores: ['src/app/api/**/*', '**/auto-unsubscribe.ts'],
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      semi: [2, 'always'],
      '@typescript-eslint/no-unused-vars': ['warn'],
      '@typescript-eslint/adjacent-overload-signatures': ['off'],
      '@typescript-eslint/no-empty-function': ['off'],
      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          prefix: 'skima',
          style: 'camelCase',
        },
      ],
      '@typescript-eslint/no-explicit-any': [
        'off'
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'skima',
          style: 'kebab-case',
        },
      ],
      '@angular-eslint/component-class-suffix': ['off'],
    },
  },
  {
    files: ['**/*.html'],
    extends: [...angular.configs.templateRecommended, ...angular.configs.templateAccessibility],
    rules: {},
  },
);
