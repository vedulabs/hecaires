import './MobileMenu.scss';
import '../../../../styling/default/overlay.scss';
import Button from '../../../generic/button/Button';
import { useLayoutEffect, useState } from 'react';
import Navigation from '../navigation/Navigation';

function MobileMenu() {
  const [shown, setShown] = useState(false);
  const [menuContainerShown, setMenuContainerShown] = useState(false);

  useLayoutEffect(() => {
    if (shown) {
      setTimeout(() => setMenuContainerShown(shown), 400);
    } else {
      setMenuContainerShown(shown);
    }
  }, [shown, setMenuContainerShown])

  return(
    <>
      <Button
        content={
          <svg focusable="false" aria-hidden="true" viewBox="0 0 24 24">
            <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"></path>
          </svg>
        }
        classes={[
          'mobile-menu-btn',
          'all-transitions-hals-sec',
          shown && 'hidden-with-opacity'
        ].filter(Boolean)}
        onClick={() => setShown(true)}
      />
      <div
        className={[
          'overlay',
          shown && 'active'
        ].filter(Boolean).join(' ')}
        onClick={() => setShown(false)}
      >
        <div
          className={[
            'mobile-menu-container',
            menuContainerShown && 'visible'
          ].filter(Boolean).join(' ')}
        >
          <Button
            content={
              <svg focusable="false" aria-hidden="true" viewBox="0 0 24 24">
                <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z"></path>
              </svg>
            }
            onClick={() => setShown(false)}
            classes={[
              'container',
              'close-menu-btn'
            ]}
          />
          <div id="header-logo">
            <img
              src={process.env.PUBLIC_URL + "/logo192.png"}
              alt="Health Care on AI resources"
            />
          </div>
          <Navigation
            isMobile={true}
          />
        </div>
      </div>
    </>
  );
}

export default MobileMenu;