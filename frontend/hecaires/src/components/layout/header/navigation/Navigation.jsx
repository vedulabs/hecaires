import './Navigation.scss';
import LinkButton from '../../../generic/link-button/LinkButton';
import PropTypes from 'prop-types';
import { useMatch } from 'react-router-dom';
import { useSelector } from 'react-redux';

function Navigation({
  isMobile
}) {
  const {
    authenticated,
  } = useSelector(state => state.authentication);

  const pathMatches = {
    'about': useMatch('/about'),
    'medical-research': useMatch('/medical-research'),
    'dashboard': useMatch('/dashboard'),
    'profile': useMatch('/profile'),
  };

  return(
    <nav
      className={[
        isMobile && 'mobile'
      ].filter(Boolean).join(' ')}
    >
      {authenticated && 
        <>
            <LinkButton
              path='/dashboard'
              content={
                <div>
                  <svg viewBox="0 0 24 24">
                    <path d="M19 5v2h-4V5h4M9 5v6H5V5h4m10 8v6h-4v-6h4M9 17v2H5v-2h4M21 3h-8v6h8V3zM11 3H3v10h8V3zm10 8h-8v10h8V11zm-10 4H3v6h8v-6z"></path>
                  </svg>
                  <span>DASHBOARD</span>
                </div>
              }
              title='Dashboard'
              classes={[
                'default',
                (pathMatches['dashboard']) && 'selected'
              ].filter(Boolean)}
            />
        </>
      }
      <LinkButton
        path='/medical-research'
        content={'MEDICAL RESEARCH'}
        title='Medical research'
        classes={[
          'default',
          (pathMatches['medical-research']) && 'selected'
        ].filter(Boolean)}
      />
      <LinkButton
        path='/about'
        content={'ABOUT'}
        title='About Hecaires project'
        classes={[
          'default',
          (pathMatches['about']) && 'selected'
        ].filter(Boolean)}
      />
      {isMobile && !authenticated && 
        <>
          <br/>
          <LinkButton
            path='/login'
            content={'LOGIN'}
            classes={['primary']}
          />
        </>
      }
      {authenticated && 
        <>
          <LinkButton
            path='/profile'
            content={
              <div>
                <svg viewBox="0 0 24 24">
                  <path d="M4 18v-.65c0-.34.16-.66.41-.81C6.1 15.53 8.03 15 10 15c.03 0 .05 0 .08.01.1-.7.3-1.37.59-1.98-.22-.02-.44-.03-.67-.03-2.42 0-4.68.67-6.61 1.82-.88.52-1.39 1.5-1.39 2.53V20h9.26c-.42-.6-.75-1.28-.97-2H4zm6-6c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0-6c1.1 0 2 .9 2 2s-.9 2-2 2-2-.9-2-2 .9-2 2-2zm10.75 10c0-.22-.03-.42-.06-.63l1.14-1.01-1-1.73-1.45.49c-.32-.27-.68-.48-1.08-.63L18 11h-2l-.3 1.49c-.4.15-.76.36-1.08.63l-1.45-.49-1 1.73 1.14 1.01c-.03.21-.06.41-.06.63s.03.42.06.63l-1.14 1.01 1 1.73 1.45-.49c.32.27.68.48 1.08.63L16 21h2l.3-1.49c.4-.15.76-.36 1.08-.63l1.45.49 1-1.73-1.14-1.01c.03-.21.06-.41.06-.63zM17 18c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z"></path>
                </svg>
                <span>Profile</span>
              </div>
            }
            title='Profile'
            classes={[
              'default',
              (pathMatches['profile']) && 'selected'
            ].filter(Boolean)}
          />
        </>
      }
    </nav>
  );
}

Navigation.defaultProps = {
  isMobile: false
}

Navigation.propTypes = {
  isMobile: PropTypes.bool
}

export default Navigation;