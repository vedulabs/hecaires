import { useCallback } from 'react';
import { Link, useMatch, useNavigate } from 'react-router-dom';
import Button from '../../generic/button/Button';
import './Header.scss';
import MobileMenu from './mobile-menu/MobileMenu';
import Navigation from './navigation/Navigation';
import { logo192PNG } from '../../../constants';
import { useDispatch, useSelector } from 'react-redux';

function Header() {
  const navigate = useNavigate();

  const onLoginBtnClick = useCallback(() => {
    navigate('/login')
  }, [navigate]);

  const dispatch = useDispatch();

  const onLogoutBtnClick = useCallback(() => {
    dispatch({
      type: 'authentication/signout'
    });

    navigate('/login');
  }, [dispatch, navigate]);

  const matchLoginView = useMatch('/login');

  const {
    authenticated,
  } = useSelector(state => state.authentication);

  return(
    <header>
      <Link
        to='/'
        title='Hecaires Home'
      >
        <div id="home-anchor">
          <div id="header-logo">
            <img
              // src={process.env.PUBLIC_URL + "/logo192.png"}
              src={logo192PNG}
              alt="Health Care on AI resources"
            />
          </div>
          <h1>ecaires</h1>
        </div>
      </Link>
      <MobileMenu />
      <div className="flex-expander"/>
      <Navigation />
      <div className="flex-expander"/>
      {!Boolean(matchLoginView) && !authenticated && 
        <Button
          content={'LOGIN'}
          classes={[
            'primary',
            'login-btn'
          ]}
          style={{minWidth: '10%'}}
          onClick={onLoginBtnClick}
        />
      }
      {authenticated && 
        <Button
          content={
            <svg viewBox="0 0 24 24">
              <path d="M10.09 15.59 11.5 17l5-5-5-5-1.41 1.41L12.67 11H3v2h9.67l-2.58 2.59zM19 3H5c-1.11 0-2 .9-2 2v4h2V5h14v14H5v-4H3v4c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z"></path>
            </svg>
          }
          classes={[
            'container',
            'logout-button'
          ]}
          onClick={onLogoutBtnClick}
        />
      }
    </header>
  );
}

export default Header;