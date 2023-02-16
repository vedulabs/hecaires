import './Footer.scss';

export default function Footer() {
  return(
    <footer>
      <small>© Hecaires {new Date().getFullYear()}</small>
    </footer>
  );
}