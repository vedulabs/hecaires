import { configureStore } from '@reduxjs/toolkit';
import thunkMiddleware from 'redux-thunk';
import authenticationReducer from './state-slices/authentication-redux-state-slice';

export default configureStore({
  reducer: {
    authentication: authenticationReducer
  },
  middleware: (getDefaultMiddleware) => [...getDefaultMiddleware(), thunkMiddleware]
});