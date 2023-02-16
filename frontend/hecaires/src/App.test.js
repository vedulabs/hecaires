import { render, screen } from '@testing-library/react';
import App from './App';

describe('When App is rendered', () => {
  it('it has logo image in the header', () => {
    render(<App />);
    const logoImage = document.querySelector('header #header-logo img');
    expect(logoImage).toBeInTheDocument();
  });
  it('it has copyright text with correct year in the footer', () => {
    render(<App />);
    const footer = document.querySelector('footer');
    expect(footer).toHaveTextContent(`© Hecaires ${new Date().getFullYear()}`);
  });
});
