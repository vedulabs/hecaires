import PropTypes from 'prop-types';
import './FormContainer.scss';

function FormContainer({
  children,
  classes,
  style,
  onSubmit
}) {
  return(
    <form
      style={style}
      className={classes?.filter(Boolean).join(' ')}
      onSubmit={onSubmit}
      method='post'
    >
      {children}
    </form>
  );
}

FormContainer.defaultProps = {
  classes: []
}

FormContainer.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node
  ]).isRequired,
  classes: PropTypes.arrayOf(PropTypes.string),
  style: PropTypes.object,
  onSubmit: PropTypes.func.isRequired
}


export default FormContainer;