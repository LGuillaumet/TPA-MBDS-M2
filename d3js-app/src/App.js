import { HashRouter } from 'react-router-dom';
import apiClient from './api/apiClient';
import Routes from './Routes';
import './App.scss';

const App = () => {
  return (
    <div className="App">
      <HashRouter>
        <Routes />
      </HashRouter>
    </div>
  );
}

export default App;