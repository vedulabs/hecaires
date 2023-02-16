import './Button.scss';
import PropTypes from 'prop-types';

/**
 * @param {{
 *  content: import('react').ReactElement | HTMLElement | string,
 *  classes?: [string],
 *  style: object
 * }} props 
 */
function Button({
  type,
  content,
  classes,
  style,
  onClick,
  disabled
}) {
  return(
    <button
      type={type}
      className={[
        'prevent-select',
        disabled && 'disabled',
        ...classes
      ].filter(Boolean).join(' ')}
      style={style}
      onClick={onClick && onClick}
      disabled={disabled}
    >
      {content}
    </button>
  );
}

Button.defaultProps = {
  type:'button',
  classes: [
    'default'
  ],
  disabled: false
}

Button.propTypes = {
  type: PropTypes.oneOf([
    'button',
    'submit',
    'reset'
  ]),
  content: PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.element,
    PropTypes.node
  ]).isRequired,
  classes: PropTypes.arrayOf(PropTypes.string),
  style: PropTypes.object,
  onClick: PropTypes.func,
  disabled: PropTypes.bool
}

export default Button;