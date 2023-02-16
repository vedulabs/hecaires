import PropTypes from 'prop-types';
import { useCallback, useEffect, useRef, useState } from 'react';
import { uuid } from '../../../utils/uuid';
import './TextInput.scss';

function TextInput({
  type,
  name,
  value,
  inputId=uuid(8),
  label,
  containerStyle,
  inputElementStyle
}) {
  const [state, setState] = useState({
    value,
    filled: false
  });

  const inputRef = useRef();

  useEffect(() => {
    /**
     * The field can be prefilled on the very first page loading by the browser
     * By the security reason browser limits access to the field value from JS level and the value becomes available
     * only after first user interaction with the page
     * So, even if the Formik thinks that the field is not touched by user and empty,
     * it actually can have some value, so we should process this edge case in the form logic
     */
    const checkAutofilled = () => {
      const autofilled = !!inputRef.current?.matches('*:-webkit-autofill');
      if (autofilled) {
        setState(state => ({
          ...state,
          filled: true
        }));
      }
    };
    // The time when it's ready is not very stable, so check few times
    setTimeout(checkAutofilled, 500);
    setTimeout(checkAutofilled, 1000);
    setTimeout(checkAutofilled, 2000);
  }, []);

  useEffect(() => {
    if (state.value) {
      setState(state => ({
        ...state,
        filled: true
      }));
    }
  }, [state.value]);

  const onInputFocusChange = useCallback(event => {
    setState(state => ({
      ...state,
      filled: event.target === document.activeElement || state.value
    }));
  }, []);

  const onChange = useCallback(event => {
    setState(state => ({
      ...state,
      value: event.target.value,
      filled: Boolean(event.target.value)
    }));
  }, []);

  return(
    <div
      className="input-container"
      style={containerStyle}
    >
      <label
        htmlFor={inputId}
        className={[
          state.filled && 'filled'
        ].filter(Boolean).join(' ')}
      >
        {label}
      </label>
      <input
        id={inputId}
        name={name}
        value={state.value}
        type={type}
        style={inputElementStyle}
        onFocus={onInputFocusChange}
        onBlur={onInputFocusChange}
        onKeyUp={onChange}
        onChange={onChange}
        ref={inputRef}
      />
    </div>
  );
}

TextInput.defaultProps = {
  value: ''
}

TextInput.propTypes = {
  type: PropTypes.oneOf([
    'text',
    'password',
    'email'
  ]),
  name: PropTypes.string.isRequired,
  value: PropTypes.string,
  inputId: PropTypes.string,
  label: PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.element,
    PropTypes.node
  ]).isRequired,
  containerStyle: PropTypes.object,
  inputElementStyle: PropTypes.object
}

export default TextInput;