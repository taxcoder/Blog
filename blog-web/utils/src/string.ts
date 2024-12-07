export const trimAll = (ele: any) => {
  if (typeof ele === 'string') {
    return ele.split(/[\t\r\f\n\s]*/g).join('');
  } else {
    console.error(`${typeof ele} is not the expected type, but the string type is expected`);
  }
};