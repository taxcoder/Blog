import { h } from 'vue';
import { Input, InputPassword } from 'ant-design-vue';

interface PasswordInterface {
  showCount: boolean;
  allowClear: boolean;
  spanName: string;
  defaultValue: ref<string>;
  onInput: Function;
  maxLength: number;
  placeholder: string;
}

const textCommon = (
  allowClear: boolean,
  showCount: boolean,
  spanName: string,
  defaultValue: ref<string>,
  onInput: Function,
  placeholder: string,
  maxLength: number = 30
) => {
  return h('div', null, [
    h(
      'span',
      {
        className: 'input-prefix',
      },
      spanName
    ),
    h(
      //@ts-ignore
      Input,
      {
        showCount: showCount,
        maxlength: maxLength,
        allowClear: allowClear,
        defaultValue: defaultValue.value,
        onInput: onInput,
        placeholder: placeholder,
      },
      null
    ),
  ]);
};

const passwordCommon = (
  allowClear: boolean,
  showCount: boolean,
  spanName: string,
  defaultValue: ref<string>,
  onInput: Function,
  placeholder: string,
  maxLength: number = 30
) => {
  return h('form', null, [
    h(
      'span',
      {
        className: 'input-prefix',
      },
      spanName
    ),
    h(
      //@ts-ignore
      InputPassword,
      {
        autocomplete: 'off',
        showCount: showCount,
        maxlength: maxLength,
        allowClear: allowClear,
        defaultValue: defaultValue.value,
        onInput: onInput,
        placeholder: placeholder,
      },
      null
    ),
  ]);
};

const text = (arrays: PasswordInterface[]): any[] => {
  let temp: PasswordInterface[] = [];
  arrays.forEach((array: Password) => {
    temp.push(
      textCommon(
        typeof array.showCount === 'boolean' ? array.showCount : true,
        typeof array.allowClear === 'boolean' ? array.allowClear : true,
        array.spanName,
        array.defaultValue,
        array.onInput,
        array.placeholder,
        array.maxLength
      )
    );
  });
  return temp;
};

const password = (arrays: PasswordInterface[]): any[] => {
  let temp: PasswordInterface[] = [];
  arrays.forEach((array: Password) => {
    temp.push(
      passwordCommon(
        typeof array.showCount === 'boolean' ? array.showCount : true,
        typeof array.allowClear === 'boolean' ? array.allowClear : true,
        array.spanName,
        array.defaultValue,
        array.onInput,
        array.placeholder,
        array.maxLength
      )
    );
  });
  return temp;
};

export { text, password };
