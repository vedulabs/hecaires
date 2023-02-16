import './Spinner.scss';
import PropTypes from 'prop-types';

function Spinner({
  variant,
  style
}) {
  return(
    <div
      className={`lds-${variant}`}
      style={{
        ...style
      }}
    >
      <div></div><div></div><div></div><div></div>
    </div>
  );
}

Spinner.defaultProps = {
  style: {}
}

Spinner.propTypes = {
  variant: PropTypes.oneOf([
    'ellipsis',
    'ring'
  ]).isRequired,
  style: PropTypes.object
}

export default Spinner;