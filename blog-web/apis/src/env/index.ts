//@ts-ignore
import dev from './dev';
//@ts-ignore
import prod from './prod';

//@ts-ignore
let base = import.meta.env.MODE === 'development' ? dev : prod;

export default base;