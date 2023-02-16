import { Link } from 'react-router-dom';
import './LinkButton.scss';
import PropTypes from 'prop-types';

/**
 * @param {
 *  path: string,
 *  content: import('react').ReactElement | HTMLElement | string,
 *  title: string
 * } props 
 */
function LinkButton({
  path,
  content,
  title,
  classes,
  style
}) {
  return(
    <Link
      to={path}
      title={title}
      className={[
        'link-button',
        'prevent-select',
        ...classes?.filter(Boolean)
      ].join(' ')}
      style={style}
    >
      {content}
    </Link>
  );
}

LinkButton.defaultProps = {
  classes: [
    'default'
  ]
}

LinkButton.propTypes = {
  path: PropTypes.string.isRequired,
  content: PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.element,
    PropTypes.node
  ]).isRequired,
  title: PropTypes.string,
  classes: PropTypes.arrayOf(PropTypes.string),
  style: PropTypes.object,
}

export default LinkButton;