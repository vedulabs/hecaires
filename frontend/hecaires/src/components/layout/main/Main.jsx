import { lazy, Suspense, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Route, Routes } from 'react-router-dom';
import { verifyAuthenticationAction } from '../../../redux/state-slices/authentication-redux-state-slice';
import HomeView from '../../views/public/home/HomeView';
import './Main.scss';

const LoginView = lazy(() => import('../../views/public/login/LoginView'));
const MedResearchView = lazy(() => import('../../views/public/med-research/MedResearchView'));
const AboutView = lazy(() => import('../../views/public/about/AboutView'));

const DashboardView = lazy(() => import('../../views/private/dashboard/DashboardView'));
const ProfileView = lazy(() => import('../../views/private/profile/ProfileView'));

function Main() {
  const authenticated = useSelector(state => state.authentication.authenticated);

  const dispatch = useDispatch();

  useEffect(() => {
    if (authenticated) {
      dispatch(verifyAuthenticationAction);
    }
  }, [authenticated, dispatch]);

  return(
    <main>
        <Suspense fallback={<div>loading...</div>}>
          <Routes>
              <Route 
                exact path='/'
                element={
                  <HomeView />
                }
              />
              <Route 
                exact path='/medical-research'
                element={
                  <MedResearchView />
                }
              />
              <Route 
                exact path='/about'
                element={
                  <AboutView />
                }
              />
              <Route 
                exact path='/login'
                element={
                  <LoginView />
                }
              />
              {authenticated &&
                <>
                  <Route 
                    exact path='/dashboard'
                    element={
                      <DashboardView />
                    }
                  />
                  <Route 
                    exact path='/profile'
                    element={
                      <ProfileView />
                    }
                  />
                </>
              }
              <Route 
                exact path='/*'
                element={
                  <LoginView />
                }
              />
          </Routes>
        </Suspense>
    </main>
  );
}

export default Main;