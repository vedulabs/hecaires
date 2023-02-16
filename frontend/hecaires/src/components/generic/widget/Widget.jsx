import PropTypes from 'prop-types';
import './Widget.scss';

function Widget({
  children,
  style
}) {
  return(
    <section
      className="widget"
      style={style}
    >
      {children}
    </section>
  );
}

Widget.defaultProps = {
}

Widget.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node
  ]).isRequired,
  style: PropTypes.object,
}

export default Widget;