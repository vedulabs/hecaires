import Footer from './footer/Footer';
import Header from './header/Header';
import './Layout.scss';
import Main from './main/Main';

export default function Layout() {
  return(
    <div id="layout">
      <Header />
      <Main />
      <Footer />
    </div>
  );
}