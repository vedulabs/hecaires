let IDX = 36;
let HEX = '';
while (IDX--) HEX += IDX.toString(36);

export function uuid(len) {
  let str = String.fromCharCode(97 + Math.floor(Math.random() * 26));
  let num = len - 1 || 31;
  while (num--) str += HEX[Math.random() * 36 | 0];
  return str;
}