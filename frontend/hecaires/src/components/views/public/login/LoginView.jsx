import './LoginView.scss';
import Widget from '../../../generic/widget/Widget';
import FormContainer from '../../../form/form-container/FormContainer';
import TextInput from '../../../form/text-input/TextInput';
import Button from '../../../generic/button/Button';
import { useCallback, useEffect, useState } from 'react';
import Spinner from '../../../generic/spinners/Spinner';
import { useDispatch, useSelector } from 'react-redux';
import { signInAction } from '../../../../redux/state-slices/authentication-redux-state-slice';
import { useNavigate } from 'react-router-dom';

function LoginView() {
  const navigate = useNavigate();
  const {
    authenticated,
    authenticationError
  } = useSelector(state => state.authentication);

  useEffect(() => {
    if (authenticated) {
      navigate('/dashboard');
    }
  }, [authenticated, navigate]);

  useEffect(() => {
    setLoading(false);
  }, [authenticationError]);

  const [loading, setLoading] = useState(false);

  const dispatch = useDispatch();

  const onSubmit = useCallback(
    /**
     * @param {SubmitEvent} event 
     */
    event => {
      event.preventDefault();
      setLoading(true);
      const loginFormFields = Array.prototype.slice.call(event.target)
      .filter(el => el.name)
      .reduce((fields, el) => ({
        ...fields,
        [el.name]: el.value,
      }), {});

      dispatch(signInAction({
        username: loginFormFields.email,
        password: loginFormFields.password
      }));

    }, [dispatch]);

  return(
    <div className="login-container">
      <Widget
        style={{ width: '80%', maxWidth: '500px', minHeight: '300px' }}
      >
        <h1 className="prevent-select">Welcome to Hecaires!</h1>

        <FormContainer
          classes={[
            'login-form'
          ]}
          onSubmit={onSubmit}
        >
          <TextInput
            type={'email'}
            name={'email'}
            label={'Email'}
          />
          <TextInput
            type={'password'}
            name={'password'}
            label={'Password'}
            containerStyle={{ marginTop: '2rem' }}
          />
          <Button
            type={'submit'}
            classes={['primary', 'accented']}
            style={{ marginTop: '2rem' }}
            content={
              loading
              ? <span>CONTINUE <Spinner variant='ellipsis'/></span>
              : 'CONTINUE'
            }
            disabled={loading}
          />
          <br/>
          {authenticationError &&
            <span className="error">{authenticationError}</span>
          }
        </FormContainer>

        <div className="flex-expander" />
        <p className="footer">
          <svg width="10px" viewBox="0 0 10 12"><path d="M2,12 L8,12 L8,9 L2,9 L2,12 Z M3,6 C3,4.898 3.897,4 5,4 C6.103,4 7,4.898 7,6 L7,7 L3,7 L3,6 Z M5,2 C2.794,2 1,3.794 1,6 L1,7 L0,7 L0,14 L10,14 L10,7 L9,7 L9,6 C9,3.794 7.206,2 5,2 L5,2 Z"></path></svg>
          <span>THIS FORM IS ENCRYPTED & SECURE</span>
        </p>
      </Widget>
    </div>
  );
}

export default LoginView;