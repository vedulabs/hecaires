import { createSlice } from '@reduxjs/toolkit';
import HecairesAPI from '../../communications/HecairesAPI';
import { loadStateSlice, saveStateSlice, wrapError } from './utils';

const defaultStateSlice = {
  authenticated: false,
  id: null,
  token: null,
  username: null,
  email: null,
  roles: [],
  authenticationError: null
};

const authenticationReduxStateSlice = createSlice({
  name: 'authentication',
  initialState: loadStateSlice('authentication') || defaultStateSlice,
  reducers: {
    signin: (state, action) => {
      if (!action.payload.error) {
        if (action.payload.statusCode && action.payload.statusCode !== 200) {
          return {
            ...defaultStateSlice,
            authenticationError: wrapError(action.payload.description)
          };
        } else {
          const authenticatedState = {
            ...defaultStateSlice,
            authenticated: true,
            id: action.payload.id,
            token: action.payload.token,
            username: action.payload.username,
            email: action.payload.email,
            roles: action.payload.roles
          };
          saveStateSlice('authentication', authenticatedState);
          return authenticatedState;
        }
      } else {
        console.warn(action.payload.error);
        return {
          ...defaultStateSlice,
          authenticationError: wrapError('System encountered a problem, please try again later...')
        };
      }
    },
    signup: state => {
    },
    verify: (state, action) => {
      if (!action.payload.error) {
        if (action.payload.statusCode && action.payload.statusCode !== 200) {
          const nonAuthenticatedState = {
            ...defaultStateSlice
          };

          saveStateSlice('authentication', nonAuthenticatedState);

          return nonAuthenticatedState;
        } else {
          const authenticatedState = {
            ...state,
            authenticated: true,
            id: action.payload.id,
            username: action.payload.username,
            email: action.payload.email,
            roles: action.payload.roles
          };

          saveStateSlice('authentication', authenticatedState);

          return authenticatedState;
        }
      } else {
        console.warn(action.payload.error);
        return {
          ...defaultStateSlice,
          authenticationError: wrapError('System encountered a problem, please try again later...')
        };
      }
    },
    signout: state => {
      const nonAuthenticatedState = {
        ...defaultStateSlice
      };

      saveStateSlice('authentication', nonAuthenticatedState);

      return nonAuthenticatedState;
    }
  }
});

export default authenticationReduxStateSlice.reducer;

export function signInAction(credentials) {
  return async function signInActionThunk(dispatch, getState) {
    const { responsePromise } = HecairesAPI.getInstance()
    .request(
      HecairesAPI.endpoints.api.auth.signin,
      credentials
    );

    try {
      const response = await responsePromise;

      dispatch({
        type: 'authentication/signin',
        payload: response
      });
    } catch (error) {
      dispatch({
        type: 'authentication/signin',
        payload: {
          error: error.message
        }
      });
    }
  }
}

export async function verifyAuthenticationAction(dispatch, getState) {
  console.log('verifying authentication...');
  const state = getState();
  const { token } = state.authentication;
  HecairesAPI.getInstance()
  .addHeader({
    name: 'Authorization',
    value: `Bearer ${token}`
  });


  const { responsePromise } = HecairesAPI.getInstance()
  .request(HecairesAPI.endpoints.api.auth.verify);

  try {
    const response = await responsePromise;

    dispatch({
      type: 'authentication/verify',
      payload: response
    });
  } catch (error) {
    console.log(error)
    dispatch({
      type: 'authentication/verify',
      payload: {
        error: error.message
      }
    });
  }
}