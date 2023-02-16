export const loadStateSlice = (stateSliceName) => {
  try {
    const serializedStateSlice = localStorage.getItem(stateSliceName);
    if (serializedStateSlice === null) {
      return undefined;
    }
    return JSON.parse(serializedStateSlice);
  } catch (error) {
    console.warn(error);
    return undefined;
  }
};

export const saveStateSlice = (stateSliceName, stateSlice) => {
  try {
    const serializedStateSlice = JSON.stringify(stateSlice);
    localStorage.setItem(stateSliceName, serializedStateSlice);
  } catch (error) {
    console.warn(error);
  }
};

export function wrapError(errorMessage) {
  return `${errorMessage} [${new Date().toJSON().replaceAll(/[T,Z]/ig, ' ').trim()}]`;
}